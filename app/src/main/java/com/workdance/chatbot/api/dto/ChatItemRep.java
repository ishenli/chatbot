package com.workdance.chatbot.api.dto;

import lombok.Data;

@Data
public class ChatItemRep {
    private String chatId;
    private String chatName;
    private String userMessage;
    private String messageGmtCreate;
    private String chatGmtCreate;
    private String avatar;
}
