package com.example.wechat.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wechat.repository.dao.UserDao;
import com.example.wechat.repository.entity.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class UserRepository extends BaseRepository {
    private UserDao userDao;

    public UserRepository(Context context) {
        super(context);
        userDao = mAppDatabase.userDao();
    }

    public LiveData<UserEntity> findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    public LiveData<List<UserEntity>> getAllUserInfo() {
        return userDao.getAllUsers();
    }

    public LiveData<UserEntity> findUserByUid(String uid) {
        return userDao.findUserByUid(uid);
    }

    public Completable insert(UserEntity user) {
        return userDao.insert(user);
    }

    public void update(UserEntity user) {
        executorService.execute(() -> {
            userDao.update(user);
        });
    }
    public LiveData<List<UserEntity>> getAllUsers() {
        return userDao.getAllUsers();
    }
}
