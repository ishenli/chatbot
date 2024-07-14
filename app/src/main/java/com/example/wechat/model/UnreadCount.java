package com.example.wechat.model;

import lombok.Data;

@Data
public class UnreadCount {

    public UnreadCount() {
    }

    /**
     * 单聊未读数
     */
    public int unread;
    /**
     * 群聊@数
     */
    public int unreadMention;
    /**
     * 群聊@All数
     */
    public int unreadMentionAll;
}
