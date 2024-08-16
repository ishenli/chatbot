package com.workdance.multimedia.scene.shortvideo;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.multimedia.player.source.MediaSource;
import com.workdance.multimedia.player.playback.VideoView;
import com.workdance.multimedia.scene.VideoViewFactory;
import com.workdance.multimedia.scene.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class ShortVideoAdapter extends RecyclerView.Adapter<ShortVideoAdapter.ViewHolder> {
    private final List<VideoItem> mItems = new ArrayList<>();
    private VideoViewFactory mVideoViewFactory;

    public void setVideoViewFactory(ShortVideoViewFactory videoViewFactory) {
        this.mVideoViewFactory = videoViewFactory;
    }

    @NonNull
    @Override
    public ShortVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(parent, mVideoViewFactory);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ShortVideoAdapter.ViewHolder holder) {
        holder.videoView.stopPlayback();
    }

    @Override
    public void onViewRecycled(@NonNull ShortVideoAdapter.ViewHolder holder) {
        holder.videoView.stopPlayback();
    }

    @Override
    public void onBindViewHolder(@NonNull ShortVideoAdapter.ViewHolder holder, int position) {
        VideoItem item = mItems.get(position);
        holder.bind(position, item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void appendItems(List<VideoItem> videoItems) {
        if (videoItems != null && !videoItems.isEmpty()) {
            int startPosition = mItems.size();
            mItems.addAll(videoItems);
            if (startPosition > 0) {
                notifyItemRangeInserted(startPosition, mItems.size());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void setItems(List<VideoItem> videoItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {

            @Override
            public int getOldListSize() {
                return mItems.size();
            }

            @Override
            public int getNewListSize() {
                return videoItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                VideoItem oldOne = mItems.get(oldItemPosition);
                VideoItem newOne = videoItems.get(newItemPosition);
                return TextUtils.equals(oldOne.getVid(), newOne.getVid());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return true;
            }
        });

        diffResult.dispatchUpdatesTo(new AdapterListUpdateCallback(this));
        mItems.clear();
        mItems.addAll(videoItems);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public FrameLayout videoViewContainer;

        public ViewHolder(@NonNull ViewGroup itemView, VideoViewFactory videoViewFactory) {
            super(itemView);
            itemView.setTag(this);
            videoViewContainer = (FrameLayout) itemView;
            videoView = videoViewFactory.createVideoView(videoViewContainer, null);
        }

        public static ViewHolder create(ViewGroup parent, VideoViewFactory mVideoViewFactory) {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new ViewHolder(frameLayout, mVideoViewFactory);
        }

        public void bind(int position, VideoItem videoItem) {
            // 绑定 VideoView 的播放源
            MediaSource mediaSource = videoView.getDataSource();
            if (mediaSource == null) {
                mediaSource = VideoItem.toMediaSource(videoItem);
                videoView.bindDataSource(mediaSource);
            } else {
                if (!TextUtils.equals(mediaSource.getMediaId(), videoItem.getVid())) {
                    videoView.stopPlayback();
                    mediaSource = VideoItem.toMediaSource(videoItem);
                    videoView.bindDataSource(mediaSource);
                } else {
                    if (videoView.player() == null) {
                        mediaSource = VideoItem.toMediaSource(videoItem);
                        videoView.bindDataSource(mediaSource);
                    }
                }
            }
        }
    }
}
