package com.example.wechat.user;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wechat.api.ChatApi;
import com.example.wechat.core.util.OperateResult;
import com.example.wechat.model.UserInfo;
import com.example.wechat.repository.entity.User;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class UserViewModel extends ViewModel {
    public MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();

    public String getUserId() {
//        return ChatManager.Instance().getUserId();
        return "997f4eb8-8dd2-4eea-bdc4-94674068b799";
    }

    public LiveData<UserInfo> getUserInfoAsync(String userId, boolean b) {
        LiveData<User> user = ChatApi.getUserInfo(userId);
        user.observeForever(user1 -> {
            UserInfo userInfo = new UserInfo();
            userInfo.uid = user1.getUid();
            userInfo.name = user1.getName();
            userInfo.displayName = user1.getNickname();
            userInfo.portrait = user1.getAvatar();
            this.userInfo.setValue(userInfo);
        });
        return userInfo;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<OperateResult<Boolean>> addUserInfo(UserInfo userInfo) {
        MutableLiveData<OperateResult<Boolean>> result = new MutableLiveData<>();
        ChatApi.addUserInfo(userInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            result.setValue(new OperateResult<>(true, 0));
        }, throwable -> {
            result.setValue(new OperateResult<>(false, 1001));
        });

        return result;
    }

    public MutableLiveData<OperateResult<Boolean>> modifyUserInfo(UserInfo userInfo) {
        MutableLiveData<OperateResult<Boolean>> result = new MutableLiveData<>();
        boolean success = ChatApi.updateUserInfo(userInfo);
        if (success) {
            result.setValue(new OperateResult<>(true, 0));
        } else {
            result.setValue(new OperateResult<>(false, 0));
        }
        return result;
    }
}
