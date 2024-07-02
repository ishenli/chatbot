package com.example.wechat.util;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<T extends Loader> implements ViewModelProvider.Factory {
    private final Application application;
    private final T loader;

    public ViewModelFactory(Application application, T loader) {
        this.application = application;
        this.loader = loader;
    }
}