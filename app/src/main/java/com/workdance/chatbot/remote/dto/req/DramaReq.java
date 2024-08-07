package com.workdance.chatbot.remote.dto.req;

import lombok.Data;

@Data
public class DramaReq {
    private String authorId;
    private int offset;
    private int pageSize;
    private String userID;

    public DramaReq() {
        this.authorId = "mini-drama-video";
        this.userID = "mini-drama-video";
    }
}
