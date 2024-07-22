package com.workdance.chatbot.ui.chat.conversation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatApi;
import com.workdance.chatbot.api.ChatClient;
import com.workdance.chatbot.core.util.AppScopeViewModel;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.ConversationInfo;

import java.util.List;

public class ConversationViewModel extends ViewModel implements AppScopeViewModel {
    private MutableLiveData<Conversation> clearConversationMessageLiveData;

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

    public MutableLiveData<List<MessageVO>> getMessages(Conversation conversation) {
        return loadOldMessages(conversation);
    }

    public MutableLiveData<List<MessageVO>> loadOldMessages(Conversation conversation) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
//        ChatApi.getMessages(conversation).observeForever(messages -> result.postValue(messages));
        ChatClient.getMessages(conversation).observeForever(messages -> result.postValue(messages));
        return result;
    }

    public MutableLiveData<List<MessageVO>> loadNewMessages(Conversation conversation, String targetUser) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
        ChatApi.getMessages(conversation).observeForever(messages -> result.postValue(messages));
        return result;
    }

    public MutableLiveData<ConversationInfo> getConversationInfo(Conversation conversation) {
        MutableLiveData<ConversationInfo> result = new MutableLiveData<>();
        ChatClient.getConversation(conversation).observeForever(conversationInfo -> {
            if (conversationInfo != null) {
                result.postValue(conversationInfo);
            }
        });
        return result;
    }
}
