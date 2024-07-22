package com.workdance.chatbot.ui.chat.conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.core.util.ChatKit;
import com.workdance.chatbot.databinding.ConversationFragmentBinding;
import com.workdance.chatbot.model.Conversation;

import java.util.List;

public class ConversationFragment extends Fragment {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ConversationFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        bindEvents(view);
        initView();
        return view;
    }

    private void bindEvents(View view) {
    }

    private void initView() {
        adapter = new ConversationMessageAdapter(this);
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

//        // 输入面板
//        this.initInputPanel();

        // 数据准备
        conversationViewModel = ChatKit.getAppScopeViewModel(ConversationViewModel.class);
        conversationViewModel.clearConversationMessageLiveData().observeForever(clearConversationMessageObserver);
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


    private void initInputPanel() {
        ConversationInputPanel conversationInputPanel = new ConversationInputPanel(binding, getActivity());
        conversationInputPanel.init();
        conversationInputPanel.setupConversation(conversation, targetUser);

    }

    public void setupConversation(Conversation conversation, String title, String focusMessageId, String target, boolean isJoinedChatRoom) {
        this.conversation = conversation;
        this.conversationTitle = title;
        this.initialFocusedMessageId = focusMessageId;
        this.targetUser = target;
        setupConversation(conversation);
    }

    public void setupConversation(Conversation conversation) {
        // 获取 Conversation 的详情
        conversationViewModel.getConversationInfo(conversation).observe(this, conversationInfo -> {
            if (conversationInfo != null) {
                this.initInputPanel(); // 需要消费 conversation_id
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
            messages = conversationViewModel.getMessages(conversation);
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

    private Observer<Conversation> clearConversationMessageObserver = new Observer<Conversation>() {
        @Override
        public void onChanged(Conversation conversation) {
            if (conversation.equals(ConversationFragment.this.conversation)) {
                adapter.setMessages(null);
                adapter.notifyDataSetChanged();
            }
        }
    };
}
