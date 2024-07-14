package com.example.wechat.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.wechat.repository.entity.MessageContentEntity;
import com.example.wechat.repository.entity.MessageEntity;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MessageDao {
    @Insert
    void insertMessage(MessageEntity msg);

    @Insert
    void insertMessageContent(MessageContentEntity messageContent);



    @Transaction
    default void insertMessageWithContent(MessageEntity message, MessageContentEntity messageContent) {
        insertMessageContent(messageContent);
        insertMessage(message);
    }

    @Update
    public Completable update(MessageEntity msg);

    @Query("SELECT * FROM user_table WHERE uid=:conversationId")
    LiveData<MessageEntity> QueryMessagesByUid(String conversationId);


}