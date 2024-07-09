package com.example.wechat.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wechat.repository.dao.UserDao;
import com.example.wechat.repository.entity.User;

@Database(version = 1, entities = {User.class})
abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao(User user);
    private static AppDatabase INSTANCE;
    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "wechat.db").build();
        return INSTANCE;
    }
}
