package com.workdance.chatbot.remote.dto.rep;

import lombok.Data;

@Data
public class ChatHistoryRep {

    private String messageId;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 修改时间
     */
    private String gmtModified;

    /**
     * 删除时间
     */
    private String deletedAt;

    /**
     * 用户提问
     */
    private String userMessage;

    /**
     * 机器回答
     */
    private String assistant;

    /**
     * 聊天 ID
     */
    private String chatId;

    /**
     * 大脑 ID
     */
    private String brainId;

}
