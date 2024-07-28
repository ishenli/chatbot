package com.workdance.chatbot.ui.chat.conversation.message.core;

import static com.workdance.chatbot.ui.chat.conversation.message.core.MessageContentType.ContentType_Loading;

import android.os.Parcel;

import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.model.MessageContent;


@ContentTag(type = ContentType_Loading, flag = PersistFlag.Persist)
public class LoadingMessageContent extends MessageContent {
    private String content = "...";

    public LoadingMessageContent() {
    }

    public LoadingMessageContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public MessagePayload encode() {
        MessagePayload payload = super.encode();
        payload.searchableContent = content;
        payload.mentionedType = mentionedType;
        payload.mentionedTargets = mentionedTargets;
        return payload;
    }


    @Override
    public void decode(MessagePayload payload) {
        content = payload.searchableContent;
        mentionedType = payload.mentionedType;
        mentionedTargets = payload.mentionedTargets;
    }

    @Override
    public String digest(Message message) {
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
    }

    protected LoadingMessageContent(Parcel in) {
        super(in);
        this.content = in.readString();
    }

    public static final Creator<LoadingMessageContent> CREATOR = new Creator<LoadingMessageContent>() {
        @Override
        public LoadingMessageContent createFromParcel(Parcel source) {
            return new LoadingMessageContent(source);
        }

        @Override
        public LoadingMessageContent[] newArray(int size) {
            return new LoadingMessageContent[size];
        }
    };
}
