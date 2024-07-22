package com.workdance.chatbot.api.dto;

import lombok.Data;

@Data
public class BrainItemRep {
    private String brainId;
    private String name;
    private String description;
    private String model;
    private String brainType;
    private String userId;
    private String logo;
}
