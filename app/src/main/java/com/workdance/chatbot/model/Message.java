package com.workdance.chatbot.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageStatus;

import lombok.Data;

@Data
public class Message implements Parcelable {

    public String messageId;
    public Conversation conversation;
    public String sender;
    public long serverTime;
    public String[] toUsers;
    public MessageContent content;
    public MessageDirection direction;
    public MessageStatus status;
    public String messageUid;
    public String localExtra;

    public Message() {}

    protected Message(Parcel in) {
        this.messageId = in.readString();
        this.conversation = in.readParcelable(Conversation.class.getClassLoader());
        this.sender = in.readString();
        this.toUsers = in.createStringArray();
        this.content = in.readParcelable(MessageContent.class.getClassLoader());
        int tmpDirection = in.readInt();
        this.direction = tmpDirection == -1 ? null : MessageDirection.values()[tmpDirection];
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : MessageStatus.values()[tmpStatus];
        this.messageUid = in.readString();
        this.serverTime = in.readLong();
        this.localExtra = in.readString();
    }
    public Message(Message msg){
        this.messageId = msg.messageId;
        this.conversation = msg.conversation;
        this.sender = msg.sender;
        this.toUsers = msg.toUsers;
        this.content = msg.content;
        this.direction = msg.direction;
        this.status = msg.status;
        this.messageUid = msg.messageUid;
        this.serverTime = msg.serverTime;
        this.localExtra = msg.localExtra;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
    }
}
