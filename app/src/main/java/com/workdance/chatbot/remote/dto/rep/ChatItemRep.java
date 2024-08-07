package com.workdance.chatbot.remote.dto.rep;

import com.workdance.chatbot.model.Brain;

import java.util.List;

import lombok.Data;

@Data
public class ChatItemRep {
    private String chatId;
    private String chatName;
    private String userMessage;
    private String messageGmtCreate;
    private String chatGmtCreate;
    private String avatar;
    private List<Brain> brains;
}
