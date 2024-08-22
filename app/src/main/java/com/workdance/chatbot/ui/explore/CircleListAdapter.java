package com.workdance.chatbot.ui.explore;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.databinding.ExploreCircleHeadBinding;
import com.workdance.chatbot.databinding.ExploreCircleItemBinding;
import com.workdance.chatbot.model.Circle;
import com.workdance.chatbot.ui.explore.viewModel.CircleCoverViewModel;
import com.workdance.chatbot.ui.explore.viewholer.CircleViewHolder;
import com.workdance.chatbot.ui.explore.viewholer.DefaultViewHolder;
import com.workdance.chatbot.ui.explore.viewholer.HeaderViewHolder;
import com.workdance.chatbot.ui.explore.viewholer.ImageViewHolder;
import com.workdance.core.widget.image.ImageItem;
import com.workdance.core.widget.image.MultiImageView;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;

public class CircleListAdapter extends ListAdapter<Circle, RecyclerView.ViewHolder> {
    public final static int TYPE_HEAD = 10;
    public static final int HEADVIEW_SIZE = 1;
    private OnItemClickListener listener;
    private final Context context;
    private CircleCoverViewModel mCircleViewModel;

    public CircleListAdapter(@NonNull DiffUtil.ItemCallback<Circle> diffCallback, Context context) {
        super(diffCallback);
        // this.listener = listener;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        int itemType = 0;
        Circle item = getItem(position);
        if (Circle.TYPE_IMAGE.equals(item.getViewType())) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if (Circle.TYPE_AUDIO.equals(item.getViewType())) {
            itemType = CircleViewHolder.TYPE_AUDIO;
        } else if (Circle.TYPE_URL.equals(item.getViewType())) {
            itemType = CircleViewHolder.TYPE_URL;
        } else if (Circle.TYPE_VIDEO.equals(item.getViewType())) {
            itemType = CircleViewHolder.TYPE_VIDEO;
        }
        return itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return new ItemViewHolder(binding);
        RecyclerView.ViewHolder holder;
        ExploreCircleItemBinding binding = ExploreCircleItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ExploreCircleHeadBinding headBinding = ExploreCircleHeadBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        if (viewType == TYPE_HEAD) { // 处理头部
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(headBinding);
            headerViewHolder.bindViewModel(mCircleViewModel);
            return headerViewHolder;
        }


        if (viewType == CircleViewHolder.TYPE_IMAGE) {
            holder = new ImageViewHolder(binding, viewType);
        } else {
            holder = new DefaultViewHolder(binding, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEAD) {
            return;
        }

        final CircleViewHolder holder = (CircleViewHolder) viewHolder;
        final Circle item = getItem(position);

        // 发送内容有文本
        if (!TextUtils.isEmpty(item.getContent())) {
            holder.contentTextView.setText(item.getContent());
            holder.contentTextView.setVisibility(View.VISIBLE);
        } else {
            holder.contentTextView.setVisibility(View.GONE);
        }
        holder.bind(item);
        
        onBindDeleteBody(holder, item);
        onBindCommentBody(holder, item);

        // 处理 ViewSub 的内容渲染
        switch (holder.viewType) {
            case CircleViewHolder.TYPE_IMAGE:
                if (holder instanceof ImageViewHolder) {
                    List<Circle.PhotoItem> photos = item.getPhotos();
                    if (photos!=null && !photos.isEmpty()) {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                        List<ImageItem> photoList = new ArrayList<>();
                        for (Circle.PhotoItem photo : photos) {
                            photoList.add(new ImageItem(photo.getUrl(), photo.getWidth(), photo.getHeight()));
                        }
                        ((ImageViewHolder) holder).multiImageView.setList(photoList);
                        ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // 打开图片浏览器
                                onBindImagePreviewer(holder, item, position);

                            }
                        });
                    }
                }
            break;
        }
    }

    /**
     * 图片预览
     * @param holder
     * @param item
     */
    private void onBindImagePreviewer(CircleViewHolder holder, Circle item, int position) {
        final List<ImageInfo> imageInfoList = new ArrayList<>();
        for (Circle.PhotoItem photo : item.getPhotos()) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setThumbnailUrl(photo.getUrl());
            imageInfo.setOriginUrl(photo.getUrl());
            imageInfoList.add(imageInfo);
        }
        ImagePreview.getInstance().setContext(context)
                .setImageInfoList(imageInfoList).setIndex(position).start();
    }

    private void onBindCommentBody(CircleViewHolder holder, Circle item) {
    }

    private void onBindDeleteBody(CircleViewHolder holder, Circle item) {
    }

    public void setCircleCoverViewModel(CircleCoverViewModel cycleCoverViewModel) {
        mCircleViewModel = cycleCoverViewModel;
    }

    public interface OnItemClickListener {
        void onItemClick(Circle item);
    }

    public static class DiffCallback extends DiffUtil.ItemCallback<Circle> {
        @Override
        public boolean areItemsTheSame(@NonNull Circle oldItem, @NonNull Circle newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Circle oldItem, @NonNull Circle newItem) {
            return oldItem.getContent().equals(newItem.getContent());
        }
    }
}
