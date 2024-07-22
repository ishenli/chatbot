package com.workdance.chatbot.ui.main.my;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {

    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private final MutableLiveData<String> mNum = new MutableLiveData<>();

    public final MutableLiveData<ArrayList<ServiceItem>> mData = new MutableLiveData<>();

    public MyViewModel() {
        mText.setValue("皓默皓默皓默皓默皓默");
        mNum.setValue("ishenli");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getNum() {
        return mNum;
    }

    public LiveData<ArrayList<ServiceItem>> getData() {
        return mData;
    }
}