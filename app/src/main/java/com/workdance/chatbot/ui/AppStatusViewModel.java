package com.workdance.chatbot.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppStatusViewModel extends ViewModel {
    private final MutableLiveData<Boolean> shouldRefreshHome = new MutableLiveData<>(false);

    public void setShouldRefreshHome(boolean shouldRefreshHome) {
        this.shouldRefreshHome.setValue(shouldRefreshHome);
    }

    public MutableLiveData<Boolean> shouldRefreshHomeLiveData() {
        return shouldRefreshHome;
    }
}
