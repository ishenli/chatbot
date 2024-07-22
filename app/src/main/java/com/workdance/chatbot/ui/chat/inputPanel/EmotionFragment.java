package com.workdance.chatbot.ui.chat.inputPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.emojilibrary.EmotionLayout;
import com.workdance.chatbot.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmotionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmotionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EKViewModel emotionboardViewModel;

    public EmotionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmotionKeyboard.
     */
    // TODO: Rename and change types and number of parameters
    public static EmotionFragment newInstance(String param1, String param2) {
        EmotionFragment fragment = new EmotionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        EmotionLayout emotionLayout = root.findViewById(R.id.emotionLayout);

        return root;

    }
}