package com.example.wechat.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wechat.repository.dao.MessageDao;
import com.example.wechat.repository.database.AppDatabase;
import com.example.wechat.repository.entity.MessageContentEntity;
import com.example.wechat.repository.entity.MessageEntity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;

public class MessageRepository {
    public final ExecutorService executorService;
    private AppDatabase mAppDatabase;
    private MessageDao messageDao;

    public MessageRepository(Context context) {
        super();
        mAppDatabase = AppDatabase.getDatabase(context);
        executorService = Executors.newSingleThreadExecutor();
        messageDao = mAppDatabase.messageDao();
    }

    public LiveData<MessageEntity> findMessageByUid(String uid) {
        return messageDao.QueryMessagesByUid(uid);
    }

    public Completable insert(MessageEntity msg) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                messageDao.insertMessage(msg);
                return null;
            }
        });
    }

    public Completable insertMessageWithContent(MessageEntity message, MessageContentEntity messageContent) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                messageDao.insertMessageWithContent(message, messageContent);
                return null;
            }
        });
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
