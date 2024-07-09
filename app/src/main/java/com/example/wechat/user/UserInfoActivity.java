package com.example.wechat.user;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.wechat.R;
import com.example.wechat.core.BaseActivity;
import com.example.wechat.databinding.ActivityUserInfoBinding;
import com.example.wechat.model.UserInfo;

public class UserInfoActivity extends BaseActivity {
    private static final String TAG = "UserInfoActivity";
    private ActivityUserInfoBinding binding;
    private UserInfo userInfo;
    private UserViewModel userViewModel;
    TextView titleTextView;
    @Override
    protected void beforeViews() {
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected String toolbarTitle() {
        return "用户信息";
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
        titleTextView = findViewById(R.id.titleTextView);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }


    @Override
    protected int menu() {
        return R.menu.user_info;
    }

    @Override
    protected void afterViews() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(userViewModel);

        userInfo = getIntent().getParcelableExtra("userInfo");
        if (userInfo != null) {
            setUserInfo(userInfo);
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        Log.d(TAG, "setUserInfo: " + userInfo.displayName);
        userViewModel.userInfo.postValue(userInfo);
    }

}
