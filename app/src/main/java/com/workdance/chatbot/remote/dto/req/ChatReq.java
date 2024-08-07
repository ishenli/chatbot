package com.workdance.chatbot.remote.dto.req;

import lombok.Data;

@Data
public class ChatReq {
    private String workId;
    private String chatId;
    private String userId;
    private String question;
    private String brainId;
    private String chatName;
}
