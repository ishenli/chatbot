package com.workdance.chatbot.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.workdance.chatbot.repository.dataobject.MessageDO;
import com.workdance.chatbot.repository.entity.MessageContentEntity;
import com.workdance.chatbot.repository.entity.MessageEntity;

import java.util.List;

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
    Completable update(MessageEntity msg);

    @Query("SELECT * FROM message_table WHERE message_id=:messageId")
    LiveData<MessageEntity> queryMessagesByUid(String messageId);


    @Query("SELECT conversation_id, direction, sender, status, message_content_type,message_content_table.message_content_id as message_content_id, content, message_table.gmt_create as gmt_create FROM message_table INNER JOIN message_content_table ON message_content_table.id = message_table.id  WHERE message_table.conversation_id=:conversationId")
    LiveData<List<MessageDO>> getMessageByConversation(String conversationId);
}