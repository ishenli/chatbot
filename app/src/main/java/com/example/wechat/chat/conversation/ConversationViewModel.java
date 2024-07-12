package com.example.wechat.chat.conversation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wechat.api.ChatApi;
import com.example.wechat.core.util.AppScopeViewModel;
import com.example.wechat.model.Conversation;
import com.example.wechat.model.MessageVO;

import java.util.List;

public class ConversationViewModel extends ViewModel implements AppScopeViewModel {
    private MutableLiveData<Conversation> clearConversationMessageLiveData;

    public MutableLiveData<Conversation> clearConversationMessageLiveData() {
        if (clearConversationMessageLiveData == null) {
            clearConversationMessageLiveData = new MutableLiveData<>();
        }
        return clearConversationMessageLiveData;
    }


    public MutableLiveData<List<MessageVO>> loadAroundMessages(Conversation conversation, String withUser, long focusIndex, int count) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
        List<MessageVO> messages = ChatApi.getMessage();
        result.postValue(messages);
        return result;
    }

    public MutableLiveData<List<MessageVO>> getMessages(Conversation conversation, String withUser, boolean enableLoadRemoteMessageWhenNoMoreLocalOldMessage) {
        return loadOldMessages(conversation, withUser, 0, 0, 20, enableLoadRemoteMessageWhenNoMoreLocalOldMessage);
    }

    public MutableLiveData<List<MessageVO>> loadOldMessages(Conversation conversation, String withUser, long fromMessageId, long fromMessageUid, int count, boolean enableLoadRemoteMessageWhenNoMoreLocalOldMessage) {
        MutableLiveData<List<MessageVO>> result = new MutableLiveData<>();
        List<MessageVO> messages = ChatApi.getMessage();
        result.postValue(messages);
        return result;
    }
}
