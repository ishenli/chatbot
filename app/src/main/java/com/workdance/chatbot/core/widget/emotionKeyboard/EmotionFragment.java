package com.workdance.chatbot.core.widget.emotionKeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.emojilibrary.EmotionLayout;
import com.example.emojilibrary.IEmotionSelectedListener;
import com.example.emojilibrary.LQREmotionKit;
import com.example.emojilibrary.MoonUtils;
import com.workdance.chatbot.R;
import com.workdance.chatbot.ui.chat.inputPanel.EKViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmotionFragment extends Fragment implements IEmotionSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int TYPING_INTERVAL_IN_SECOND = 10;
    private static final int MAX_EMOJI_PER_MESSAGE = 50;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Activity mActivty;
    private EditText editText;
    private int messageEmojiCount = 0;

    private EKViewModel emotionboardViewModel;

    public EmotionFragment(Activity activity) {
        // Required empty public constructor
        mActivty = activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emotionboardViewModel =
                new ViewModelProvider(this).get(EKViewModel.class);

        View root = inflater.inflate(R.layout.fragment_keyboard_emotion, container, false);
        bindEvents();

        // 初始化键盘
        EmotionLayout emotionLayout = root.findViewById(R.id.emotionLayout);
        emotionLayout.setEmotionAddVisiable(true);
        emotionLayout.setEmotionSelectedListener(this);
//        emotionLayout.setEmotionSettingVisiable(true);
        return root;

    }

    private void bindEvents() {
        editText = mActivty.findViewById(R.id.chat_edit_text);
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable editable = editText.getText();
        if (key.equals("/DEL")) {
            messageEmojiCount--;
            messageEmojiCount = messageEmojiCount < 0 ? 0 : messageEmojiCount;
            editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            if (messageEmojiCount >= MAX_EMOJI_PER_MESSAGE) {
                Toast.makeText(mActivty, "最多允许输入" + MAX_EMOJI_PER_MESSAGE + "个表情符号", Toast.LENGTH_SHORT).show();
                return;
            }
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
}