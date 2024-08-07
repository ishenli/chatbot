package com.workdance.chatbot.ui.main.my;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.databinding.ViewIconItemBinding;
import com.workdance.chatbot.ui.multimedia.MultimediaHomeActivity;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ItemViewHolder>{

    private final List<ServiceItem> items;
    private final Activity mActivity;

    public GridAdapter(Activity activity, List<ServiceItem> items) {
        this.mActivity = activity;
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
        holder.itemView.setOnClickListener(v -> {
            ServiceItem item = items.get(position);
            if (item.getCode().equals(ServiceItem.ServiceItemType.See)) {
                MultimediaHomeActivity.intentInto(this.mActivity);
            }
        });
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
