package com.workdance.chatbot.ui.chat.conversation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.R;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.chat.conversation.message.core.LoadingMessageContent;
import com.workdance.chatbot.ui.chat.conversation.message.viewholder.MessageContentViewHolder;
import com.workdance.chatbot.ui.chat.conversation.message.viewholder.MessageViewHolderManager;
import com.workdance.chatbot.ui.chat.conversation.message.viewholder.NormalMessageContentViewHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Setter;

public class ConversationMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ConversationFragment fragment;
    private List<MessageVO> messages = new ArrayList<>();

    @Setter
    private OnPortraitClickListener onPortraitClickListener;
    public ConversationMessageAdapter(ConversationFragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int direction = viewType >> 24;
        int messageType = viewType & 0x7FFFFF;

        Class<? extends MessageContentViewHolder> viewHolderClazz = MessageViewHolderManager.getInstance().getMessageContentViewHolder(messageType);
        int sendResId = MessageViewHolderManager.getInstance().sendLayoutResId(messageType);
        int receiveResId = MessageViewHolderManager.getInstance().receiveLayoutResId(messageType);

        View itemView;
        ViewStub viewStub;
        if (direction == 0) {
            itemView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.conversation_item_message_container_send, parent, false);
            viewStub = itemView.findViewById(R.id.contentViewStub);
            viewStub.setLayoutResource(sendResId);
        } else {
            itemView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.conversation_item_message_container_receive, parent, false);
            viewStub = itemView.findViewById(R.id.contentViewStub);
            viewStub.setLayoutResource(receiveResId);
        }

        View view = viewStub.inflate();
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(null);
        }

        try {
            Constructor constructor = viewHolderClazz.getConstructor(ConversationFragment.class, RecyclerView.Adapter.class, View.class);
            MessageContentViewHolder viewHolder = (MessageContentViewHolder) constructor.newInstance(fragment, this, itemView);
            return viewHolder;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageContentViewHolder viewHolder = (MessageContentViewHolder) holder;
        ((MessageContentViewHolder) holder).onBind(getItem(position), position);
        MessageItemView itemView = (MessageItemView) holder.itemView;

        processContentLongClick(viewHolder.getClass(), viewHolder, itemView);
        if (holder instanceof NormalMessageContentViewHolder) {
            processPortraitClick(viewHolder, itemView);
            processPortraitLongClick(viewHolder, itemView);
        }
    }

    private void processContentLongClick(Class<? extends MessageContentViewHolder> viewHolderClazz, MessageContentViewHolder viewHolder, View itemView) {
    }
    private void processPortraitClick(MessageContentViewHolder viewHolder, View itemView) {
        itemView.findViewById(R.id.portraitImageView).setOnClickListener(v -> {
            if (onPortraitClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                MessageVO message = getItem(position);
                // 点击头像
                onPortraitClickListener.onPortraitClick(message.message.sender);
            }
        });
    }

    private void processPortraitLongClick(MessageContentViewHolder viewHolder, View itemView) {

    }

    public MessageVO getItem(int position) {
        return messages.get(position);
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null) {
            return R.layout.conversation_item_loading;
        }
        Message msg = getItem(position).message;
        return msg.direction.value() << 24 | msg.content.getMessageContentType();
    }

    public int getMessagePosition(String messageId) {
        if (messages == null) {
            return -1;
        }
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).message.getMessageId() == messageId) {
                return i;
            }
        }
        return -1;
    }

    public void highlightFocusMessage(int position) {
        messages.get(position).isFocus = true;
        notifyItemChanged(position);
    }



    /**
     * 各种消息的操作
     * @param messages
     */
    public void setMessages(List<MessageVO> messages) {
        if (messages != null && !messages.isEmpty()) {
            for (MessageVO uiMsg : messages) {
                if (uiMsg.message.getMessageId() != null) {
                    this.messages.add(uiMsg);
                }
            }
        } else {
            this.messages = new ArrayList<>();
        }
    }

    public List<MessageVO> getMessages() {
        return messages;
    }

    public void addNewMessage(MessageVO message) {
        if (message == null) {
            return;
        }
        int index = contains(message);
        if (index >= 0) {
            updateMessage(index, message);
            return;
        }
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void addMessagesAtTail(List<MessageVO> messages) {
        if (messages == null) {
            return;
        }
        int insertStartPosition = this.messages.size();
        this.messages.addAll(messages);
        // notifyItemRangeInserted 介绍
        notifyItemRangeInserted(insertStartPosition, messages.size());
    }

    public void updateMessage(int index, MessageVO message) {
        if (index >= 0) {
            messages.set(index, message);
            notifyItemChanged(index);
        }
    }

    public void updateMessage(MessageVO message) {
        int index = -1;
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(message.message.getUniqueMessageId())) {
                // 聊天室消息收到的消息
                if (Objects.equals(messages.get(i).message.getUniqueMessageId(), message.message.getUniqueMessageId())) {
                    messages.set(i, message);
                    index = i;
                    break;
                }
            }
        }
        if (index > -1) {
            notifyItemChanged(index);
        }
    }

    public boolean showStreamLoading(String loadText, String avatar) {
        boolean isInsert = false;
        if (!this.messages.isEmpty() && this.messages.get(this.messages.size() - 1).message.content instanceof LoadingMessageContent) {
            LoadingMessageContent lastLoadingMessageContent = (LoadingMessageContent) this.messages.get(this.messages.size() - 1).message.content;
            if (!TextUtils.isEmpty(loadText)) {
                lastLoadingMessageContent.setContent(loadText);
            }
            notifyItemChanged(this.messages.size() - 1);
        } else {
            this.messages.add(MessageVO.createLoadingMessage(loadText, avatar));
            notifyItemInserted(this.messages.size() - 1);
            isInsert = true;
        }

        return isInsert;
    }

    public void hideStreamLoading() {
        MessageVO loadingMessage = null;
        List<MessageVO> datas = this.messages;
        int loadingIdx = datas.size() - 1;
        for (int i = datas.size() - 1; i >= 0; i--) {
            MessageVO current = datas.get(i);
            if (current.message.content instanceof LoadingMessageContent) {
                loadingIdx = i;
                loadingMessage = current;
            }
        }

        if (loadingMessage != null) {
            datas.remove(loadingIdx);
            notifyItemRemoved(loadingIdx);
        }
    }


    private int contains(MessageVO message) {
        int index = -1;
        for (int i = 0; i < messages.size(); i++) {
            MessageVO msg = messages.get(i);
            if (Objects.equals(msg.message.getUniqueMessageId(), message.message.getUniqueMessageId())) {
                index = i;
                break;
            }
        }
        return index;
    }



    public interface OnPortraitClickListener {
        void onPortraitClick(String sender);
    }

    public interface OnPortraitLongClickListener {
        void onPortraitLongClick(UserInfo userInfo);
    }

    public interface OnMessageCheckListener {
        void onMessageCheck(MessageVO uiMessage, boolean checked);
    }

    public interface OnMessageReceiptClickListener {
        void onMessageReceiptCLick(Message message);
    }
}
