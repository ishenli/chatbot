package com.workdance.chatbot.model;

import lombok.Data;

@Data
public class Assistant {
    private String brainId;
    private String name;
    private String description;
    private String model;
    private String brainType;
    private String userId;
    private String logo;
}
