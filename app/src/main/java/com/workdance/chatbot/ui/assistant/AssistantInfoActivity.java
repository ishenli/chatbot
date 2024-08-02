package com.workdance.chatbot.ui.assistant;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.workdance.chatbot.R;
import com.workdance.chatbot.core.BaseActivity;
import com.workdance.chatbot.databinding.ActivityAssistantInfoBinding;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.chat.ChatActivity;

public class AssistantInfoActivity extends BaseActivity {
    private static final String TAG = "AssistantInfoActivity";
    private ActivityAssistantInfoBinding binding;
    private UserInfo userInfo;
    private AssistantViewModel assistantViewModel;
    TextView titleTextView;
    @Override
    protected void beforeViews() {
        binding = ActivityAssistantInfoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected String toolbarTitle() {
        return "助理详情";
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
        titleTextView = findViewById(R.id.titleTextView);
        binding.chatButton.setOnClickListener(v -> chat());
    }

    private void chat() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("conversationTitle", userInfo.displayName);
        intent.putExtra("brainId", userInfo.uid);
        startActivity(intent);
        finish();
    }


    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }


    @Override
    protected void afterViews() {
        assistantViewModel = getApplicationScopeViewModel(AssistantViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(assistantViewModel);

        userInfo = getIntent().getParcelableExtra("userInfo");
        if (userInfo != null) {
            Assistant assistant = new Assistant();
            assistant.setName(userInfo.displayName);
            assistant.setDescription(userInfo.name);
            assistant.setLogo(userInfo.portrait);
            assistant.setBrainId(userInfo.uid);
            assistantViewModel.assistant.postValue(assistant);
        }

        binding.chatButton.setVisibility(View.VISIBLE);
    }

}
