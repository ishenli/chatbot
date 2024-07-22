/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.ui.chat.conversation;


import com.workdance.chatbot.model.Message;

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

    public MessageVO(Message message) {
        this.message = message;
    }

    public MessageVO(Message message, Object extra) {
        this.message = message;
        this.extra = extra;
    }
}
