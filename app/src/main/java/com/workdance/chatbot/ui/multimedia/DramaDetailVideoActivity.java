package com.workdance.chatbot.ui.multimedia;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.workdance.chatbot.databinding.ActivityMmDramaDetailVideoBinding;
import com.workdance.chatbot.ui.multimedia.drama.DramaDetailVideoFragment;
import com.workdance.core.BaseActivity;
import com.workdance.core.util.ViewUtils;

/**
 * 视频播放页面
 */
public class DramaDetailVideoActivity extends BaseActivity {
    private ActivityMmDramaDetailVideoBinding binding;

    @Override
    protected int toolbarDisplayType() {
        return BaseActivity.TOOLBAR_HIDDEN;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(binding.container.getId(), DramaDetailVideoFragment.class, null, DramaDetailVideoFragment.TAG)
                    .commit();
        }
    }

    @Override
    protected void beforeViews() {
        binding = ActivityMmDramaDetailVideoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        ((ViewGroup.MarginLayoutParams) toolbar.getLayoutParams()).topMargin = ViewUtils.getStatusBarHeight(this);
        super.setActionBarTheme(true, true, "", Color.TRANSPARENT, Color.WHITE);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

}
