package com.workdance.chatbot.remote.dto.req;

import lombok.Data;

@Data
public class ChatHistoryReq {
    /**
     * 用户提问
     */
    private String userMessage;

    /**
     * 机器回答
     */
    private String assistant;

    /**
     * 聊天 ID
     */
    private String chatId;

    /**
     * 大脑 ID
     */
    private String brainId;


    /**
     * 消息 ID
     */
    private String messageId;
}
