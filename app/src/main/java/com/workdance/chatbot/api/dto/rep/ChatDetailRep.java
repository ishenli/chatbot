package com.workdance.chatbot.api.dto.rep;

import com.workdance.chatbot.model.Brain;

import java.util.List;

import lombok.Data;

@Data
public class ChatDetailRep {
    private String chatId;
    private String chatName;
    private String avatar;
    private String gmtCreate;
    private String brainId;
    private List<Brain> brains;
}
