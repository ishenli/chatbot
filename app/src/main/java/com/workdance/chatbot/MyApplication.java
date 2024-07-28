package com.workdance.chatbot;

import com.example.emojilibrary.LQREmotionKit;
import com.workdance.chatbot.api.ChatApi;
import com.workdance.chatbot.core.BaseApplication;
import com.workdance.chatbot.core.util.ChatKit;

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LQREmotionKit.init(getApplicationContext());
        ChatApi.init(this);
        ChatKit chatKit = ChatKit.getChatKit();
        chatKit.init(this);
    }
}