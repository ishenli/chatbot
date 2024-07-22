package com.workdance.chatbot.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
public class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gmt_create", defaultValue = "CURRENT_TIMESTAMP")
    public Long gmtCreate;

    @ColumnInfo(name = "gmt_modified", defaultValue = "CURRENT_TIMESTAMP")
    public Long gmtModified;

    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
}
