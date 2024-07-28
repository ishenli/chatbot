package com.workdance.chatbot.model;

import java.util.List;

import lombok.Data;

@Data
public class ConversationInfo {
    private Conversation conversation;
    private Message lastMessage;
    private long timestamp;
    private String draft;
    private UnreadCount unreadCount;
    private int top;
    private boolean isSilent;
    private List<Brain> brains;

    public ConversationInfo() {
    }

    public Brain getDefaultBrain() {
        if (brains != null && !brains.isEmpty()) {
            return brains.get(0);
        }
        return new Brain();
    }
}
