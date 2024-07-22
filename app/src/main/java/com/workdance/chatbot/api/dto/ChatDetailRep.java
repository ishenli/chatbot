package com.workdance.chatbot.api.dto;

import lombok.Data;

@Data
public class ChatDetailRep {
    private String chatId;
    private String chatName;
    private String avatar;
    private String gmtCreate;
}
