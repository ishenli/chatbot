package com.example.wechat.main.dashboard.assistantList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.databinding.ViewAssistantListItemBinding;
import com.example.wechat.model.UserInfo;

import java.util.List;

import lombok.Setter;

public class AssistantListAdapter extends RecyclerView.Adapter<AssistantListAdapter.ItemViewHolder>{

    @Setter
    private List<UserInfo> items;
    private final OnItemClickListener listener;

    public AssistantListAdapter(List<UserInfo> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewAssistantListItemBinding binding = ViewAssistantListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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

        private final ViewAssistantListItemBinding binding;

        public ItemViewHolder(ViewAssistantListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserInfo item, OnItemClickListener listener) {
            binding.setAssistantItemView(item);
            binding.executePendingBindings();
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(UserInfo item);
    }
}
