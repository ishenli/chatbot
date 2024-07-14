package com.example.wechat.repository;

import android.content.Context;

import com.example.wechat.repository.database.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseRepository {
    public AppDatabase mAppDatabase;
    public ExecutorService executorService;

    public BaseRepository(Context context) {
        super();
        mAppDatabase = AppDatabase.getDatabase(context);
        executorService = Executors.newSingleThreadExecutor();
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
