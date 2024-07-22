package com.workdance.chatbot.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.workdance.chatbot.repository.entity.ConversationEntity;
import com.workdance.chatbot.repository.entity.MessageEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface ConversationDao {
    @Insert
    void insert(ConversationEntity conversation);

    @Update
    public Completable update(MessageEntity msg);

//    @Query("SELECT * FROM conversation_with_user_table WHERE user_id=:userId")
//    LiveData<MessageEntity> QueryConversationListByUserId(String userId);

    @Query(
            "SELECT conversation_table.* FROM conversation_table JOIN conversation_with_user_table ON conversation_table.conversation_id = conversation_with_user_table.conversation_id JOIN user_table ON conversation_with_user_table.user_id = user_table.uid WHERE user_table.uid=:userId")
    LiveData<List<ConversationEntity>> queryConversationByUserId(String userId);

    @Query("SELECT * FROM conversation_table WHERE conversation_id=:conversationId")
    LiveData<ConversationEntity> queryConversationById(String conversationId);

    @Query("SELECT * FROM conversation_table WHERE conversation_id=:conversationId")
    ConversationEntity queryConversationEntityById(String conversationId);

    @Query("SELECT * FROM conversation_table WHERE target=:targetId AND conversation_type=:conversationType")
    ConversationEntity queryConversationByTargetIdAndType(String targetId, int conversationType);
}