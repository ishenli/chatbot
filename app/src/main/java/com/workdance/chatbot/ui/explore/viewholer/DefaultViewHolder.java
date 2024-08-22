package com.workdance.chatbot.ui.explore.viewholer;

import android.view.ViewStub;

import com.workdance.chatbot.databinding.ExploreCircleItemBinding;

public class DefaultViewHolder extends CircleViewHolder {

    public DefaultViewHolder(ExploreCircleItemBinding binding, int viewType) {
        super(binding, viewType);
    }


    @Override
    public void initSubView(int viewType, ViewStub viewStub) {

    }
}