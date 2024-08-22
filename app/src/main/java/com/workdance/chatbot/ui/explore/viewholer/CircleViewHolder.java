package com.workdance.chatbot.ui.explore.viewholer;

import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ExploreCircleItemBinding;
import com.workdance.chatbot.model.Circle;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class CircleViewHolder extends RecyclerView.ViewHolder {

    public final static int TYPE_URL = 1;
    public final static int TYPE_TEXT = 2;
    public final static int TYPE_IMAGE = 3;
    public final static int TYPE_VIDEO = 4;
    public final static int TYPE_AUDIO = 5;
    public final static int TYPE_UNKNOWN = 6;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_URL, TYPE_TEXT, TYPE_IMAGE, TYPE_VIDEO, TYPE_AUDIO, TYPE_UNKNOWN})
    public @interface CircleViewType {
    }

    @CircleViewType
    public final int viewType;
    public ExploreCircleItemBinding binding;
    public final TextView contentTextView;
    private final View snsBtn;
    private final View likeBtn;

    public CircleViewHolder(@NonNull ExploreCircleItemBinding binding, @NonNull int viewType) {
        super(binding.getRoot());
        this.binding = binding;
        this.viewType = viewType;
        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
        initSubView(viewType, viewStub);

        // 绑定各种节点
        contentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
        snsBtn = itemView.findViewById(R.id.snsBtn);
        likeBtn = itemView.findViewById(R.id.likeBtn);
    }


    public void bind(Circle item) {
        contentTextView.setText(item.getContent());
        this.binding.setViewModel(item);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);
}
