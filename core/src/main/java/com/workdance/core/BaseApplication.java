package com.workdance.core;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class BaseApplication extends Application implements ViewModelStoreOwner {
    //Application ViewModel
    private ViewModelStore mViewModelStore;


    @Override
    public void onCreate() {
        super.onCreate();
        mViewModelStore = new ViewModelStore();

        // 记录异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // 记录异常日志
                Log.e("UncaughtException", "Uncaught exception in thread " + t.getName(), e);

                // 处理异常，例如，收集崩溃报告，重启应用，或者退出应用
                handleUncaughtException(t, e);
            }
        });
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mViewModelStore;
    }


    private void handleUncaughtException(Thread t, Throwable e) {
        // 示例：简单地退出应用程序
        System.exit(1);
    }
}