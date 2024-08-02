package com.workdance.chatbot.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppStatusViewModel extends ViewModel {
    private final MutableLiveData<Boolean> shouldRefreshHome = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> shouldRefreshDashboard = new MutableLiveData<>(false);

    public void setShouldRefreshHome(boolean shouldRefreshHome) {
        this.shouldRefreshHome.setValue(shouldRefreshHome);
    }

    public void setShouldRefreshDashboard(boolean shouldRefreshDashboard) {
        this.shouldRefreshDashboard.setValue(shouldRefreshDashboard);
    }

    public MutableLiveData<Boolean> shouldRefreshHomeLiveData() {
        return shouldRefreshHome;
    }

    public MutableLiveData<Boolean> shouldRefreshDashboardLiveData() {
        return shouldRefreshDashboard;
    }
}
