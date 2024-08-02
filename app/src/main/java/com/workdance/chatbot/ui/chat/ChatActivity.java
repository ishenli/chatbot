package com.workdance.chatbot.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.workdance.chatbot.R;
import com.workdance.chatbot.api.dto.req.ChatReq;
import com.workdance.chatbot.core.BaseActivity;
import com.workdance.chatbot.core.util.StringUtils;
import com.workdance.chatbot.databinding.ActivityChatBinding;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.ui.AppStatusViewModel;
import com.workdance.chatbot.ui.IMServiceStatusViewModel;
import com.workdance.chatbot.ui.chat.conversation.ConversationFragment;
import com.workdance.chatbot.ui.chat.conversation.ConversationViewModel;

public class ChatActivity extends BaseActivity {
    private static final String TAG = "ChatActivity";
    private ActivityChatBinding binding;
    private ConversationFragment conversationFragment;
    private Conversation conversation;
    private boolean isInitialized;
    private ConversationViewModel conversationViewModel;
    private AppStatusViewModel appStatusViewModel;

    @Override
    protected void beforeViews() {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    protected String toolbarTitle() {
        Intent intent = getIntent();
        return intent.getStringExtra("conversationTitle");
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

        conversationViewModel = getActivityScopeViewModel(ConversationViewModel.class);
        appStatusViewModel = getApplicationScopeViewModel(AppStatusViewModel.class);
    }

    private void init() {
        Intent intent = getIntent();
        String conversationTitle = intent.getStringExtra("conversationTitle");
        conversationViewModel.setConversationTitle(conversationTitle);
        String chatId = intent.getStringExtra("chatId");
        String brainId = intent.getStringExtra("brainId");
        boolean isPreJoinedChatRoom = intent.getBooleanExtra("isPreJoinedChatRoom", false);
        String initialFocusedMessageId = intent.getStringExtra("toFocusMessageId");
        // 如果没有 chatId，则需要创新的会话
        if (StringUtils.isEmpty(chatId)) {
            ChatReq chatReq = new ChatReq();
            chatReq.setChatName(conversationTitle);
            chatReq.setBrainId(brainId);
            conversationViewModel.createConversation(chatReq).observe(this, result -> {
                String id = result.getChatId();
                conversation = new Conversation(Conversation.ConversationType.Single, id);
                conversationFragment.setupConversation(conversation, conversationTitle, initialFocusedMessageId, isPreJoinedChatRoom);
                appStatusViewModel.setShouldRefreshHome(true);
            });

        } else {
            conversation = new Conversation(Conversation.ConversationType.Single, chatId);
            conversationFragment.setupConversation(conversation, conversationTitle, initialFocusedMessageId, isPreJoinedChatRoom);
        }

//        if (conversation == null) {
//            finish();
//            return;
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        conversation = intent.getParcelableExtra("conversation");
        String conversationTitle = intent.getStringExtra("conversationTitle");
//        conversationFragment.setupConversation(conversation, conversationTitle, String.valueOf(-1), false);
    }

    // 重写 onOptionsItemSelected 方法处理返回按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppCompatActivity activity = this;
        if (item.getItemId() == R.id.deleteChat) {
            new XPopup.Builder(this).asConfirm("确定删除对话", "删除后，聊天记录将不再恢复",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    // 处理删除聊天记录的逻辑
                                    conversationViewModel.deleteConversation(conversation).observe(activity, result -> {
                                        if (result.isSuccess()) {
                                            // 通知首页更新
                                            appStatusViewModel.setShouldRefreshHome(true);
                                            finish();
                                        } else {
                                            Toast.makeText(activity, "删除失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                    .show();

            return true;
        }

        if (item.getItemId() == R.id.editChatName) {
            new XPopup.Builder(this).asInputConfirm("对话名称", conversationViewModel.getConversationTitle(),
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    if (StringUtils.isEmpty(text)) {
                                        return;
                                    }
                                    ChatReq chatReq = new ChatReq();
                                    chatReq.setChatId(conversation.getId());
                                    chatReq.setChatName(text);
                                    conversationViewModel.modifyConversation(chatReq).observe(activity, result -> {
                                        if (result.isSuccess()) {
                                            conversationViewModel.setConversationTitle(text);
                                            toolBarText.setText(text);
                                            // 通知首页更新
                                            appStatusViewModel.setShouldRefreshHome(true);
                                        } else {
                                            Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected int menu() {
        return R.menu.chat_optionmenu;
    }

}
