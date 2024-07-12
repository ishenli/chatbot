package com.example.wechat.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wechat.repository.dao.UserDao;
import com.example.wechat.repository.database.AppDatabase;
import com.example.wechat.repository.entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;

public class UserRepository {
    public final ExecutorService executorService;
    private AppDatabase mAppDatabase;
    private UserDao userDao;

    public UserRepository(Context context) {
        super();
        mAppDatabase = AppDatabase.getDatabase(context);
        executorService = Executors.newSingleThreadExecutor();
        userDao = mAppDatabase.userDao();
    }

    public LiveData<User> findUserByUid(String uid) {
        return userDao.findUserByUid(uid);
    }

    public Completable insert(User user) {
        return userDao.insert(user);
    }

    public void update(User user) {
        executorService.execute(() -> {
            userDao.update(user);
        });
    }
//
//    public Future<List<User>> getAllUsers() {
//        return executorService.submit(() -> userDao.getAllUsers());
//    }
    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }


    public void closeDataBase() {
        if (mAppDatabase != null) {
            if (mAppDatabase.isOpen()) {
                mAppDatabase.close();
                mAppDatabase = null;
            }
        }
    }
}
