package com.workdance.chatbot.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "message_table")
@Setter
@Getter
public class MessageEntity extends BaseEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "message_id")
    private String messageId;

    @ColumnInfo(name = "message_content_id")
    private String messageContentId;

    public String sender;

    public int direction;

    public int status;

    @ColumnInfo(name = "local_extra")
    public String localExtra;

    @ColumnInfo(name = "conversation_id")
    public String conversationId;

    @ColumnInfo(name = "to_users")
    public String toUsers;

}
