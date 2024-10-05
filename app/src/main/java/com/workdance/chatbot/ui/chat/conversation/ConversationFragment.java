package com.workdance.chatbot.ui.chat.conversation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ConversationActivityBinding;
import com.workdance.chatbot.model.Brain;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.ConversationInfo;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.remote.ChatClient;
import com.workdance.chatbot.ui.AppStatusViewModel;
import com.workdance.chatbot.ui.user.UserInfoActivity;
import com.workdance.core.AppKit;
import com.workdance.core.BaseFragment;
import com.workdance.core.widget.InputAwareLayout;
import com.workdance.core.widget.KeyboardAwareLinearLayout;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationFragment extends BaseFragment implements
        KeyboardAwareLinearLayout.OnKeyboardShownListener,
        KeyboardAwareLinearLayout.OnKeyboardHiddenListener,
        ConversationInputPanel.OnConversationInputPanelStateChangeListener,
        ConversationMessageAdapter.OnPortraitClickListener {
    private static final String TAG = "convFragment";
    private static final int MESSAGE_LOAD_COUNT_PER_TIME = 20;
    private static final int MESSAGE_LOAD_AROUND = 10;
    private static final long TYPING_INTERNAL = 10 * 1000;


    ConversationActivityBinding binding;

    private ConversationMessageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String initialFocusedMessageId;
    private Conversation conversation;
    private String conversationTitle;
    private boolean shouldContinueLoadNewMessage = false;
    private ConversationViewModel conversationViewModel;
    private boolean moveToBottom = true;
    private String targetUser;
    private boolean loadingNewMessage;
    private MessageViewModel messageViewModel;
    private AppStatusViewModel appStatusViewModel;
    private InputAwareLayout rootLinearLayout;
    private ConversationInputPanel inputPanel;
    private RecyclerView recyclerView;
    private Handler handler;

    @Override
    protected void initViewModel() {
        // 数据准备
        appStatusViewModel = getApplicationScopeViewModel(AppStatusViewModel.class);
        conversationViewModel = getActivityScopeViewModel(ConversationViewModel.class);
        messageViewModel = getFragmentScopeViewModel(MessageViewModel.class);
        conversationViewModel.clearConversationMessageLiveData().observeForever(clearConversationMessageObserver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ConversationActivityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = binding.msgRecyclerView;
        rootLinearLayout = view.findViewById(R.id.rootLinearLayout);
        initView();
        return view;
    }

    public void initView() {
        handler = new Handler();
        adapter = new ConversationMessageAdapter(this);
        adapter.setOnPortraitClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                if (!recyclerView.canScrollVertically(1)) {
                    moveToBottom = true;
                    if (!loadingNewMessage) {
                        // 底部的时候去加载
                        int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                        if (lastVisibleItem > adapter.getItemCount() - 3) {
                            loadMoreNewMessages();
                        }
                    }
                } else {
                    moveToBottom = false;
                }
            }
        });
        this.initInputPanel();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initInputPanel() {
        rootLinearLayout.addOnKeyboardShownListener(this);
        inputPanel = binding.inputPanelFrameLayout;
        inputPanel.init(this, rootLinearLayout);
        inputPanel.setOnConversationInputPanelStateChangeListener(this);
        inputPanel.setOnSendSubmitClickListener(new ConversationInputPanel.onSendSubmitClickListener() {
            @Override
            public void onSendSubmitClick(String txtContent) {
                messageViewModel.sendTextMsg(conversation, toUsers(), txtContent).observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        adapter.addNewMessage(message);
                        String avatar = getReceiverAvatar(toUsers());
                        adapter.showStreamLoading("AI 正在思考中...", avatar);
                        scrollToCycleListBottom();
                        // 接受 AI 的内容返回
                        requestAIService(conversation, toUsers(), txtContent, message);

                    }
                });
            }
        });
        binding.contentLayout.setOnTouchListener((v, event) -> ConversationFragment.this.onTouch(v, event));
        binding.msgRecyclerView.setOnTouchListener((v, event) -> ConversationFragment.this.onTouch(v, event));
    }

    private boolean onTouch(View v, MotionEvent event) {
        inputPanel.closeConversationInputPanel();
        return false;
    }

    private Brain getReceiverBrain(List<String> toUsers) {
        ConversationInfo conversationInfo = conversationViewModel.getConversationInfoLiveData().getValue();
        Brain brain = new Brain();
        if (conversationInfo != null && conversationInfo.getBrains() != null) {
            if (toUsers.size() == 1) {
                brain = conversationInfo.getBrains().stream().filter(item -> item.getBrainId().equals(toUsers.get(0))).collect(Collectors.toList()).get(0);
            }
        }
        return brain;
    }

    private String getReceiverAvatar(List<String> toUsers) {
        Brain brain = getReceiverBrain(toUsers);
        if (brain.getLogo() == null) {
            return "";
        }
        return brain.getLogo();
    }

    public void requestAIService(Conversation conversation, List<String> toUsers, String question, MessageVO askMessageVo) {
        // 拿到 MessageVo 直接更新
        Brain brain = getReceiverBrain(toUsers);
        conversation.setTarget(toUsers.get(0));
        messageViewModel.receiveMessage(question, brain, conversation, askMessageVo).observe(getViewLifecycleOwner(), messageVO -> {
            if (messageVO != null) {
                adapter.hideStreamLoading();
                adapter.addNewMessage(messageVO);
                AppKit.postTaskDelay(() -> {
                    binding.msgRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }, 100);
                appStatusViewModel.setShouldRefreshHome(true);
            }
        });
    }

    // 程序的启动入口，activity 来调用
    public void setupConversation(Conversation conversation, String title, String focusMessageId, boolean isJoinedChatRoom) {
        this.conversation = conversation;
        this.conversationTitle = title;
        this.initialFocusedMessageId = focusMessageId;
        setupConversation(conversation);
    }

    public void setupConversation(Conversation conversation) {
        // 获取 Conversation 的详情
        conversationViewModel.loadConversationInfo(conversation).observe(this, conversationInfo -> {
            if (conversationInfo != null) {
                // this.initInputPanel(); // 需要消费 conversation_id
                // 获取 brand_id
                targetUser = conversationInfo.getDefaultBrain().getBrainId();
            }
        });

        loadMessage(initialFocusedMessageId);
    }

    private void loadMessage(String focusMessageId) {

        MutableLiveData<List<MessageVO>> messages;
        if (!TextUtils.isEmpty(focusMessageId)) {
            shouldContinueLoadNewMessage = true;
            messages = conversationViewModel.loadAroundMessages(conversation, targetUser);
        } else {
            messages = conversationViewModel.loadMessages(conversation);
        }

        // 消息有变化，通知列表刷新
        messages.observe(this, uiMessages -> {
            adapter.setMessages(uiMessages);
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() > 1) {
                int initialMessagePosition;
                if (!TextUtils.isEmpty(focusMessageId)) {
                    initialMessagePosition = adapter.getMessagePosition(focusMessageId);
                    if (initialMessagePosition != -1) {
                        binding.msgRecyclerView.scrollToPosition(initialMessagePosition);
                        adapter.highlightFocusMessage(initialMessagePosition);
                    }
                } else {
                    moveToBottom = true;
                    binding.msgRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
    }

    private void reloadMessage() {
    }

    private void loadMoreNewMessages() {
        loadingNewMessage = true;
        conversationViewModel.loadNewMessages(conversation, targetUser).observe(this, messages -> {
            loadingNewMessage = false;
            if (messages != null && !messages.isEmpty()) {
                adapter.addMessagesAtTail(messages);
            }
        });
    }

    private Observer<Conversation> clearConversationMessageObserver = new Observer<Conversation>() {
        @Override
        public void onChanged(Conversation conversation) {
            if (conversation.equals(ConversationFragment.this.conversation)) {
                adapter.setMessages(null);
                adapter.notifyDataSetChanged();
            }
        }
    };


    private List<String> toUsers() {
        if (TextUtils.isEmpty(this.targetUser)) {
            return null;
        }
        return Collections.singletonList(this.targetUser);
    }

    @Override
    public void onPortraitClick(String sender) {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        UserInfo userInfo = new UserInfo();
        ChatClient.getUserInfoById(sender).observe(this, my -> {
            userInfo.displayName = my.displayName;
            userInfo.uid = my.uid;
            userInfo.name = my.name;
            userInfo.portrait = my.portrait;
            intent.putExtra("userInfo", userInfo);
            startActivity(intent);
        });
    }

    @Override
    public void onKeyboardShown() {
        inputPanel.onKeyboardShown();
        // recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        scrollToCycleListBottom();
    }

    public void scrollToCycleListBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION) {
                // 获取最后一个可见视图的 ViewHolder
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(lastVisibleItemPosition);
                if (viewHolder != null) {
                    // 获取最后一个可见视图的高度
                    View itemView = viewHolder.itemView;
                    int lastItemHeight = itemView.getHeight();
                    int lastItemPosition = adapter.getItemCount() - 1;
                    layoutManager.scrollToPositionWithOffset(lastItemPosition, -lastItemHeight);
                }
            }
        }
    }

    @Override
    public void onKeyboardHidden() {
        inputPanel.onKeyboardHidden();
    }

    @Override
    public void onInputPanelExpanded() {
        scrollToCycleListBottom();
    }

    @Override
    public void onInputPanelCollapsed() {
        // do nothing
    }

    @Override
    public void onPause() {
        super.onPause();
        inputPanel.onActivityPause();
    }
}
