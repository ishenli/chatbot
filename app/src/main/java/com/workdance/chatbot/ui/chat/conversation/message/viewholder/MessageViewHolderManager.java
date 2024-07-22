package com.workdance.chatbot.ui.chat.conversation.message.viewholder;

import android.util.Log;
import android.util.SparseArray;

import com.workdance.chatbot.R;
import com.workdance.chatbot.ui.chat.conversation.message.core.ContentTag;
import com.workdance.chatbot.model.MessageContent;
import com.workdance.chatbot.core.annotation.MessageContentType;

/**
 * 用来管理各种 Message
 */
public class MessageViewHolderManager {
    private static final String TAG = "MsgViewHolderManager";
    private SparseArray<Class<? extends MessageContentViewHolder>> messageViewHolders = new SparseArray<>();
    private SparseArray<Integer> messageReceiveLayoutRes = new SparseArray<>();
    private SparseArray<Integer> messageSendLayoutRes = new SparseArray<>();

    public static MessageViewHolderManager instance = new MessageViewHolderManager();

    public static MessageViewHolderManager getInstance() {
        return instance;
    }

    private MessageViewHolderManager() {
        init();
    }

    private void init() {
        registerMessageViewHolder(TextMessageContentViewHolder.class, R.layout.conversation_item_text_send, R.layout.conversation_item_text_receive);
    }

    private void registerMessageViewHolder(Class<? extends MessageContentViewHolder> clazz, int sendLayoutRes, int receiveLayoutRes) {
        MessageContentType contentType = clazz.getAnnotation(MessageContentType.class);
        Class<? extends MessageContent> clazzes[] = contentType.value();
        for (Class<? extends MessageContent> notificationClazz : clazzes) {
            ContentTag contentTag = notificationClazz.getAnnotation(ContentTag.class);
            if (messageViewHolders.get(contentTag.type()) == null) {
                messageViewHolders.put(contentTag.type(), clazz);
                messageSendLayoutRes.put(contentTag.type(), sendLayoutRes);
                messageReceiveLayoutRes.put(contentTag.type(), receiveLayoutRes);
            } else {
                Log.e(MessageViewHolderManager.class.getSimpleName(), "re-register message view holder " + clazz.getSimpleName());
            }
        }
    }


    public Class<? extends MessageContentViewHolder> getMessageContentViewHolder(int messageType) {
        Class clazz = messageViewHolders.get(messageType);
        if (clazz == null) {
            Log.d(TAG, "not register messageContentViewHolder for messageType " + messageType + ", fall-back to UnknownMessageContentViewHolder");
            return UnkownMessageContentViewHolder.class;
        }
        return clazz;
    }

    public int sendLayoutResId(int messageType) {
        Integer sendLayoutResId = messageSendLayoutRes.get(messageType);
        return sendLayoutResId == null ? R.layout.conversation_item_unknown_send : sendLayoutResId;
    }

    public int receiveLayoutResId(int messageType) {
        Integer receiveLayoutResId = messageReceiveLayoutRes.get(messageType);
        return receiveLayoutResId == null ? R.layout.conversation_item_unknown_receive : receiveLayoutResId;
    }

}
