package com.example.wechat.repository.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.wechat.repository.dao.UserDao;
import com.example.wechat.repository.entity.User;

@Database(version = 2, entities = {User.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "wechat.db")
                .addMigrations(new Migration(1, 2) {
                    @Override public void migrate(SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE user_table ADD COLUMN name TEXT");
                    }
                })
//                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                .build();
        return INSTANCE;
    }
}
