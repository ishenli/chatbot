package com.workdance.chatbot.ui.chat.conversationlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatClient;

import java.util.List;

public class ConversationListViewModel extends ViewModel {
    private MutableLiveData<List<ConversationListItemVO>> conversationListLiveData;

    public MutableLiveData<List<ConversationListItemVO>> conversationListLiveData() {
        if (conversationListLiveData == null) {
            conversationListLiveData = new MutableLiveData<>();
        }

        ChatClient.getConversationList().observeForever(conversationList -> {
            if (conversationList != null) {
                conversationListLiveData.postValue(conversationList);
            }
        });

        return conversationListLiveData;
    }
}

