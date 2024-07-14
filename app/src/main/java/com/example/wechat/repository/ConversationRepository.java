package com.example.wechat.repository;

import android.content.Context;

import androidx.room.Insert;

import com.example.wechat.repository.dao.ConversationDao;
import com.example.wechat.repository.entity.ConversationEntity;

public class ConversationRepository extends BaseRepository {
    private final ConversationDao conversationDao;

    public ConversationRepository(Context context) {
        super(context);
        conversationDao = mAppDatabase.conversationDao();
    }

    @Insert
    public void insert(ConversationEntity conversation) {
        executorService.execute(() -> {
            conversationDao.insert(conversation);
        });
    }
}
