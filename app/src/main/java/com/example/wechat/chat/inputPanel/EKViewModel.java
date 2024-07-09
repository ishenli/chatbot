
package com.example.wechat.chat.inputPanel;


import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class EKViewModel extends ViewModel {
    private Application application;

    public MutableLiveData<List<String>> pageData = new MutableLiveData<>();

    public static EKViewModel getInstance() {
        return new EKViewModel().create();
    }

    public EKViewModel create() {
        List<String> pageDataList = new ArrayList<>();
        pageDataList.add("a");
        pageDataList.add("b");
        pageDataList.add("c");
        pageData.setValue(pageDataList);
        return this;
    }

}