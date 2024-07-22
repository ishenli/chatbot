package com.workdance.chatbot.ui.main.dashboard.assistantList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatClient;
import com.workdance.chatbot.api.dto.ChatReq;
import com.workdance.chatbot.model.Assistant;

import java.util.List;

public class AssistantListViewModel extends ViewModel {
    public final MutableLiveData<String> mName = new MutableLiveData<>();
    public final MutableLiveData<String> mMessage = new MutableLiveData<>();


    public LiveData<List<Assistant>> getAllAssistant (String userId) {
        ChatReq chatReq = new ChatReq();
        chatReq.setUserId(userId);
        return ChatClient.getAllAssistant(chatReq);
    }
}
