package com.workdance.chatbot.repository;

import android.content.Context;
import android.util.Log;

import androidx.room.Insert;

import com.workdance.chatbot.repository.dao.ConversationDao;
import com.workdance.chatbot.repository.entity.ConversationEntity;

import java.util.UUID;
import java.util.concurrent.Future;

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

//    public LiveData<ConversationEntity> getConversationV2(int value, String target, int line) {
//
//        ConversationEntity conversationEntityLiveData = conversationDao.queryConversationByTargetIdAndType(target, value);
//        if (conversationEntityLiveData == null) {
//            ConversationEntity conversationEntity = new ConversationEntity();
//            conversationEntity.setConversationId(UUID.randomUUID().toString());
//            conversationEntity.setTarget(target);
//            conversationEntity.setConversationType(Conversation.ConversationType.Single.getValue());
//            this.insert(conversationEntity);
//            return this.getConversation(value, target, line);
//        }
//        return conversationEntityLiveData;
//    }

    public ConversationEntity getConversation(int value, String target, int line) {
        Future<ConversationEntity> future = executorService.submit(() -> {
            ConversationEntity conversationEntity = conversationDao.queryConversationByTargetIdAndType(target, value);
            if (conversationEntity == null) {
                conversationEntity = new ConversationEntity();
                String conversationId = UUID.randomUUID().toString();
                conversationEntity.setConversationId(conversationId);
                conversationEntity.setTarget(target);
                conversationEntity.setConversationType(value);
                conversationEntity.setLine(line);
                conversationDao.insert(conversationEntity);
                return conversationDao.queryConversationEntityById(conversationId);
            }

            return conversationEntity;
        });

        try {
            return future.get();
        } catch (Exception e) {
            Log.e("UncaughtException", "Uncaught exception in ConversationRepository " + e);
            return null;
        }
    }
}
