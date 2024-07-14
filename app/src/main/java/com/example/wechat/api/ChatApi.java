package com.example.wechat.api;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wechat.chat.conversation.message.core.MessageDirection;
import com.example.wechat.chat.conversation.message.core.MessageStatus;
import com.example.wechat.chat.conversation.message.core.TextMessageContent;
import com.example.wechat.model.Conversation;
import com.example.wechat.model.Message;
import com.example.wechat.model.MessageVO;
import com.example.wechat.model.UserInfo;
import com.example.wechat.repository.ConversationRepository;
import com.example.wechat.repository.MessageRepository;
import com.example.wechat.repository.UserRepository;
import com.example.wechat.repository.entity.MessageContentEntity;
import com.example.wechat.repository.entity.MessageEntity;
import com.example.wechat.repository.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Setter;

public class ChatApi {
    private static final String TAG = "ChatApi";
    private static UserRepository userRepository;
    private static MessageRepository messageRepository;
    private static ConversationRepository conversationRepository;

    @Setter
    private static UserInfo currentUser;

    public static void init(Application app) {
        init(app.getApplicationContext());
    }

    public static void init(Context context) {
        userRepository = new UserRepository(context);
        messageRepository = new MessageRepository(context);
        conversationRepository = new ConversationRepository(context);
    }

    public static String getUserId() {
        return currentUser.uid;
    }


//    public static Completable getConversationList(UserInfo userInfo) {
//
//    }

    public static List<MessageVO> getMessage() {
        List<MessageVO> messages = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 30; i++) {
            Message message = new Message();
            Conversation conversation = new Conversation(Conversation.ConversationType.Channel, "admin", 0);

            message.conversation = conversation;
            message.messageId = Long.parseLong("431167406061125762");
            message.serverTime = Long.parseLong("1720332640616");
            if (i % 4 == 0) {
                message.direction = MessageDirection.Send;
            } else {
                message.direction = MessageDirection.Receive;
            }
            message.content = new TextMessageContent("hello");
            message.sender = "admin";
            message.status = MessageStatus.Unread;
            MessageVO messageVO = new MessageVO(message);
            messages.add(messageVO);
        }
        return messages;
    }

    public static LiveData<List<UserEntity>> getAllUserInfo() {
        LiveData<List<UserEntity>> users = userRepository.getAllUserInfo();
        return users;
    }

    public static LiveData<UserEntity> getUserInfoByName(String name) {
        LiveData<UserEntity> user = userRepository.findUserByName(name);
        return user;
    }

    public static LiveData<UserEntity> getUserInfo(String userId) {
        LiveData<UserEntity> user = userRepository.findUserByUid(userId);
        return user;
    }

    public static boolean insertUserInfo(UserInfo userInfo) {
        UserEntity user = new UserEntity();
        user.setNickname(userInfo.displayName);
        userRepository.insert(user);
        return true;
    }

    public static boolean updateUserInfo(UserInfo userInfo) {
        UserEntity user = new UserEntity();
        user.setNickname(userInfo.displayName);
        userRepository.update(user);
        return true;
    }

    public static Completable addUserInfo(UserInfo userInfo) {
        UserEntity user = new UserEntity();
        user.setUid(UUID.randomUUID().toString());
        user.setNickname(userInfo.displayName);
        user.setName(userInfo.name);
        user.setAvatar(userInfo.portrait);
        return userRepository.insert(user).subscribeOn(Schedulers.io());
    }

    public static Completable sendMessage(Message msg, Object o) {
        MessageEntity message = new MessageEntity();
        MessageContentEntity messageContent = new MessageContentEntity();

        // 消息内容
        String messageContentId = UUID.randomUUID().toString();
        messageContent.setMessageContentId(messageContentId);
        String content;
        if (msg.content instanceof TextMessageContent) {
            content = ((TextMessageContent) msg.content).getContent();
        } else {
            content = "";
        }
        messageContent.setContent(content);

        // 消息
        message.setDirection(String.valueOf(MessageDirection.Send));
        message.setStatus(String.valueOf(MessageStatus.Sending));
        message.setSender(getUserId());
        message.setMessageUid(UUID.randomUUID().toString());
        message.setMessageContentId(messageContentId);

        return messageRepository.insertMessageWithContent(message, messageContent).subscribeOn(Schedulers.io());
    }

}
