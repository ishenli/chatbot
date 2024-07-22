package com.workdance.chatbot.ui.chat.conversation.message.viewholder;

import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emojilibrary.MoonUtils;
import com.workdance.chatbot.R;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;
import com.workdance.chatbot.ui.chat.conversation.ConversationFragment;
import com.workdance.chatbot.ui.chat.conversation.message.core.PTextMessageContent;
import com.workdance.chatbot.core.annotation.MessageContentType;
import com.workdance.chatbot.ui.chat.conversation.MessageVO;

@MessageContentType(value = {
        TextMessageContent.class,
        PTextMessageContent.class

})
public class TextMessageContentViewHolder extends NormalMessageContentViewHolder implements View.OnClickListener{
    TextView contentTextView;
    TextView refTextView;

    public TextMessageContentViewHolder(@NonNull ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        bindViews(itemView);
        bindEvents(itemView);
    }
    private void bindViews(View itemView) {
        contentTextView = itemView.findViewById(R.id.contentTextView);
        refTextView = itemView.findViewById(R.id.refTextView);
    }
    private void bindEvents(View itemView) {
        itemView.findViewById(R.id.contentTextView).setOnClickListener(this::onClick);
        itemView.findViewById(R.id.refTextView).setOnClickListener(this::onRefClick);
    }

    private void onRefClick(View view) {
    }

    public void onClick(View view) {
    }


    @Override
    protected void onBind(MessageVO message) {
        // 主要对引用以及文本做一些处理
        TextMessageContent textMessageContent = (TextMessageContent) message.message.content;
        String content = textMessageContent.getContent();
        MoonUtils.identifyFaceExpression(fragment.getContext(), contentTextView, ((TextMessageContent) message.message.content).getContent(), ImageSpan.ALIGN_BOTTOM);
        refTextView.setVisibility(View.GONE);
    }

}
