package com.workdance.chatbot.ui.explore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ActivityExploreCycleBinding;
import com.workdance.chatbot.ui.AppActivity;
import com.workdance.core.BaseActivity;
import com.workdance.core.util.ViewUtils;

public class ExploreActivity extends AppActivity {
    private static final String TAG = "UserInfoActivity";
    private ActivityExploreCycleBinding binding;

    public static void intentInto(Activity activity) {
        Intent intent = new Intent(activity, ExploreActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void beforeViews() {
        binding = ActivityExploreCycleBinding.inflate(getLayoutInflater());
    }

    @Override
    protected int toolbarDisplayType() {
        return BaseActivity.TOOLBAR_DEFAULT;
    }

    @Override
    protected String toolbarTitle() {
        return "文字居中";
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
    }


    @Override
    protected void afterViews() {
        setSupportActionBar(toolbar);
        ViewUtils.setSystemBarTheme(this, Color.TRANSPARENT, true, true, Color.WHITE, false, false);
        setActionBarTheme(true, false, "探索", Color.TRANSPARENT, Color.BLACK);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(binding.container.getId(), new CircleFragment(), CircleFragment.TAG)
                .commit();
    }

    @Override
    protected int menu() {
        return R.menu.cycle_optionmenu;
    }
}
