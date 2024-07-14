package com.example.wechat.chat.conversationlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wechat.model.ConversationInfo;

import java.util.List;

public class ConversationListViewModel extends ViewModel {
    private MutableLiveData<List<ConversationInfo>> conversationListLiveData;

    public MutableLiveData<List<ConversationInfo>> conversationListLiveData() {
        if (conversationListLiveData == null) {
            conversationListLiveData = new MutableLiveData<>();
        }

//        ChatApi.getConversationList(userInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
//            result.setValue(new OperateResult<>(true, 0));
//        }, throwable -> {
//            result.setValue(new OperateResult<>(false, 1001));
//        });

        return conversationListLiveData;
    }
}

