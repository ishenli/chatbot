package com.example.wechat.chat.conversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.R;
import com.example.wechat.chat.conversation.message.Message;
import com.example.wechat.chat.conversation.message.viewholder.MessageContentViewHolder;
import com.example.wechat.chat.conversation.message.viewholder.MessageViewHolderManager;
import com.example.wechat.chat.conversation.message.viewholder.NormalMessageContentViewHolder;
import com.example.wechat.model.MessageVO;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ConversationMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ConversationFragment fragment;
    private List<MessageVO> messages = new ArrayList<>();
    long oldestMessageUid = Long.MAX_VALUE;
    long oldestMessageId = Long.MAX_VALUE;

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


    public void setMessages(List<MessageVO> messages) {
        if (messages != null && !messages.isEmpty()) {
            for (MessageVO uiMsg : messages) {
                if (uiMsg.message.messageId != 0) {
                    this.messages.add(uiMsg);
                }
            }
            if (!this.messages.isEmpty()) {
                for (int i = 0; i < messages.size(); i++) {
                    Message msg = messages.get(i).message;
                    // 以下是新消息 那条提示消息的 messageId 是 Long.MAX_VALUE
                    if (msg.messageId != Long.MAX_VALUE) {
                        oldestMessageId = msg.messageId;
                        break;
                    }
                }
            }
        } else {
            this.messages = new ArrayList<>();
        }
    }

    public List<MessageVO> getMessages() {
        return messages;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null) {
            return R.layout.conversation_item_loading;
        }
        Message msg = getItem(position).message;
        return msg.direction.value() << 24 | msg.content.getMessageContentType();
    }

    public int getMessagePosition(long messageId) {
        if (messages == null) {
            return -1;
        }
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).message.messageId == messageId) {
                return i;
            }
        }
        return -1;
    }

    public void highlightFocusMessage(int position) {
        messages.get(position).isFocus = true;
        notifyItemChanged(position);
    }
}
