package com.example.wechat.main.dashboard.assistantList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssistantListViewModel extends ViewModel {
    public final MutableLiveData<String> mName = new MutableLiveData<>();
    public final MutableLiveData<String> mMessage = new MutableLiveData<>();
}
