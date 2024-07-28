package com.workdance.chatbot.ui.chat.conversation.message.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.workdance.chatbot.R;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.ui.chat.conversation.ConversationFragment;
import com.workdance.chatbot.ui.chat.conversation.MessageVO;

public abstract class NormalMessageContentViewHolder extends MessageContentViewHolder {
    private TextView nameTextView;
    ImageView portraitImageView;

    public NormalMessageContentViewHolder(@NonNull ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        bindViews(itemView);
        bindEvents(itemView);
    }
    private void bindEvents(View itemView) {

    }

    private void bindViews(View itemView) {
        nameTextView = itemView.findViewById(R.id.nameTextView);
        portraitImageView = itemView.findViewById(R.id.portraitImageView);
    }

    @Override
    public void onBind(MessageVO message, int position) {
        super.onBind(message, position);

        setSenderAvatar(message.message);
        setSenderName(message.message);
        setSendStatus(message.message);
        try {
            onBind(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (message.isFocus) {
//            highlightItem(itemView, message);
        }
    }

    private void setSenderAvatar(Message item) {
        String portraitUrl = item.getAvatar();
        if (portraitImageView != null && portraitUrl != null) {
            Glide
                .with(fragment)
                .load(portraitUrl)
                .transforms(new CenterCrop(), new RoundedCorners(10))
                .placeholder(R.mipmap.avatar_def)
                .into(portraitImageView);
        }

    }

    protected void setSendStatus(Message item) {
        if (item.conversation.type == Conversation.ConversationType.Single) {
            nameTextView.setVisibility(View.GONE);
        } else if (item.conversation.type == Conversation.ConversationType.Group) {
            showGroupMemberAlias(message.message.conversation, message.message, message.message.sender);
        } else {
            // todo
        }
    }



    private void setSenderName(Message item) {
        if (item.conversation.type == Conversation.ConversationType.Single) {
            nameTextView.setVisibility(View.GONE);
        } else if (item.conversation.type == Conversation.ConversationType.Group) {
            showGroupMemberAlias(message.message.conversation, message.message, message.message.sender);
        } else {
            // todo
        }
    }

    private void showGroupMemberAlias(Conversation conversation, Message message, String sender) {
//        UserViewModel userViewModel = new ViewModelProvider(fragment).get(UserViewModel.class);
//        String hideGroupNickName = userViewModel.getUserSetting(UserSettingScope.GroupHideNickname, conversation.target);
//        if ((!TextUtils.isEmpty(hideGroupNickName) && "1".equals(hideGroupNickName)) || message.direction == MessageDirection.Send) {
//            nameTextView.setVisibility(View.GONE);
//            return;
//        }
//        nameTextView.setVisibility(View.VISIBLE);
//        // TODO optimize 缓存userInfo吧
////        if (Conversation.equals(nameTextView.getTag(), sender)) {
////            return;
////        }
//        GroupViewModel groupViewModel = ViewModelProviders.of(fragment).get(GroupViewModel.class);
//
//        nameTextView.setText(groupViewModel.getGroupMemberDisplayNameEx(conversation.target, sender, 11));
        nameTextView.setTag(sender);
    }

    @Override
    public boolean contextMenuItemFilter(MessageVO uiMessage, String tag) {
        return false;
    }

    @Override
    public String contextConfirmPrompt(Context context, String tag) {
        String title = "未设置";
        switch (tag) {
            case MessageContextMenuItemTags.TAG_DELETE:
                title = "确认删除此消息";
                break;
            default:
                break;
        }
        return title;
    }

    @Override
    public String contextMenuTitle(Context context, String tag) {
        String title = "未设置";
        switch (tag) {
            case MessageContextMenuItemTags.TAG_RECALL:
                title = "撤回";
                break;
            case MessageContextMenuItemTags.TAG_DELETE:
                title = "删除";
                break;
            case MessageContextMenuItemTags.TAG_FORWARD:
                title = "转发";
                break;
            case MessageContextMenuItemTags.TAG_QUOTE:
                title = "引用";
                break;
            case MessageContextMenuItemTags.TAG_MULTI_CHECK:
                title = "多选";
                break;
            case MessageContextMenuItemTags.TAG_CHANNEL_PRIVATE_CHAT:
                title = "私聊";
                break;
            case MessageContextMenuItemTags.TAG_FAV:
                title = "收藏";
                break;
            default:
                break;
        }
        return title;
    }

    protected abstract void onBind(MessageVO message);


}
