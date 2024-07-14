package com.example.wechat.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wechat.repository.entity.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface UserDao {
    @Insert
    public Completable insert(UserEntity user);

    @Update
    public Completable update(UserEntity user);

    @Query("SELECT * FROM user_table")
    LiveData<List<UserEntity>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE uid=:uid")
    LiveData<UserEntity> findUserByUid(String uid);


    @Query("SELECT * FROM user_table WHERE name=:name")
    LiveData<UserEntity>  findUserByName(String name);
}