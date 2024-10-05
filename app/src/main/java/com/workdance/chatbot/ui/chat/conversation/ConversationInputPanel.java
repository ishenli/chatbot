package com.workdance.chatbot.ui.chat.conversation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.emojilibrary.EmotionLayout;
import com.example.emojilibrary.IEmotionSelectedListener;
import com.example.emojilibrary.LQREmotionKit;
import com.example.emojilibrary.MoonUtils;
import com.workdance.chatbot.R;
import com.workdance.core.widget.InputAwareLayout;
import com.workdance.core.widget.KeyboardHeightFrameLayout;
import com.workdance.core.widget.SimpleTextWatcher;
import com.workdance.core.widget.ViewPagerFixed;

import lombok.Setter;

public class ConversationInputPanel extends FrameLayout implements IEmotionSelectedListener {

    private FragmentActivity activity;
    private Fragment fragment;
    private InputAwareLayout rootLinearLayout;

    // 各种页面元素
    EmotionLayout emotionLayout;
    LinearLayout inputContainerLinearLayout;
    TextView disableInputTipTextView;

    ImageView menuImageView;
    ImageView audioImageView;
    ImageView pttImageView;
    Button audioButton;
    EditText editText;
    ImageView emotionImageView;
    ImageView extImageView;
    Button sendButton;
    LinearLayout channelMenuContainerLinearLayout;

    KeyboardHeightFrameLayout emotionContainerFrameLayout;
    KeyboardHeightFrameLayout extContainerFrameLayout;

    ViewPagerFixed extViewPager;

    RelativeLayout refRelativeLayout;
    EditText refEditText;

    // 事件狗子
    @Setter
    private OnConversationInputPanelStateChangeListener onConversationInputPanelStateChangeListener;

    @Setter
    private onSendSubmitClickListener onSendSubmitClickListener;
    private int messageEmojiCount;
    private SharedPreferences sharedPreferences;

    public ConversationInputPanel(@NonNull Context context) {
        super(context);
    }

    public ConversationInputPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public ConversationInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConversationInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void init(Fragment fragment, InputAwareLayout rootInputAwareLayout) {
        LayoutInflater.from(getContext()).inflate(R.layout.conversation_input_panel, this, true);
        bindViews();
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.rootLinearLayout = rootInputAwareLayout;

        sharedPreferences = getContext().getSharedPreferences("sticker", Context.MODE_PRIVATE);

        // emotion
        emotionLayout.setEmotionAddVisiable(true);
        emotionLayout.setEmotionSettingVisiable(true);
        emotionLayout.setEmotionSelectedListener(this);

    }

