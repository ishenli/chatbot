package com.workdance.chatbot.ui;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emojilibrary.EmotionLayout;
import com.workdance.chatbot.R;

public class EmotionActivity extends AppCompatActivity {
    public void onCreate(android.os.Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        EmotionLayout emotionLayout = (EmotionLayout) findViewById(R.id.emotionLayout);

        emotionLayout.setEmotionAddVisiable(true);
        emotionLayout.setEmotionSettingVisiable(true);

    }
}
