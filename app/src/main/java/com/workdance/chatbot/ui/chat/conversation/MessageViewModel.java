package com.workdance.chatbot.ui.chat.conversation;

import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatApi;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.Message;

import java.util.List;

public class MessageViewModel extends ViewModel {
    public void sendTextMsg(Conversation conversation, List<String> toUsers, TextMessageContent txtContent) {
        Message message = new Message();
        message.conversation = conversation;
        message.content = txtContent;
        if (toUsers != null) {
            message.toUsers = toUsers.toArray(new String[0]);
        }
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        message.sender = ChatApi.getUserId();
        ChatApi.sendMessage(message, null);
    }
}
