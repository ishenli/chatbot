package com.workdance.chatbot.ui;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.workdance.chatbot.R;
import com.workdance.chatbot.api.ChatAIClient;
import com.workdance.chatbot.api.dto.req.ChatReq;

public class EmotionActivity extends AppCompatActivity {
    public void onCreate(android.os.Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

//        EmotionLayout emotionLayout = (EmotionLayout) findViewById(R.id.emotionLayout);
//
//        emotionLayout.setEmotionAddVisiable(true);
//        emotionLayout.setEmotionSettingVisiable(true);
        init();
    }

    protected void init() {
        TextView textView = (TextView) findViewById(R.id.testOllama);
        ChatReq chatReq = new ChatReq();
        chatReq.setQuestion("请简单介绍下 Android");
        ChatAIClient.askOllama(chatReq).observe(this, s -> {
            textView.setText(s.getData());
        });
    }
}
