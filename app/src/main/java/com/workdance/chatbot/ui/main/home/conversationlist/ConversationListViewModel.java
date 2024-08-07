package com.workdance.chatbot.ui.main.home.conversationlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.remote.ChatClient;

import java.util.List;

public class ConversationListViewModel extends ViewModel {
    private MutableLiveData<List<ConversationListItemVO>> conversationListLiveData = new MutableLiveData<>();

    public MutableLiveData<List<ConversationListItemVO>> loadConversationList(String userId) {
        if (conversationListLiveData == null) {
            conversationListLiveData = new MutableLiveData<>();
        }

        ChatClient.getConversationList(userId).observeForever(conversationList -> {
            if (conversationList != null) {
                conversationListLiveData.postValue(conversationList);
            }
        });

        return conversationListLiveData;
    }

    public MutableLiveData<List<ConversationListItemVO>> conversationListLiveData() {
        return conversationListLiveData;
    }
}

