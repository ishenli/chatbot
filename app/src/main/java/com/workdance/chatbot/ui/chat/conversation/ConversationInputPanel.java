package com.workdance.chatbot.ui.chat.conversation;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ConversationFragmentBinding;
import com.workdance.chatbot.ui.chat.inputPanel.InputPanelManager;
import com.workdance.chatbot.ui.chat.inputPanel.StageKeyboardFragment;
import com.workdance.core.widget.emotionKeyboard.EmotionFragment;

import lombok.Setter;

public class ConversationInputPanel {
    private final FragmentActivity activity;
    private final ConversationFragmentBinding binding;
    private String targetUser;
    @Setter
    private onSendSubmitClickListener onSendSubmitClickListener;

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
                .bindMotionInputFragment(new EmotionFragment(activity, R.id.chat_edit_text));
        bindEvents(binding);
    }

    private void bindEvents(ConversationFragmentBinding binding) {
        binding.sendButton.setOnClickListener(v -> {
            Editable content = binding.chatEditText.getText();
            if (TextUtils.isEmpty(content)) {
                return;
            }

            if (onSendSubmitClickListener != null) {
                onSendSubmitClickListener.onSendSubmitClick(content.toString().trim());
            }

            binding.chatEditText.setText("");
        });
    }

    public interface onSendSubmitClickListener {
        void onSendSubmitClick(String content);
    }
}
