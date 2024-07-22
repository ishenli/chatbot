package com.workdance.chatbot.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.workdance.chatbot.repository.dao.ConversationDao;
import com.workdance.chatbot.repository.dao.MessageDao;
import com.workdance.chatbot.repository.dao.UserDao;
import com.workdance.chatbot.repository.entity.ConversationEntity;
import com.workdance.chatbot.repository.entity.ConversationWithUserEntity;
import com.workdance.chatbot.repository.entity.MessageContentEntity;
import com.workdance.chatbot.repository.entity.MessageEntity;
import com.workdance.chatbot.repository.entity.UserEntity;

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
                "chatbot.db")
                .build();
        return INSTANCE;
    }
}
