package com.example.wechat.api;

import com.example.wechat.chat.conversation.message.Message;
import com.example.wechat.chat.conversation.message.core.MessageDirection;
import com.example.wechat.chat.conversation.message.core.MessageStatus;
import com.example.wechat.chat.conversation.message.core.TextMessageContent;
import com.example.wechat.model.Conversation;
import com.example.wechat.model.MessageVO;
import com.example.wechat.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class chatApi {
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

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.displayName = "皓默";
        userInfo.uid = "2088202565878663";
        userInfo.name = "ishenli";
        userInfo.portrait = "https://himg.bdimg.com/sys/portrait/item/public.1.cd7083db.YCCdYhAb3c_HTkJ5oKEIuw.jpg";
        return userInfo;
    }
}
