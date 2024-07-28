package com.workdance.chatbot.api.dto.rep;

import lombok.Data;

@Data
public class MessageItemRep {
    private String chatId;
    private String messageId;
    private String chatName;
    private String userMessage;
    private String assistant;
    private String brainName;
    private String gmtCreate;
    private String brainId;
    private String brainLogo;
}
