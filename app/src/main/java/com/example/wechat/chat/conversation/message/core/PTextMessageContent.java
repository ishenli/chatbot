package com.example.wechat.chat.conversation.message.core;

import static com.example.wechat.chat.conversation.message.core.MessageContentType.ContentType_P_Text;

import android.os.Parcel;

import com.example.wechat.model.Message;

@ContentTag(type = ContentType_P_Text, flag = PersistFlag.Persist)
public class PTextMessageContent extends TextMessageContent {
    private String content;

    public PTextMessageContent() {
    }

    public PTextMessageContent(String content) {
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
        return payload;
    }


    @Override
    public void decode(MessagePayload payload) {
        content = payload.searchableContent;
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

    protected PTextMessageContent(Parcel in) {
        super(in);
        this.content = in.readString();
    }

    public static final Creator<PTextMessageContent> CREATOR = new Creator<PTextMessageContent>() {
        @Override
        public PTextMessageContent createFromParcel(Parcel source) {
            return new PTextMessageContent(source);
        }

        @Override
        public PTextMessageContent[] newArray(int size) {
            return new PTextMessageContent[size];
        }
    };
}

