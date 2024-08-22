package com.workdance.chatbot.ui.explore.viewholer;

import android.view.View;
import android.view.ViewStub;

import androidx.annotation.NonNull;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ExploreCircleItemBinding;
import com.workdance.core.widget.image.MultiImageView;

public class ImageViewHolder extends CircleViewHolder {
    public MultiImageView multiImageView;

    public ImageViewHolder(@NonNull ExploreCircleItemBinding binding, @NonNull int viewType) {
        super(binding, viewType);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.explore_cricle_viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImageView);
        if (multiImageView !=null) {
            this.multiImageView = multiImageView;
        }
    }
}
