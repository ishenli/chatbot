package com.workdance.chatbot.ui.chat.conversation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatAIClient;
import com.workdance.chatbot.api.ChatApi;
import com.workdance.chatbot.api.ChatClient;
import com.workdance.chatbot.api.dto.StreamEventEnum;
import com.workdance.chatbot.api.dto.req.ChatReq;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.ui.chat.conversation.message.core.MarkdownMessageContent;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private static final String TAG = "MessageViewModel";

    public LiveData<MessageVO> sendTextMsg(Conversation conversation, List<String> toUsers, String txtContent) {
        TextMessageContent textMessageContent = new TextMessageContent(txtContent);
        Message message = new Message();
        message.conversation = conversation;
        message.content = textMessageContent;
        if (toUsers != null) {
            message.toUsers = toUsers.toArray(new String[0]);
        }
        return sendMessage(message);
    }

    public void setImageMsg(Conversation conversation, List<String> toUsers, String txtContent) {

    }

    private LiveData<MessageVO> sendMessage(Message message) {
        message.sender = ChatApi.getUserId();
        return ChatClient.sendChatMessage(message);
    }


    public LiveData<MessageVO> receiveMessage(String question, String avatar, Conversation conversation, MessageVO askMessageVo) {
        MutableLiveData<MessageVO> result = new MutableLiveData<>();
        String askMessageId = askMessageVo.getMessage().getMessageId();

        ChatReq chatReq = new ChatReq();
        chatReq.setQuestion(question);
        chatReq.setChatId(conversation.getId());
        chatReq.setBrainId(conversation.getTarget());
        Message message = new Message();
        message.setConversation(conversation);
        message.setDirection(MessageDirection.Receive);
        message.setSender(conversation.getTarget());
        message.setAvatar(avatar);
        message.setMessageId(askMessageId);
        MessageVO messageVO = new MessageVO();


        ChatAIClient.askOllama(chatReq).observeForever(se -> {
            if (se != null) {
                Log.d(TAG, "receiveMessage: " + se.getData());
                String content = "";
                if (se.getStatus() == StreamEventEnum.Doing) {
                    content = se.getData() + "\n\n  ..."; // 内容结尾展示加个...
                } else if (se.getStatus() == StreamEventEnum.Done) {
                    content = se.getData();
                }
                MarkdownMessageContent textMessageContent = new MarkdownMessageContent(content);
                message.setContent(textMessageContent);
                messageVO.setMessage(message);
                result.setValue(messageVO);

                if (se.getStatus() == StreamEventEnum.Done) {
                    // 更新数据库
                    Log.d(TAG, "receiveMessage Done: " + askMessageId);
                    ChatClient.modifyChatMessage(askMessageId, se.getData()).observeForever(chatHistoryRep -> {
                        if (chatHistoryRep != null) {
                            Log.d(TAG, "receiveMessage update done: " + se.getData());
                        }
                    });
                }
            }
        });

        // ChatAIClient.askOllama(chatReq).observe(fragment, se -> {
        //     if (se != null) {
        //         Log.d(TAG, "receiveMessage: " + se);
        //     }
        // });


        return result;
    }
}