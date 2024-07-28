package com.workdance.chatbot.ui.chat.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.api.ChatClient;
import com.workdance.chatbot.core.BaseFragment;
import com.workdance.chatbot.core.util.ChatKit;
import com.workdance.chatbot.databinding.ConversationFragmentBinding;
import com.workdance.chatbot.model.Brain;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.ConversationInfo;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.AppStatusViewModel;
import com.workdance.chatbot.ui.user.UserInfoActivity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationFragment extends BaseFragment implements ConversationMessageAdapter.OnPortraitClickListener {
    private static final String TAG = "convFragment";
    private static final int MESSAGE_LOAD_COUNT_PER_TIME = 20;
    private static final int MESSAGE_LOAD_AROUND = 10;
    private static final long TYPING_INTERNAL = 10 * 1000;


    ConversationFragmentBinding binding;

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
        binding = ConversationFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        adapter = new ConversationMessageAdapter(this);
        adapter.setOnPortraitClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        return view;
    }

    private void initInputPanel() {
        ConversationInputPanel conversationInputPanel = new ConversationInputPanel(binding, getActivity());
        conversationInputPanel.init();
        conversationInputPanel.setOnSendSubmitClickListener(new ConversationInputPanel.onSendSubmitClickListener() {
            @Override
            public void onSendSubmitClick(String txtContent) {
                // 处理群聊的场景
                if (conversation.type == Conversation.ConversationType.Group) {

                } else {
                    messageViewModel.sendTextMsg(conversation, toUsers(), txtContent).observe(getViewLifecycleOwner(), message -> {
                        if (message != null) {
                            adapter.addNewMessage(message);
                            String avatar = getReceiverAvatar(toUsers());
                            adapter.showStreamLoading("AI 正在思考中...", avatar);
                            binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            // 接受 AI 的内容返回
                            requestAIService(conversation, toUsers(), txtContent, message);

                        }
                    });
                }

            }
        });
    }

    private String getReceiverAvatar(List<String> toUsers) {
        ConversationInfo conversationInfo = conversationViewModel.getConversationInfoLiveData().getValue();
        String avatar = "";
        if (conversationInfo != null && conversationInfo.getBrains() != null) {
            if (toUsers.size() == 1) {
                Brain brain = conversationInfo.getBrains().stream().filter(item -> item.getBrainId().equals(toUsers.get(0))).collect(Collectors.toList()).get(0);
                avatar = brain.getLogo();
            }
        }
        return avatar;
    }

    public void requestAIService(Conversation conversation, List<String> toUsers, String question, MessageVO askMessageVo) {
        // 拿到 MessageVo 直接更新
        String avatar = getReceiverAvatar(toUsers);
        conversation.setTarget(toUsers.get(0));
        messageViewModel.receiveMessage(question, avatar, conversation, askMessageVo).observe(getViewLifecycleOwner(), messageVO -> {
            if (messageVO != null) {
                adapter.hideStreamLoading();
                adapter.addNewMessage(messageVO);
                ChatKit.postTaskDelay(() -> {
                    binding.recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
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
                        binding.recyclerView.scrollToPosition(initialMessagePosition);
                        adapter.highlightFocusMessage(initialMessagePosition);
                    }
                } else {
                    moveToBottom = true;
                    binding.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
}
