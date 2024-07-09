/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.example.wechat.chat.conversation.message.core;

import static com.example.wechat.chat.conversation.message.core.MessageContentType.ContentType_Text;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.wechat.chat.conversation.message.Message;
import com.example.wechat.chat.conversation.message.MessageContent;
import com.example.wechat.model.QuoteInfo;

import org.json.JSONException;
import org.json.JSONObject;


@ContentTag(type = ContentType_Text, flag = PersistFlag.Persist_And_Count)
public class TextMessageContent extends MessageContent {
    private String content;
    // 引用信息
    private QuoteInfo quoteInfo;

    public TextMessageContent() {
    }

    public TextMessageContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuoteInfo getQuoteInfo() {
        return quoteInfo;
    }

    public void setQuoteInfo(QuoteInfo quoteInfo) {
        this.quoteInfo = quoteInfo;
    }

    @Override
    public MessagePayload encode() {
        MessagePayload payload = super.encode();
        payload.searchableContent = content;
        payload.mentionedType = mentionedType;
        payload.mentionedTargets = mentionedTargets;
        if (quoteInfo != null) {
            JSONObject object = new JSONObject();
            try {
                object.put("quote", quoteInfo.encode());
                payload.binaryContent = object.toString().getBytes();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return payload;
    }


    @Override
    public void decode(MessagePayload payload) {
        content = payload.searchableContent;
        mentionedType = payload.mentionedType;
        mentionedTargets = payload.mentionedTargets;
        if (payload.binaryContent != null && payload.binaryContent.length > 0) {
            try {
                JSONObject object = new JSONObject(new String(payload.binaryContent));
                quoteInfo = new QuoteInfo();
                quoteInfo.decode(object.optJSONObject("quote"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        dest.writeParcelable(this.quoteInfo, flags);
    }

    protected TextMessageContent(Parcel in) {
        super(in);
        this.content = in.readString();
        this.quoteInfo = in.readParcelable(QuoteInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<TextMessageContent> CREATOR = new Parcelable.Creator<TextMessageContent>() {
        @Override
        public TextMessageContent createFromParcel(Parcel source) {
            return new TextMessageContent(source);
        }

        @Override
        public TextMessageContent[] newArray(int size) {
            return new TextMessageContent[size];
        }
    };
}
