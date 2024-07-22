package com.workdance.chatbot.core.util;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

public class ChatKit {
    private static ChatKit chatKit;
    private ViewModelStore viewModelStore;
    private static ViewModelProvider viewModelProvider;

    public static ChatKit getChatKit() {
        if (chatKit == null) {
            chatKit = new ChatKit();
        }
        return chatKit;
    }

    public void init(Application application) {
        viewModelStore = new ViewModelStore();
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        viewModelProvider = new ViewModelProvider(viewModelStore, factory);

        Log.d("ChatKit", "init end");
    }

    public static <T extends ViewModel> T getAppScopeViewModel(@NonNull Class<T> modelClass) {
        if (!AppScopeViewModel.class.isAssignableFrom(modelClass)) {
            throw new IllegalArgumentException("the model class should be subclass of AppScopeViewModel");
        }
        return viewModelProvider.get(modelClass);
    }
}
