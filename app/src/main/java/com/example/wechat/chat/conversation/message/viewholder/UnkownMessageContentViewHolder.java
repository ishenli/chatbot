/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.example.wechat.chat.conversation.message.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.R;
import com.example.wechat.chat.conversation.ConversationFragment;
import com.example.wechat.chat.conversation.message.core.UnknownMessageContent;
import com.example.wechat.core.annotation.MessageContentType;
import com.example.wechat.model.MessageVO;


@MessageContentType(UnknownMessageContent.class)
public class UnkownMessageContentViewHolder extends NormalMessageContentViewHolder {
    TextView contentTextView;

    public UnkownMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        bindViews(itemView);
    }

    private void bindViews(View itemView) {
        contentTextView =itemView.findViewById(R.id.contentTextView);
    }

    public void onBind(MessageVO message) {
        contentTextView.setText("暂不支持此消息，请升级最新版本!");
    }

}
