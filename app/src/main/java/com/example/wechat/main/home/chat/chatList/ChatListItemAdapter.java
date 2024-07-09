package com.example.wechat.main.home.chat.chatList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.databinding.ViewChatListItemBinding;

import java.util.List;

public class ChatListItemAdapter extends RecyclerView.Adapter<ChatListItemAdapter.ItemViewHolder>{

    private final List<ChatListItemVO> items;
    private final OnItemClickListener listener;

    public ChatListItemAdapter(List<ChatListItemVO> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
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

        public void bind(ChatListItemVO item, OnItemClickListener listener) {
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
        void onItemClick(ChatListItemVO item);
    }
}
