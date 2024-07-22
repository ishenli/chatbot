/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.ui.chat.conversation.message.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MessagePayload implements Parcelable {

    public /*MessageContentType*/ int type;
    public String searchableContent;
    public String pushContent;
    public String pushData;
    public String content;
    public byte[] binaryContent;

    public String extra;

    public int mentionedType;
    public List<String> mentionedTargets;

    public MessageContentMediaType mediaType;
    public String remoteMediaUrl;

    //前面的属性都会在网络发送，下面的属性只在本地存储
    public String localMediaPath;

    //前面的属性都会在网络发送，下面的属性只在本地存储
    public String localContent;

    public MessagePayload() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.searchableContent);
        dest.writeString(this.pushContent);
        dest.writeString(this.pushData);
        dest.writeString(this.content);
        dest.writeByteArray(this.binaryContent);
        dest.writeInt(this.mentionedType);
        dest.writeStringList(this.mentionedTargets);
        dest.writeInt(this.mediaType == null ? -1 : this.mediaType.ordinal());
        dest.writeString(this.remoteMediaUrl);
        dest.writeString(this.localMediaPath);
        dest.writeString(this.localContent);
        dest.writeString(this.extra);
    }

    public MessagePayload(Parcel in) {
        this.type = in.readInt();
        this.searchableContent = in.readString();
        this.pushContent = in.readString();
        this.pushData = in.readString();
        this.content = in.readString();
        this.binaryContent = in.createByteArray();
        this.mentionedType = in.readInt();
        this.mentionedTargets = in.createStringArrayList();
        int tmpMediaType = in.readInt();
        this.mediaType = tmpMediaType == -1 ? null : MessageContentMediaType.values()[tmpMediaType];
        this.remoteMediaUrl = in.readString();
        this.localMediaPath = in.readString();
        this.localContent = in.readString();
        this.extra = in.readString();
    }

    public static final Creator<MessagePayload> CREATOR = new Creator<MessagePayload>() {
        @Override
        public MessagePayload createFromParcel(Parcel source) {
            return new MessagePayload(source);
        }

        @Override
        public MessagePayload[] newArray(int size) {
            return new MessagePayload[size];
        }
    };
}
