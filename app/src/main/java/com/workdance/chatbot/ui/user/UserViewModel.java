package com.workdance.chatbot.ui.user;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.ChatApi;
import com.workdance.chatbot.core.util.OperateResult;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.repository.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class UserViewModel extends ViewModel {
    public MutableLiveData<UserInfo> mUserInfo = new MutableLiveData<>();
    public MutableLiveData<List<UserInfo>> userList = new MutableLiveData<>(new ArrayList<>());

    public String getDefaultUserName() {
        return ChatApi.getDefaultUser().name;
    }

    public String getUserId() {
        return ChatApi.getUserId();
    }

    public LiveData<UserInfo> getUserInfoByName(String name) {
        if (name.equals(this.getDefaultUserName())) {
            UserInfo userInfo = ChatApi.getDefaultUser();
            this.mUserInfo.setValue(userInfo);
            ChatApi.setCurrentUser(userInfo);
            return mUserInfo;
        }

        LiveData<UserEntity> user = ChatApi.getUserInfoByName(name);
        user.observeForever(user1 -> {
            if (user1 == null) {
                return;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.uid = user1.getUid();
            userInfo.name = user1.getName();
            userInfo.displayName = user1.getNickname();
            userInfo.portrait = user1.getAvatar();
            this.mUserInfo.setValue(userInfo);
        });
        return mUserInfo;
    }

    public LiveData<UserInfo> getUserInfoAsync(String userId, boolean b) {
        LiveData<UserEntity> user = ChatApi.getUserInfo(userId);
        user.observeForever(user1 -> {
            if (user1 == null) {
                return;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.uid = user1.getUid();
            userInfo.name = user1.getName();
            userInfo.displayName = user1.getNickname();
            userInfo.portrait = user1.getAvatar();
            this.mUserInfo.setValue(userInfo);
        });
        return mUserInfo;
    }

    public LiveData<List<UserInfo>> getAllUserInfo() {
        LiveData<List<UserEntity>> users = ChatApi.getAllUserInfo();
        users.observeForever(users1 -> {
            if (users1 == null) {
                return;
            }
            List<UserInfo> userInfoList = new ArrayList<>();
            for (UserEntity userEntity : users1) {
                UserInfo userInfo = new UserInfo();
                userInfo.uid = userEntity.getUid();
                userInfo.name = userEntity.getName();
                userInfo.displayName = userEntity.getNickname();
                userInfo.portrait = userEntity.getAvatar();
                userInfoList.add(userInfo);
            }
            userList.postValue(userInfoList);
        });
        return userList;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<OperateResult<Boolean>> addUserInfo(UserInfo userInfo) {
        MutableLiveData<OperateResult<Boolean>> result = new MutableLiveData<>();
        if (TextUtils.isEmpty(userInfo.portrait)) {
            userInfo.portrait = UserUtil.getRandomAvatar();
        }

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
