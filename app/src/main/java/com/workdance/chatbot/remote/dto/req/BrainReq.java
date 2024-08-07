package com.workdance.chatbot.remote.dto.req;

import lombok.Data;

@Data
public class BrainReq {
    /**
     * 大脑名称
     */
    private String name;

    /**
     * 大脑状态
     */
    private String status;

    private String description;

    private String model;

    private Long maxTokens;

    private Double temperature;

    /**
     * 提升模板
     */
    private String promptId;

    /**
     * 大脑类型
     */
    private String brainType;

    private String userId;

    private String logo;
}
