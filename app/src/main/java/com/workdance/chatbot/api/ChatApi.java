package com.workdance.chatbot.api;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.workdance.chatbot.ui.chat.conversation.message.core.MessageContentType;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageStatus;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.ui.chat.conversation.MessageVO;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.repository.ConversationRepository;
import com.workdance.chatbot.repository.MessageRepository;
import com.workdance.chatbot.repository.UserRepository;
import com.workdance.chatbot.repository.dataobject.MessageDO;
import com.workdance.chatbot.repository.entity.MessageContentEntity;
import com.workdance.chatbot.repository.entity.MessageEntity;
import com.workdance.chatbot.repository.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 默认用户，先写死，便于后续调试
     * @return
     */
    public static UserInfo getDefaultUser() {
        UserInfo userInfo1 = new UserInfo();
        userInfo1.portrait = "https://himg.bdimg.com/sys/portrait/item/public.1.cd7083db.YCCdYhAb3c_HTkJ5oKEIuw.jpg";
        userInfo1.name = "haomo";
        userInfo1.uid = "105766";
        userInfo1.displayName="皓默";
        return userInfo1;
    }

    @SuppressLint("CheckResult")
    public static LiveData<List<MessageVO>> getMessages(Conversation conversation) {
        LiveData<List<MessageDO>> messageDOs = messageRepository.getMessages(conversation);
        return Transformations.map(messageDOs, allMessageDOs -> {
            List<MessageVO> messages = new ArrayList<>();
            for (MessageDO messageDO : allMessageDOs) {
                Message msg = new Message();
                switch (messageDO.getMessageContentType()) {
                    case MessageContentType.ContentType_Text:
                        msg.content = new TextMessageContent(messageDO.getContent());
                        break;
                    case MessageContentType.ContentType_Image:
                        break;
                    case MessageContentType.ContentType_Voice:
                        break;
                    case MessageContentType.ContentType_Video:
                        break;
                    case MessageContentType.ContentType_File:
                        break;
                    case MessageContentType.ContentType_Location:
                        break;
                    case MessageContentType.ContentType_Sticker:
                }
                msg.sender = messageDO.getSender();
                msg.serverTime = Long.parseLong(messageDO.getGmtCreate());
                msg.direction = MessageDirection.direction(messageDO.getDirection());
                msg.status = MessageStatus.status(messageDO.getStatus());
                msg.conversation = conversation;
                msg.messageUid = messageDO.getMessageId();
                MessageVO messageVO = new MessageVO(msg);
                messages.add(messageVO);
            }

            return  messages;
        });
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

    public static void sendMessage(Message msg, Object o) {
        MessageEntity message = new MessageEntity();
        MessageContentEntity messageContent = new MessageContentEntity();

        // 消息内容
        String messageContentId = UUID.randomUUID().toString();
        messageContent.setMessageContentId(messageContentId);
        String content;
        if (msg.content instanceof TextMessageContent) {
            content = ((TextMessageContent) msg.content).getContent();
            messageContent.setMessageContentType(MessageContentType.ContentType_Text);
        } else {
            messageContent.setMessageContentType(MessageContentType.ContentType_Text);
            content = "";
        }
        messageContent.setContent(content);
        // 消息
        message.setDirection(MessageDirection.Send.value());
        message.setStatus(MessageStatus.Sending.value());
        message.setSender(getUserId());
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageContentId(messageContentId);
        message.setConversationId(msg.conversation.getId());

        messageRepository.insertMessageWithContent(message, messageContent);
    }
}
