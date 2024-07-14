package com.example.wechat.chat.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wechat.core.util.ChatKit;
import com.example.wechat.databinding.ConversationFragmentBinding;
import com.example.wechat.model.Conversation;
import com.example.wechat.model.MessageVO;

import java.util.List;

public class ConversationFragment extends Fragment {
    private static final String TAG = "convFragment";
    private static final int MESSAGE_LOAD_COUNT_PER_TIME = 20;
    private static final int MESSAGE_LOAD_AROUND = 10;
    private static final long TYPING_INTERNAL = 10 * 1000;


    ConversationFragmentBinding binding;

    private ConversationMessageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Long initialFocusedMessageId;
    private Conversation conversation;
    private String conversationTitle;
    private boolean shouldContinueLoadNewMessage = false;
    private ConversationViewModel conversationViewModel;
    private boolean moveToBottom = true;
    private String targetUser;

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

        // 输入面板
        this.initInputPanel();

        // 数据准备
        conversationViewModel = ChatKit.getAppScopeViewModel(ConversationViewModel.class);
        conversationViewModel.clearConversationMessageLiveData().observeForever(clearConversationMessageObserver);
    }



    private void initInputPanel() {
        ConversationInputPanel conversationInputPanel = new ConversationInputPanel(binding, getActivity());
        conversationInputPanel.init();
        conversationInputPanel.setUpConversation(conversation, targetUser);

    }

    public void setupConversation(Conversation conversation, String title, long focusMessageId, String target, boolean isJoinedChatRoom) {
        this.conversation = conversation;
        this.conversationTitle = title;
        this.initialFocusedMessageId = focusMessageId;
        this.targetUser = target;
        setupConversation(conversation);
    }

    public void setupConversation(Conversation conversation) {
        loadMessage(initialFocusedMessageId);

    }

    private void loadMessage(Long focusMessageId) {

        MutableLiveData<List<MessageVO>> messages;
        if (focusMessageId != -1) {
            shouldContinueLoadNewMessage = true;
            messages = conversationViewModel.loadAroundMessages(conversation, targetUser, focusMessageId, MESSAGE_LOAD_AROUND);
        } else {
            messages = conversationViewModel.getMessages(conversation, targetUser, false);
        }

        // 消息有变化，通知列表刷新
        messages.observe(this, uiMessages -> {
            adapter.setMessages(uiMessages);
            if (adapter.getItemCount() > 1) {
                int initialMessagePosition;
                if (focusMessageId != -1) {
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
