package com.workdance.chatbot.ui.friend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ActivityMoreInfoBinding;
import com.workdance.chatbot.ui.AppActivity;
import com.workdance.chatbot.ui.user.UserInfoCreateActivity;
import com.workdance.core.util.ViewUtils;

public class FriendCycleActivity extends AppActivity {
    private static final String TAG = "UserInfoActivity";
    private ActivityMoreInfoBinding binding;
    TextView titleTextView;

    public static void intentInto(Activity activity) {
        Intent intent = new Intent(activity, FriendCycleActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void beforeViews() {
        binding = ActivityMoreInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected String toolbarTitle() {
        return "更多设置";
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
        titleTextView = findViewById(R.id.titleTextView);
        binding.addNewUser.setOnClickListener( v -> addNewUser());
    }

    private void addNewUser() {
        Intent intent = new Intent(this, UserInfoCreateActivity.class);
        startActivity(intent);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }


    @Override
    protected void afterViews() {
        binding.setLifecycleOwner(this);
        ViewUtils.setSystemBarTheme(this, Color.TRANSPARENT, true, true, Color.parseColor("#EDEDED"), false, false);
    }
}
