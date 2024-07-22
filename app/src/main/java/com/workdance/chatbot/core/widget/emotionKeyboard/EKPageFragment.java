package com.workdance.chatbot.core.widget.emotionKeyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.workdance.chatbot.R;

public class EKPageFragment extends Fragment {
    public static EKPageFragment newInstance(int position) {
        
        Bundle args = new Bundle();
        args.putInt("position", position);
        
        EKPageFragment fragment = new EKPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keyboard_emotion_pager, container, false);

        TextView textView = view.findViewById(R.id.textValue);
        assert getArguments() != null;
        int position = getArguments().getInt("position") + 1;
        // 一定要转成字符串
        textView.setText("page" + position);
        return view;
    }
}
