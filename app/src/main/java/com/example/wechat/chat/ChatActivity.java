package com.example.wechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.wechat.IMServiceStatusViewModel;
import com.example.wechat.R;
import com.example.wechat.chat.conversation.ConversationFragment;
import com.example.wechat.core.BaseActivity;
import com.example.wechat.databinding.ActivityChatBinding;
import com.example.wechat.model.Conversation;

import java.util.Objects;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private ConversationFragment conversationFragment;
    private Conversation conversation;
    private boolean isInitialized;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void bindViews() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 设置返回按钮可见
        getSupportActionBar().setDisplayShowHomeEnabled(true); // 设置为默认的向上导航图标
        // 可选：设置标题
        getSupportActionBar().setTitle("Your Activity Title");
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
        conversation = intent.getParcelableExtra("conversation");
        String conversationTitle = intent.getStringExtra("conversationTitle");
        boolean isPreJoinedChatRoom = intent.getBooleanExtra("isPreJoinedChatRoom", false);
        long initialFocusedMessageId = intent.getLongExtra("toFocusMessageId", -1);
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
        conversationFragment.setupConversation(conversation, conversationTitle, -1, conversation.target, false);

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
