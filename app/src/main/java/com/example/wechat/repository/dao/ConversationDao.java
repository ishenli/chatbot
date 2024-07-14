package com.example.wechat.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wechat.repository.entity.ConversationEntity;
import com.example.wechat.repository.entity.MessageEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface ConversationDao {
    @Insert
    void insert(ConversationEntity conversation);

    @Update
    public Completable update(MessageEntity msg);

    @Query("SELECT * FROM conversation_with_user_table WHERE user_id=:userId")
    LiveData<MessageEntity> QueryConversationListByUserId(String userId);

    @Query(
            "SELECT conversation_table.* FROM conversation_table JOIN conversation_with_user_table ON conversation_table.conversation_id = conversation_with_user_table.conversation_id JOIN user_table ON conversation_with_user_table.user_id = user_table.uid WHERE user_table.uid=:userId")
    LiveData<List<ConversationEntity>> QueryConversationByUid(String userId);

}