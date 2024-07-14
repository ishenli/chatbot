package com.example.wechat.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wechat.repository.dao.ConversationDao;
import com.example.wechat.repository.dao.MessageDao;
import com.example.wechat.repository.dao.UserDao;
import com.example.wechat.repository.entity.ConversationEntity;
import com.example.wechat.repository.entity.ConversationWithUserEntity;
import com.example.wechat.repository.entity.MessageContentEntity;
import com.example.wechat.repository.entity.MessageEntity;
import com.example.wechat.repository.entity.UserEntity;

@Database(version = 1, entities = {UserEntity.class, MessageEntity.class, MessageContentEntity.class, ConversationEntity.class, ConversationWithUserEntity.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract MessageDao messageDao();
    public abstract ConversationDao conversationDao();

    private static AppDatabase INSTANCE;
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "wechatV2.db")
                .build();
        return INSTANCE;
    }
}
