package com.example.wechat.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import lombok.Data;

@Entity(tableName = "conversation_table")
@Data
public class ConversationEntity extends BaseEntity {

    @ColumnInfo(name = "conversation_id")
    private String conversationId;

    @ColumnInfo(name = "conversation_type")
    private String conversationType;

    @ColumnInfo(name = "target")
    private String target;

    public int line;

    @ColumnInfo(name = "last_message_id")
    public String lastMessageId;

}
