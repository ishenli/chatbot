package com.workdance.core.util;

import android.util.Log;

import androidx.lifecycle.Observer;

public abstract class SafeObserver<T> implements Observer<T> {

    @Override
    public void onChanged(T t) {
        try {
            onSafeChanged(t);
        } catch (Exception e) {
            handleException(e);
        }
    }

    protected abstract void onSafeChanged(T t);

    protected void handleException(Exception e) {
        // 默认处理异常，可以在这里记录日志或通知用户
        Log.e("SafeObserver", "An error occurred in observer", e);
    }
}