package com.workdance.chatbot.api.dto.rep;

import lombok.Data;

@Data
public class UploadRep {
    private String traceId;
    private String data;
    private String description;
    private String model;
    private String brainType;
    private String userId;
    private String logo;
}
