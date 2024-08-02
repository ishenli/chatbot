package com.workdance.chatbot.model;

import lombok.Data;

@Data
public class Brain {
    private String name;
    private String brainId;
    private String description;
    private String model;
    private String brainType;
    private String userId;
    private String logo;

    public enum BrainTypeEnum {
        BASIC("basic"),
        DOC("doc"),
        API("api");

        private final String value;

        BrainTypeEnum(String basic) {
            this.value = basic;
        }

        public String getValue() {
            return value;
        }
    }
}
