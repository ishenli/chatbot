package com.workdance.chatbot.core.util;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

public class ChatKit {
    private static ChatKit chatKit;

    public static ChatKit getChatKit() {
        if (chatKit == null) {
            chatKit = new ChatKit();
        }
        return chatKit;
    }

    public static Handler mainHandler = new Handler();

    public void init(Application application) {

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
}
