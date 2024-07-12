package com.example.wechat.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wechat.repository.entity.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface UserDao {
    @Insert
    public Completable insert(User user);

    @Update
    public Completable update(User user);

    @Query("SELECT * FROM user_table")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE uid=:uid")
    LiveData<User> findUserByUid(String uid);
}