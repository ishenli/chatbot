package com.workdance.chatbot.api.dto;

import lombok.Data;

@Data
public class ChatReq {
    private String workId;
    private String chatId;
    private String userId;
}
