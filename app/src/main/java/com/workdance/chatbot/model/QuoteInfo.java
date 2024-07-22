/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class QuoteInfo implements Parcelable {
    private long messageUid;
    // 本地使用
    private Message message;
    private String userId;
    private String userDisplayName;
    private String messageDigest;

    public long getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(long messageUid) {
        this.messageUid = messageUid;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getMessageDigest() {
        return messageDigest;
    }

    public void setMessageDigest(String messageDigest) {
        this.messageDigest = messageDigest;
    }

    public static QuoteInfo initWithMessage(Message message) {
        QuoteInfo info = new QuoteInfo();
        info.message = message;
        return info;

    }


    public JSONObject encode() {
        JSONObject object = new JSONObject();
        try {
            object.put("u", messageUid);
            object.put("i", userId);
            object.put("n", userDisplayName);
            object.put("d", messageDigest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void decode(JSONObject object) {
        messageUid = object.optLong("u");
        userId = object.optString("i");
        userDisplayName = object.optString("n");
        messageDigest = object.optString("d");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.messageUid);
        dest.writeString(this.userId);
        dest.writeString(this.userDisplayName);
        dest.writeString(this.messageDigest);
    }

    public QuoteInfo() {
    }

    protected QuoteInfo(Parcel in) {
        this.messageUid = in.readLong();
        this.userId = in.readString();
        this.userDisplayName = in.readString();
        this.messageDigest = in.readString();
    }

    public static final Creator<QuoteInfo> CREATOR = new Creator<QuoteInfo>() {
        @Override
        public QuoteInfo createFromParcel(Parcel source) {
            return new QuoteInfo(source);
        }

        @Override
        public QuoteInfo[] newArray(int size) {
            return new QuoteInfo[size];
        }
    };
}
