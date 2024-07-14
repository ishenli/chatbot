package com.example.wechat.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
public class BaseEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gmt_create", defaultValue = "CURRENT_TIMESTAMP")
    public String gmtCreate;

    @ColumnInfo(name = "gmt_modified", defaultValue = "CURRENT_TIMESTAMP")
    public String gmtModified;

    @ColumnInfo(name = "deleted_at")
    public String deletedAt;
}
