package com.example.wechat.model;

import lombok.Data;

@Data
public class ConversationInfo {
    public Conversation conversation;
    public Message lastMessage;
    public long timestamp;
    public String draft;
    public UnreadCount unreadCount;
    public int top;
    public boolean isSilent;

    public ConversationInfo() {
    }
}
