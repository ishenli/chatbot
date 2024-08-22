package com.workdance.chatbot.ui.explore.viewholer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.databinding.ExploreCircleHeadBinding;
import com.workdance.chatbot.ui.explore.viewModel.CircleCoverViewModel;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    private ExploreCircleHeadBinding binding;
    public HeaderViewHolder(@NonNull ExploreCircleHeadBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind() {
        binding.executePendingBindings();
    }

    public void bindViewModel(CircleCoverViewModel circleCoverViewModel) {
        binding.setViewModel(circleCoverViewModel);
    }
}
