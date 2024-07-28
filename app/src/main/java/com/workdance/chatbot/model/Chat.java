package com.workdance.chatbot.model;

import java.util.List;

import lombok.Data;

@Data
public class Chat {
    private String chatId;
    private String chatName;
    private String avatar;
    private String gmtCreate;
    private List<Brain> brainId;
}
