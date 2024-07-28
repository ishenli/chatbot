package com.workdance.chatbot.ui.chat.conversation.message.viewholder;

import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emojilibrary.MoonUtils;
import com.workdance.chatbot.R;
import com.workdance.chatbot.ui.chat.conversation.message.annotation.MessageContentType;
import com.workdance.chatbot.ui.chat.conversation.ConversationFragment;
import com.workdance.chatbot.ui.chat.conversation.MessageVO;
import com.workdance.chatbot.ui.chat.conversation.message.core.LoadingMessageContent;
import com.workdance.chatbot.ui.chat.conversation.message.core.PTextMessageContent;

@MessageContentType(value = {
        LoadingMessageContent.class,
        PTextMessageContent.class

})
public class LoadingMessageContentViewHolder extends NormalMessageContentViewHolder implements View.OnClickListener {
    TextView contentTextView;

    public LoadingMessageContentViewHolder(@NonNull ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        bindViews(itemView);
        bindEvents(itemView);
    }

    private void bindViews(View itemView) {
        contentTextView = itemView.findViewById(R.id.contentTextView);
    }

    private void bindEvents(View itemView) {
        itemView.findViewById(R.id.contentTextView).setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
    }


    @Override
    protected void onBind(MessageVO message) {
        // 主要对引用以及文本做一些处理
        LoadingMessageContent loadingMessageContent = (LoadingMessageContent) message.message.content;
        String content = loadingMessageContent.getContent();
        MoonUtils.identifyFaceExpression(fragment.getContext(), contentTextView, content, ImageSpan.ALIGN_BOTTOM);
    }

}
