/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.workdance.chatbot.ui.chat.conversation.message.core.ContentTag;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessagePayload;
import com.workdance.chatbot.ui.chat.conversation.message.core.PersistFlag;

import java.util.List;

public abstract class MessageContent implements Parcelable {
    public abstract void decode(MessagePayload payload);

    public abstract String digest(Message message);

    //0 普通消息, 1 部分提醒, 2 提醒全部
    public int mentionedType;

    //提醒对象，mentionedType 1时有效
    public List<String> mentionedTargets;

    //一定要用json，保留未来的可扩展性
    public String extra;
    public String pushContent;


    final public int getMessageContentType() {
        ContentTag tag = getClass().getAnnotation(ContentTag.class);
        if (tag != null) {
            return tag.type();
        }
        return -1;
    }

    final public PersistFlag getPersistFlag() {
        ContentTag tag = getClass().getAnnotation(ContentTag.class);
        if (tag != null) {
            return tag.flag();
        }
        return PersistFlag.No_Persist;
    }

    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        payload.type = getMessageContentType();
        payload.mentionedType = mentionedType;
        payload.mentionedTargets = mentionedTargets;
        payload.extra = extra;
        payload.pushContent = pushContent;
        return payload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mentionedType);
        dest.writeStringList(this.mentionedTargets);
        dest.writeString(this.extra);
        dest.writeString(this.pushContent);
    }

    public MessageContent() {
    }

    protected MessageContent(Parcel in) {
        this.mentionedType = in.readInt();
        this.mentionedTargets = in.createStringArrayList();
        this.extra = in.readString();
        this.pushContent = in.readString();
    }

}
