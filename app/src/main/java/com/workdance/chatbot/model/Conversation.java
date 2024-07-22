package com.workdance.chatbot.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class Conversation implements Parcelable {

    protected Conversation(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : ConversationType.values()[tmpType];
        this.target = in.readString();
        this.line = in.readInt();
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.target);
        dest.writeInt(this.line);
    }

    public enum ConversationType {
        // 单聊
        Single(0),
        // 群聊
        Group(1),
        // 聊天室
        ChatRoom(2),
        //频道
        Channel(3),
        //设备
        Things(4),
        //密聊
        SecretChat(5);


        private int value;

        ConversationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ConversationType type(int type) {
            ConversationType conversationType = null;
            switch (type) {
                case 0:
                    conversationType = Single;
                    break;
                case 1:
                    conversationType = Group;
                    break;
                case 2:
                    conversationType = ChatRoom;
                    break;
                case 3:
                    conversationType = Channel;
                    break;
                case 4:
                    conversationType = Things;
                    break;
                case 5:
                    conversationType = SecretChat;
                    break;
                default:
                    throw new IllegalArgumentException("type " + type + " is invalid");
            }
            return conversationType;
        }
    }

    public String id;

    public ConversationType type;

    /**
     * 会话的对方
     */
    public String target;

    public int line;

    public Conversation() {
    }

    public Conversation(ConversationType type, String target, int line) {
        this.type = type;
        this.target = target;
        this.line = line;
    }

    public Conversation(ConversationType type, String target) {
        this.type = type;
        this.target = target;
        this.line = 0;
    }


}
