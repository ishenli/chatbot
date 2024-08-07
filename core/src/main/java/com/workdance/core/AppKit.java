package com.workdance.core;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class AppKit {
    private static AppKit chatKit;
    private Application application;

    public static AppKit getAppKit() {
        if (chatKit == null) {
            chatKit = new AppKit();
        }
        return chatKit;
    }

    public static Handler mainHandler = new Handler();

    public void init(Application application) {
        this.application = application;
        Log.d("ChatKit", "init end");
    }

    /**
     * UI 延迟执行任务
     *
     * @param task
     * @param delayMillis
     */
    public static void postTaskDelay(Runnable task, int delayMillis) {
        mainHandler.postDelayed(task, delayMillis);
    }

    public Context getContext() {
        return this.application.getApplicationContext();
    }
}
