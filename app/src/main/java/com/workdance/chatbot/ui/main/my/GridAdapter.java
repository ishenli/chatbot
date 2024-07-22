package com.workdance.chatbot.ui.main.my;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.databinding.ViewIconItemBinding;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ItemViewHolder>{

    private final List<ServiceItem> items;

    public GridAdapter(List<ServiceItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewIconItemBinding binding = ViewIconItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ViewIconItemBinding binding;

        public ItemViewHolder(ViewIconItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ServiceItem item) {
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }
}
