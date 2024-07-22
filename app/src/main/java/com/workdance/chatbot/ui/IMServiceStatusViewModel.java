package com.workdance.chatbot.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IMServiceStatusViewModel extends ViewModel {
    private MutableLiveData<Boolean> imServiceStatusLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIMServiceStatusLiveData() {
        return imServiceStatusLiveData;
    }

    public MutableLiveData<Boolean> imServiceStatusLiveData() {
        imServiceStatusLiveData.setValue(true);
        return imServiceStatusLiveData;
    }
}
