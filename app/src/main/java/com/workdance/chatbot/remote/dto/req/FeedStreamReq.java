package com.workdance.chatbot.remote.dto.req;

import lombok.Data;

@Data
public class FeedStreamReq {
    private String authorId;
    private int offset;
    private int pageSize;
    private String userID;
    private int codec;
    private int format;
}
