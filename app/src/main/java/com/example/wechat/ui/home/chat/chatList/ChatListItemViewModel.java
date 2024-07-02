package com.example.wechat.ui.home.chat.chatList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatListItemViewModel extends ViewModel {
    public final MutableLiveData<String> mName = new MutableLiveData<>();
    public final MutableLiveData<String> mMessage = new MutableLiveData<>();
}
