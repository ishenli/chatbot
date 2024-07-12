package com.example.wechat.api;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wechat.chat.conversation.message.Message;
import com.example.wechat.chat.conversation.message.core.MessageDirection;
import com.example.wechat.chat.conversation.message.core.MessageStatus;
import com.example.wechat.chat.conversation.message.core.TextMessageContent;
import com.example.wechat.model.Conversation;
import com.example.wechat.model.MessageVO;
import com.example.wechat.model.UserInfo;
import com.example.wechat.repository.UserRepository;
import com.example.wechat.repository.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatApi {
    private static UserRepository userRepository;

    public static void init(Application app) {
        init(app.getApplicationContext());
    }

    public static void init(Context context) {
        userRepository = new UserRepository(context);
    }

    public static List<MessageVO> getMessage() {

        List<MessageVO> messages = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            Conversation conversation = new Conversation(Conversation.ConversationType.Channel, "admin", 0);

            message.conversation = conversation;
            message.messageId = Long.parseLong("431167406061125762");
            message.serverTime = Long.parseLong("1720332640616");
            message.direction = MessageDirection.Receive;
            message.content = new TextMessageContent("hello");
            message.sender = "admin";
            message.status = MessageStatus.Unread;
            MessageVO messageVO = new MessageVO(message);
            messages.add(messageVO);
        }

        return messages;

    }

    public static LiveData<User> getUserInfo(String userId) {
        LiveData<User> user = userRepository.findUserByUid(userId);
        return user;
    }

    public static boolean insertUserInfo(UserInfo userInfo) {
        User user = new User();
        user.setNickname(userInfo.displayName);
        userRepository.insert(user);
        return true;
    }

    public static boolean updateUserInfo(UserInfo userInfo) {
        User user = new User();
        user.setNickname(userInfo.displayName);
        userRepository.update(user);
        return true;
    }

    public static Completable addUserInfo(UserInfo userInfo) {
        User user = new User();
        user.setUid(UUID.randomUUID().toString());
        user.setNickname(userInfo.displayName);
        user.setName(userInfo.name);
        user.setAvatar(userInfo.portrait);
        return userRepository.insert(user).subscribeOn(Schedulers.io());
    }

//    public static List<UserInfo> getUserList() {
//        Future<List<User>> users = userRepository.getAllUsers();
//        users.
//    }
}
