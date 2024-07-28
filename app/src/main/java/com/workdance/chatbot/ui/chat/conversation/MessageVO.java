/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.ui.chat.conversation;


import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.ui.chat.conversation.message.core.LoadingMessageContent;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;

import lombok.Data;

@Data
public class MessageVO {
    public boolean isPlaying;
    public boolean isDownloading;
    public boolean isFocus;
    public boolean isChecked;
    /**
     * media类型消息，上传或下载的进度
     */
    public int progress;
    public Message message;
    public Object extra;

    public boolean continuousPlayAudio;
    public boolean audioPlayCompleted;

    public MessageVO() {};

    public MessageVO(Message message) {
        this.message = message;
    }

    public MessageVO(Message message, Object extra) {
        this.message = message;
        this.extra = extra;
    }

    public static MessageVO createLoadingMessage(String loading, String avatar) {
        LoadingMessageContent messageContent = new LoadingMessageContent(loading);
        Message message1 = new Message();
        Conversation conversation = new Conversation();
        conversation.setType(Conversation.ConversationType.Single);
        message1.setConversation(conversation);
        message1.setDirection(MessageDirection.Receive);
        message1.setContent(messageContent);
        message1.setAvatar(avatar);
        return new MessageVO(message1);
    }
}
