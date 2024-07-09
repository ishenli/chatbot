package com.example.wechat.chat.conversation.message.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.R;
import com.example.wechat.chat.conversation.ConversationFragment;
import com.example.wechat.chat.conversation.ConversationMessageAdapter;
import com.example.wechat.chat.conversation.MessageViewModel;
import com.example.wechat.chat.conversation.message.Message;
import com.example.wechat.core.util.TimeUtils;
import com.example.wechat.model.MessageVO;

public abstract class MessageContentViewHolder extends RecyclerView.ViewHolder {
    @NonNull
    protected ConversationFragment fragment;
    protected View itemView;
    protected MessageVO message;
    protected int position;
    protected RecyclerView.Adapter adapter;
    protected MessageViewModel messageViewModel;

    TextView timeTextView;


    public MessageContentViewHolder(@NonNull ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(itemView);
        this.fragment = fragment;
        this.itemView = itemView;
        this.adapter = adapter;
        messageViewModel = new ViewModelProvider(fragment).get(MessageViewModel.class);
        bindViews(itemView);
    }

    private void bindViews(View itemView) {
        timeTextView = itemView.findViewById(R.id.timeTextView);
    }

    public void onBind(MessageVO message, int position) {
        this.message = message;
        this.position = position;
        setMessageTime(message.message, position);
    }

    /**
     * @param uiMessage 消息
     * @param tag       菜单项 tag
     * @return 返回true，将从context menu中排除
     */
    public abstract boolean contextMenuItemFilter(MessageVO uiMessage, String tag);

    public abstract String contextMenuTitle(Context context, String tag);

    public abstract String contextConfirmPrompt(Context context, String tag);

    public void onViewRecycled() {
        // you can do some clean up here
    }

    protected void setMessageTime(Message item, int position) {
        long msgTime = item.serverTime;
        if (position > 0) {
            Message preMsg = ((ConversationMessageAdapter) adapter).getItem(position - 1).message;
            long preMsgTime = preMsg.serverTime;
            if (msgTime - preMsgTime > (5 * 60 * 1000)) {
                timeTextView.setVisibility(View.VISIBLE);
                timeTextView.setText(TimeUtils.getMsgFormatTime(msgTime));
            } else {
                timeTextView.setVisibility(View.GONE);
            }
        } else {
            timeTextView.setVisibility(View.VISIBLE);
            timeTextView.setText(TimeUtils.getMsgFormatTime(msgTime));
        }
    }

}