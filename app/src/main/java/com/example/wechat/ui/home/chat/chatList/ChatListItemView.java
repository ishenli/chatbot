package com.example.wechat.ui.home.chat.chatList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.wechat.R;
import com.example.wechat.databinding.ViewChatListItemBinding;

public class ChatListItemView extends LinearLayout {

    private ViewChatListItemBinding viewChatListItemBinding;

    private ChatListItemViewModel chatListItemViewModel;

    public ChatListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.view_chat_list_item, this, true);

        if (context instanceof ViewModelStoreOwner) {
            chatListItemViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ChatListItemViewModel.class);
        }

        chatListItemViewModel.mName.setValue("皓默");
        chatListItemViewModel.mMessage.setValue("haomo");

        initEvent();
    }

    private void initEvent() {

    }
}
