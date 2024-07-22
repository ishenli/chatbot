package com.workdance.chatbot.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.workdance.chatbot.ui.IMServiceStatusViewModel;
import com.workdance.chatbot.R;
import com.workdance.chatbot.ui.chat.conversation.ConversationFragment;
import com.workdance.chatbot.core.BaseActivity;
import com.workdance.chatbot.databinding.ActivityChatBinding;
import com.workdance.chatbot.model.Conversation;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private ConversationFragment conversationFragment;
    private Conversation conversation;
    private boolean isInitialized;

    @Override
    protected void beforeViews() {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    protected String toolbarTitle() {
        Intent intent = getIntent();
        String conversationTitle = intent.getStringExtra("conversationTitle");
        return conversationTitle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
    }

    @Override
    protected void afterViews() {
        IMServiceStatusViewModel imServiceStatusViewModel = new ViewModelProvider(this).get(IMServiceStatusViewModel.class);
        imServiceStatusViewModel.imServiceStatusLiveData().observe(this, status -> {
            if (!isInitialized && status) {
                init();
                isInitialized = true;
            }
        });

        conversationFragment = new ConversationFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerFrameLayout, conversationFragment, "content")
                .commit();
    }

    private void init() {
        Intent intent = getIntent();
//        conversation = intent.getParcelableExtra("conversation");
        String conversationTitle = intent.getStringExtra("conversationTitle");
        String chatId = intent.getStringExtra("chatId");
        conversation = new Conversation(Conversation.ConversationType.Single, chatId);
        boolean isPreJoinedChatRoom = intent.getBooleanExtra("isPreJoinedChatRoom", false);
        String initialFocusedMessageId = intent.getStringExtra("toFocusMessageId");
//        if (conversation == null) {
//            finish();
//            return;
//        }
        conversationFragment.setupConversation(conversation, conversationTitle, initialFocusedMessageId, null, isPreJoinedChatRoom);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        conversation = intent.getParcelableExtra("conversation");
        String conversationTitle = intent.getStringExtra("conversationTitle");
        conversationFragment.setupConversation(conversation, conversationTitle, String.valueOf(-1), conversation.target, false);
    }

    // 重写 onOptionsItemSelected 方法处理返回按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 关闭当前Activity并返回上一个
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }
}
