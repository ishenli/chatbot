package com.workdance.chatbot;

import android.content.Context;

import com.example.emojilibrary.LQREmotionKit;
import com.workdance.chatbot.remote.ChatApi;
import com.workdance.core.AppKit;
import com.workdance.core.BaseApplication;
import com.workdance.core.util.LogUtils;
import com.workdance.multimedia.Config;

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

        setupWFCDirs();
    }

    private void setupWFCDirs() {
        Config.VIDEO_SAVE_DIR = this.getDir("video", Context.MODE_PRIVATE).getAbsolutePath();
        Config.AUDIO_SAVE_DIR = this.getDir("audio", Context.MODE_PRIVATE).getAbsolutePath();
        Config.PHOTO_SAVE_DIR = this.getDir("photo", Context.MODE_PRIVATE).getAbsolutePath();
        Config.FILE_SAVE_DIR = this.getDir("file", Context.MODE_PRIVATE).getAbsolutePath();
    }
}