package com.workdance.chatbot.repository.dataobject;

import androidx.room.ColumnInfo;

import lombok.Data;

@Data
public class MessageDO {
    @ColumnInfo(name = "message_id")
    private String messageId;
    @ColumnInfo(name = "message_content_id")
    private String messageContentId;
    private int direction;
    private String content;
    private String sender;
    private int status;
    @ColumnInfo(name = "message_content_type")
    private int messageContentType;

    @ColumnInfo(name = "gmt_create")
    private String gmtCreate;
}
