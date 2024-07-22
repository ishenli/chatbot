package com.workdance.chatbot.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.repository.dao.MessageDao;
import com.workdance.chatbot.repository.dataobject.MessageDO;
import com.workdance.chatbot.repository.entity.MessageContentEntity;
import com.workdance.chatbot.repository.entity.MessageEntity;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Completable;

public class MessageRepository extends BaseRepository {
    private MessageDao messageDao;

    public MessageRepository(Context context) {
        super(context);
        messageDao = mAppDatabase.messageDao();
    }

    public LiveData<MessageEntity> findMessageByUid(String uid) {
        return messageDao.queryMessagesByUid(uid);
    }

    public Completable insert(MessageEntity msg) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                messageDao.insertMessage(msg);
                return null;
            }
        });
    }

//    public Completable insertMessageWithContent(MessageEntity message, MessageContentEntity messageContent) {
//        return Completable.fromCallable(new Callable<Void>() {
//            @Override
//            public Void call() {
//                messageDao.insertMessageWithContent(message, messageContent);
//                return null;
//            }
//        });
//    }

    public void insertMessageWithContent(MessageEntity message, MessageContentEntity messageContent) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.insertMessageWithContent(message, messageContent);
            }
        });
    }

    public LiveData<List<MessageDO>> getMessages(Conversation conversation) {
        return messageDao.getMessageByConversation(conversation.getId());
    }
}
