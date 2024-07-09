package com.example.wechat.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wechat.api.chatApi;
import com.example.wechat.model.UserInfo;

public class UserViewModel extends ViewModel {
    public MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();

    public String getUserId() {
//        return ChatManager.Instance().getUserId();
        return "2088202565878663";
    }

    public LiveData<UserInfo> getUserInfoAsync(String userId, boolean b) {
        userInfo.setValue(chatApi.getUserInfo());
        return userInfo;
    }
}
