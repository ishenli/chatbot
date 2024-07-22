package com.workdance.chatbot.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity(tableName = "message_content_table")
@Data
public class MessageContentEntity extends BaseEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "message_content_id")
    private String messageContentId;

    @ColumnInfo(name = "message_content_type")
    private int messageContentType;

    private String content;

    @ColumnInfo(name = "quote_info_id")
    public String QuoteInfoId;
}
