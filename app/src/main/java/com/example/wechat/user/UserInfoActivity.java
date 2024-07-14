package com.example.wechat.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.wechat.R;
import com.example.wechat.chat.ChatActivity;
import com.example.wechat.core.BaseActivity;
import com.example.wechat.databinding.ActivityUserInfoBinding;
import com.example.wechat.model.Conversation;
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
        binding.aliasOptionItemView.setOnClickListener( v -> goToChangeName());
        binding.chatButton.setOnClickListener(v -> chat());
    }

    private void chat() {
        Intent intent = new Intent(this, ChatActivity.class);
        Conversation conversation = new Conversation(Conversation.ConversationType.Single, userInfo.uid, 0);
        intent.putExtra("conversation", conversation);
        intent.putExtra("conversationTitle", userInfo.displayName);
        startActivity(intent);
        finish();
    }

    private void goToChangeName() {
        String userId = userViewModel.getUserId();
        if (userId.equals(userInfo.uid)) {
            Intent intent = new Intent(this, ChangeMyNameActivity.class);
            intent.putExtra("userInfo", userInfo);
            startActivity(intent);
        } else {
//            Intent intent = new Intent(this, ChangeMyNameActivity.class);
//            startActivity(intent);
        }
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

        String currentUserId = userViewModel.getUserId();
        if (currentUserId.equals(userInfo.uid)) {
        // 表明是当前用户
            binding.chatButton.setVisibility(View.GONE);
            binding.voipChatButton.setVisibility(View.GONE);
            binding.inviteButton.setVisibility(View.GONE);
            binding.aliasOptionItemView.setVisibility(View.VISIBLE);
        } else {
            binding.chatButton.setVisibility(View.VISIBLE);
            binding.voipChatButton.setVisibility(View.VISIBLE);
            binding.inviteButton.setVisibility(View.GONE);
        }

    }

    public void setUserInfo(UserInfo userInfo) {
        Log.d(TAG, "setUserInfo: " + userInfo.displayName);
        userViewModel.userInfo.postValue(userInfo);
    }

}