    private void bindViews() {
        emotionLayout = findViewById(R.id.emotionLayout);
        inputContainerLinearLayout = findViewById(R.id.inputContainerLinearLayout);
        disableInputTipTextView = findViewById(R.id.disableInputTipTextView);
        audioImageView = findViewById(R.id.audioImageView);
        pttImageView = findViewById(R.id.pttImageView);
        audioButton = findViewById(R.id.audioButton);
        editText = findViewById(R.id.editText);
        emotionImageView = findViewById(R.id.emotionImageView);
        extImageView = findViewById(R.id.extImageView);
        sendButton = findViewById(R.id.sendButton);
        channelMenuContainerLinearLayout = findViewById(R.id.channelMenuContainerLinearLayout);
        emotionContainerFrameLayout = findViewById(R.id.emotionContainerFrameLayout);
        emotionLayout = findViewById(R.id.emotionLayout);
        extContainerFrameLayout = findViewById(R.id.extContainerContainerLayout);
        extViewPager = findViewById(R.id.conversationExtViewPager);
        refRelativeLayout = findViewById(R.id.refRelativeLayout);
        refEditText = findViewById(R.id.refEditText);

        // 开始注册各类事件
        sendButton.setOnClickListener(v -> sendMessage());
        emotionImageView.setOnClickListener(v -> onEmotionImageViewClick());
        extImageView.setOnClickListener(v -> onExtImageViewClick());
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterInputTextChanged(s);
            }
        });
    }

    void afterInputTextChanged(Editable editable) {
        if (!editText.getText().toString().trim().isEmpty()) {
            if (activity.getCurrentFocus() == editText) {
                // notifyTyping(TypingMessageContent.TYPING_TEXT);
            }
            sendButton.setVisibility(View.VISIBLE);
            extImageView.setVisibility(View.GONE);
        } else {
            sendButton.setVisibility(View.GONE);
            extImageView.setVisibility(View.VISIBLE);
        }
    }

    void onInputTextChanged(CharSequence s, int start, int before, int count) {
        if (activity.getCurrentFocus() == editText) {
        }
    }

    private void onExtImageViewClick() {
        if (rootLinearLayout.getCurrentInput() == extContainerFrameLayout) {
            hideConversationExtension();
            rootLinearLayout.showSoftkey(editText);
        } else {
            emotionImageView.setImageResource(R.mipmap.ic_chat_emo);
            showConversationExtension();
        }
    }

    private void showConversationExtension() {
        rootLinearLayout.show(editText, extContainerFrameLayout);
        if (onConversationInputPanelStateChangeListener != null) {
            onConversationInputPanelStateChangeListener.onInputPanelExpanded();
        }
    }

    private void hideConversationExtension() {
        if (onConversationInputPanelStateChangeListener != null) {
            onConversationInputPanelStateChangeListener.onInputPanelCollapsed();
        }
    }

    private void onEmotionImageViewClick() {
        if (rootLinearLayout.getCurrentInput() == emotionContainerFrameLayout) {
            hideEmotionLayout();
            rootLinearLayout.showSoftkey(editText);
        } else {
            showEmotionLayout();
        }
    }

    private void showEmotionLayout() {
        audioButton.setVisibility(View.GONE);
        emotionImageView.setImageResource(R.mipmap.ic_chat_keyboard);
        rootLinearLayout.show(editText, emotionContainerFrameLayout);
        if (onConversationInputPanelStateChangeListener != null) {
            onConversationInputPanelStateChangeListener.onInputPanelExpanded();
        }
    }

    private void sendMessage() {
        Editable content = editText.getText();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (onSendSubmitClickListener != null) {
            onSendSubmitClickListener.onSendSubmitClick(content.toString().trim());
        }

        editText.setText("");
    }

    public void closeConversationInputPanel() {
        // extension.reset();
        emotionImageView.setImageResource(R.mipmap.ic_chat_emo);
        rootLinearLayout.hideAttachedInput(true);
        rootLinearLayout.hideCurrentInput(editText);
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable editable = editText.getText();
        if (key.equals("/DEL")) {
            messageEmojiCount--;
            messageEmojiCount = messageEmojiCount < 0 ? 0 : messageEmojiCount;
            editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            messageEmojiCount++;
            int code = Integer.decode(key);
            char[] chars = Character.toChars(code);
            String value = Character.toString(chars[0]);
            for (int i = 1; i < chars.length; i++) {
                value += Character.toString(chars[i]);
            }
            int start = editText.getSelectionStart();
            int end = editText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            editable.replace(start, end, value);

            int editEnd = editText.getSelectionEnd();
            MoonUtils.replaceEmoticons(LQREmotionKit.getContext(), editable, 0, editable.toString().length());
            editText.setSelection(editEnd);
        }
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {

    }

    public void onKeyboardShown() {
        hideEmotionLayout();
    }

    private void hideEmotionLayout() {
        emotionImageView.setImageResource(R.mipmap.ic_chat_emo);
        if (onConversationInputPanelStateChangeListener != null) {
            onConversationInputPanelStateChangeListener.onInputPanelCollapsed();
        }
    }

    public void onKeyboardHidden() {
    }

    public void onActivityPause() {
        updateConversationDraft();
    }
    private void updateConversationDraft() {
        Editable editable = editText.getText();
        // String draft = Draft.toDraftJson(editable, messageEmojiCount, quoteInfo);
        // if (conversation != null) {
        //     messageViewModel.saveDraft(conversation, draft);
        // }
    }

    public interface onSendSubmitClickListener {
        void onSendSubmitClick(String content);
    }


    public interface OnConversationInputPanelStateChangeListener {
        /**
         * 输入面板展开
         */
        void onInputPanelExpanded();

        /**
         * 输入面板关闭
         */
        void onInputPanelCollapsed();
    }
}