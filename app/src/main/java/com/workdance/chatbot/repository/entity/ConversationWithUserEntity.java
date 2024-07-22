package com.workdance.chatbot.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.Data;

@Entity(tableName = "conversation_with_user_table")
@Data
public class ConversationWithUserEntity extends BaseEntity {
    @ColumnInfo(name = "conversation_id")
    private String conversationId;

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "conversation_remark")
    private String conversationRemark;
}
