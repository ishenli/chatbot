package com.workdance.chatbot.ui.chat.conversation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatApi;
import com.workdance.chatbot.api.ChatClient;
import com.workdance.chatbot.api.dto.rep.ChatDetailRep;
import com.workdance.chatbot.api.dto.rep.MessageItemRep;
import com.workdance.chatbot.api.dto.req.ChatReq;
import com.workdance.chatbot.core.dto.OperateResult;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.ConversationInfo;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.ui.chat.conversation.message.core.MarkdownMessageContent;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ConversationViewModel extends ViewModel {
    private static final String TAG = "ConversationViewModel";
    @Setter
    @Getter
    private String conversationTitle;
    private MutableLiveData<Conversation> clearConversationMessageLiveData;
    @Getter
    private MutableLiveData<ConversationInfo> conversationInfoLiveData = new MutableLiveData<>();

    public MutableLiveData<Conversation> clearConversationMessageLiveData() {
        if (clearConversationMessageLiveData == null) {
            clearConversationMessageLiveData = new MutableLiveData<>();
        }
        return clearConversationMessageLiveData;
    }


    public MutableLiveData<List<MessageVO>> loadAroundMessages(Conversation conversation, String withUser) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
        ChatApi.getMessages(conversation).observeForever(messages -> {
            result.postValue(messages);
        });
        return result;
    }

    public MutableLiveData<List<MessageVO>> loadMessages(Conversation conversation) {
        return loadOldMessages(conversation);
    }

    public MutableLiveData<List<MessageVO>> loadOldMessages(Conversation conversation) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
        ChatClient.getMessages(conversation).observeForever(messages -> {
            if (messages != null) {
                List<MessageVO> list = new ArrayList<>();
                for (MessageItemRep item : messages) {

                    // 我的消息
                    if (item.getUserMessage() != null) {
                        Message message = new Message();
                        message.conversation = conversation;
                        message.setMessageId(item.getMessageId());
                        message.content = new TextMessageContent(item.getUserMessage());
                        message.direction = MessageDirection.Send;
                        message.sender = ChatApi.getDefaultUser().uid;
                        message.avatar = ChatApi.getDefaultUser().portrait;
                        MessageVO messageVO = new MessageVO(message);
                        list.add(messageVO);
                    }

                    // 助理消息
                    if (item.getAssistant() != null) {
                        Message message = new Message();
                        message.conversation = conversation;
                        message.setMessageId(item.getMessageId());
                        message.direction = MessageDirection.Receive;
                        message.content = new MarkdownMessageContent(item.getAssistant());
                        message.sender = item.getBrainId();
                        message.avatar = item.getBrainLogo();
                        MessageVO messageVO = new MessageVO(message);
                        list.add(messageVO);
                    }
                }
                result.setValue(list);
            }
        });
        return result;
    }

    public MutableLiveData<List<MessageVO>> loadNewMessages(Conversation conversation, String targetUser) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
        ChatApi.getMessages(conversation).observeForever(messages -> result.postValue(messages));
        return result;
    }

    public MutableLiveData<ConversationInfo> loadConversationInfo(Conversation conversation) {
        ChatClient.getChatDetail(conversation).observeForever(chatDetailRep -> {
            if (chatDetailRep != null) {
                ConversationInfo conversationInfo = new ConversationInfo();
                conversationInfo.setConversation(conversation);
                conversationInfo.setBrains(chatDetailRep.getBrains());
                conversationInfoLiveData.postValue(conversationInfo);
            }
        });
        return conversationInfoLiveData;
    }


    public LiveData<OperateResult<Boolean>> deleteConversation(Conversation conversation) {
        return ChatClient.deleteChat(conversation);
    }

    public LiveData<OperateResult<Boolean>> modifyConversation(ChatReq conversation) {
        return ChatClient.modifyChat(conversation);
    }

    public LiveData<ChatDetailRep> createConversation(ChatReq chatReq) {
        MutableLiveData<ChatDetailRep> result = new MutableLiveData<>();
        ChatClient.createChat(chatReq).observeForever(chatDetailRep -> {
            if (chatDetailRep != null) {
                result.setValue(chatDetailRep);
            }

        });

        return result;
    }

}
