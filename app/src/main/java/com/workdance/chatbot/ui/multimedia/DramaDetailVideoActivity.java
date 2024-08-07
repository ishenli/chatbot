package com.workdance.chatbot.ui.multimedia;

import android.view.View;

import com.workdance.core.BaseActivity;
import com.workdance.chatbot.databinding.ActivityMmDramaDetailVideoBinding;

public class DramaDetailVideoActivity extends BaseActivity {
    private ActivityMmDramaDetailVideoBinding binding;

    @Override
    protected void beforeViews() {
        binding = ActivityMmDramaDetailVideoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

}
