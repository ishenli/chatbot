package com.example.wechat.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wechat.main.home.chat.chatList.ChatListItemVO;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    public final MutableLiveData<String> mText;

    public final MutableLiveData<List<ChatListItemVO>> mChatList = new MutableLiveData<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        ArrayList<ChatListItemVO> data = ChatListItemVO.mock();
        mChatList.setValue(data);
    }

    public LiveData<String> getText() {
        return mText;
    }
}