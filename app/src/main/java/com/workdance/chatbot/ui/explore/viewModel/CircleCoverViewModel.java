package com.workdance.chatbot.ui.explore.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CircleCoverViewModel extends ViewModel {
    private final MutableLiveData<CircleCover> mCircleCover = new MutableLiveData<>();

    public MutableLiveData<CircleCover> getCircleCover() {
        return mCircleCover;
    }
    public void setCircleCover(CircleCover circleCover) {
        mCircleCover.setValue(circleCover);
    }
}
