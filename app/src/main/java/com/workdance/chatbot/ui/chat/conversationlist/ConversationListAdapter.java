package com.workdance.chatbot.ui.chat.conversationlist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.databinding.ViewChatListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ItemViewHolder>{
    private final Fragment fragment;
    private List<ConversationListItemVO> items = new ArrayList<>();
    private OnItemClickListener listener;

    public ConversationListAdapter(Fragment context) {
        super();
        fragment = context;
    }

    public void setOnClickConversationItemListener(OnItemClickListener onClickConversationItemListener) {
        this.listener = onClickConversationItemListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewChatListItemBinding binding = ViewChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ViewChatListItemBinding binding;

        public ItemViewHolder(ViewChatListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ConversationListItemVO item, OnItemClickListener listener) {
            binding.setChatItemView(item);
            binding.executePendingBindings();
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(ConversationListItemVO item);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setChatList(List<ConversationListItemVO> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
