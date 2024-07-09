package com.example.wechat.repository.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.wechat.repository.entity.User;

@Dao
public interface UserDao {
    @Insert
    Long insert(User user);
}