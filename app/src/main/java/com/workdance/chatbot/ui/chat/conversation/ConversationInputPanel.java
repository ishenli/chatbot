package com.workdance.chatbot.ui.chat.conversation;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;
import com.workdance.chatbot.ui.chat.inputPanel.InputPanelManager;
import com.workdance.chatbot.ui.chat.inputPanel.StageKeyboardFragment;
import com.workdance.chatbot.core.widget.emotionKeyboard.EmotionFragment;
import com.workdance.chatbot.databinding.ConversationFragmentBinding;
import com.workdance.chatbot.model.Conversation;

import java.util.Collections;
import java.util.List;

public class ConversationInputPanel {
    private final FragmentActivity activity;
    private final ConversationFragmentBinding binding;
    private String targetUser;
    private Conversation conversation;
    private MessageViewModel messageViewModel;
    private ConversationViewModel conversationViewModel;

    public ConversationInputPanel(ConversationFragmentBinding binding, FragmentActivity activity) {
        this.binding = binding;
        this.activity = activity;
    }
    void init() {
        InputPanelManager inputPanelManager = new InputPanelManager(activity, activity.getSupportFragmentManager());
        inputPanelManager.bindContentView(binding.chatMessageBody)
                .bindInputEditText(binding.chatEditText)
                .bindMorePanelView(binding.fragmentKeyboardContainer)
                .bindListView(binding.recyclerView)
                .bindMotionBtn(binding.chatEmoBtn, new InputPanelManager.OnMotionClickListenerCallback() {
                    @Override
                    public void onMotionClick(int motionClickState) {
                        Log.d("voiceClickState", "voiceClickState: " + motionClickState);
                        switch (motionClickState) {
                            case InputPanelManager.MOTION_SOFT_KEY_BOARD_INPUT:
                                // 软键盘输入状态
//                                Toast.makeText(ChatActivity.this, "表情", Toast.LENGTH_SHORT).show();
                                break;
                            case InputPanelManager.MOTION_INPUT:
                                // 录音状态
//                                Toast.makeText(ChatActivity.this, "键盘", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .bindVoiceBtn(binding.chatVoiceBtn, new InputPanelManager.OnVoiceClickListenerCallback() {
                    @Override
                    public void onVoiceClick(int voiceClickState) {
                        Log.d("voiceClickState", "voiceClickState: " + voiceClickState);
//                        Toast.makeText(ChatActivity.this, voiceClickState, Toast.LENGTH_SHORT).show();
                    }
                })
                .bindMoreBottom(binding.chatAddBtn)
                .bindSendBtn(binding.sendButton)
                .bindInputEditText(binding.chatEditText)
                .bindMoreInputFragment(new StageKeyboardFragment())
                .bindMotionInputFragment(new EmotionFragment(activity));

        bindEvents(binding);

        messageViewModel =  new ViewModelProvider(this.activity).get(MessageViewModel.class);
        conversationViewModel = new ViewModelProvider(this.activity).get(ConversationViewModel.class);
    }

    void setupConversation(Conversation conversation, String targetUser) {
        this.conversation = conversation;
        this.targetUser = targetUser;
    }

    private void bindEvents(ConversationFragmentBinding binding) {
        binding.sendButton.setOnClickListener(v -> {
            Editable content = binding.chatEditText.getText();
            if (TextUtils.isEmpty(content)) {
                return;
            }

            TextMessageContent txtContent = new TextMessageContent(content.toString().trim());

            messageViewModel.sendTextMsg(conversation, toUsers(), txtContent);
            conversationViewModel.getMessages(conversation);
        });
    }

    private List<String> toUsers() {
        if (TextUtils.isEmpty(this.targetUser)) {
            return null;
        }
        return Collections.singletonList(this.targetUser);
    }
}
