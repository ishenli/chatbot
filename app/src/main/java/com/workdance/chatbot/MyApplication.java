package com.workdance.chatbot;

import com.example.emojilibrary.LQREmotionKit;
import com.workdance.chatbot.remote.ChatApi;
import com.workdance.core.AppKit;
import com.workdance.core.BaseApplication;
import com.workdance.core.util.LogUtils;

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LQREmotionKit.init(getApplicationContext());
        ChatApi.init(this);
        AppKit chatKit = AppKit.getAppKit();
        chatKit.init(this);

        // 各种开关
        LogUtils.ENABLE_LOG = true;
    }
}