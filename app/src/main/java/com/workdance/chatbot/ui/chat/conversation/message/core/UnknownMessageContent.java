/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.ui.chat.conversation.message.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.model.MessageContent;

@ContentTag(type = MessageContentType.ContentType_Unknown, flag = PersistFlag.Persist)
public class UnknownMessageContent extends MessageContent {
    private MessagePayload orignalPayload;

    public UnknownMessageContent() {
    }

    public MessagePayload getOrignalPayload() {
        return orignalPayload;
    }

    public void setOrignalPayload(MessagePayload payload) {
        this.orignalPayload = payload;
    }

    @Override
    public MessagePayload encode() {
        return orignalPayload;
    }


    @Override
    public void decode(MessagePayload payload) {
        orignalPayload = payload;
    }

    @Override
    public String digest(Message message) {
        return "未知类型消息(" + (orignalPayload != null ? orignalPayload.type : "") + ")";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.orignalPayload, flags);
    }

    protected UnknownMessageContent(Parcel in) {
        super(in);
        this.orignalPayload = in.readParcelable(MessagePayload.class.getClassLoader());
    }

    public static final Parcelable.Creator<UnknownMessageContent> CREATOR = new Parcelable.Creator<UnknownMessageContent>() {
        @Override
        public UnknownMessageContent createFromParcel(Parcel source) {
            return new UnknownMessageContent(source);
        }

        @Override
        public UnknownMessageContent[] newArray(int size) {
            return new UnknownMessageContent[size];
        }
    };
}
