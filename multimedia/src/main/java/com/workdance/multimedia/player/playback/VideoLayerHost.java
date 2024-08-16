package com.workdance.multimedia.player.playback;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.workdance.multimedia.player.playback.VideoLayer;
import com.workdance.multimedia.player.playback.VideoView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VideoLayerHost {
    private final FrameLayout mHostView;
    private final List<VideoLayer> mLayers = new CopyOnWriteArrayList<>();
    private final List<VideoLayerHostListener> mListeners = new CopyOnWriteArrayList<>();
    private VideoView mVideoView;


    public VideoLayerHost(Context context) {
        mHostView = new FrameLayout(context);
    }

    public void addLayer(VideoLayer layer) {
        if (layer != null && !mLayers.contains(layer)) {
            mLayers.add(layer);
            layer.bindLayerHost(this);
        }
    }

    public void addVideoLayerHostListener(VideoLayerHostListener listener) {
        if (listener != null && !mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeVideoLayerHostListener(VideoLayerHostListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    public VideoView videoView() {
        return mVideoView;
    }

    public FrameLayout hostView() {
        return mHostView;
    }

    public void addLayerView(VideoLayer layer) {
        final ViewGroup hostView = mHostView;
        View layerView = layer.getView();
        if (layerView == null) return;

        if (layerView.getParent() == null) {
            final int layerIndex = indexOfLayer(layer);
            final int layerViewIndex = calViewIndex(hostView, layerIndex);
            hostView.addView(layerView, layerViewIndex);
            layer.onViewAddedToHostView(hostView);
        }
    }

    /**
     * 该函数用于计算一个视图的索引位置。
     * 它接收一个 ViewGroup 类型的宿主视图和一个图层索引作为参数。函数功能是从给定的图层索引开始，
     * 向前遍历寻找上一个有效的图层，并返回该图层在宿主视图中的索引位置加一。如果没有找到有效的图层，则返回0。
     */
    private int calViewIndex(ViewGroup hostView, int layerIndex) {
        int preLayerIndex = layerIndex - 1;
        int preLayerViewIndex = -1;
        for (int i = preLayerIndex; i >= 0; i--) {
            final VideoLayer layer = findLayer(i);
            final View layerView = layer.getView();
            if (layerView != null) {
                int viewIndex = hostView.indexOfChild(layerView);
                if (viewIndex >= 0) {
                    preLayerViewIndex = viewIndex;
                    break;
                }
            }
        }
        return preLayerViewIndex < 0 ? 0 : preLayerViewIndex + 1;
    }

    @Nullable
    public final VideoLayer findLayer(int index) {
        return mLayers.get(index);
    }

    private int indexOfLayer(VideoLayer layer) {
        if (layer != null) {
            return mLayers.indexOf(layer);
        }
        return -1;
    }

    public void removeLayerView(VideoLayer layer) {
        final ViewGroup hostView = mHostView;
        final View layerView = layer.getView();

        if (layerView == null) return;

        final int layerIndex = mLayers.indexOf(layer);
        final int layerViewIndex = hostView.indexOfChild(layerView);
        if (layerViewIndex >= 0) {
            hostView.removeView(layerView);
            layer.onViewRemovedFromHostView(hostView);
        }
    }

    public int indexOfLayerView(VideoLayer layer) {
        final ViewGroup hostView = mHostView;

        View layerView = layer.getView();
        if (layerView == null) return -1;

        return hostView.indexOfChild(layerView);
    }

    public void attachToVideoView(VideoView videoView) {
        if (mVideoView == null) {
            mVideoView = videoView;
            mVideoView.bindLayerHost(this);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mHostView.getLayoutParams();
            if (lp == null) {
                lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;
            }
            videoView.addView(mHostView, lp);
            for (VideoLayerHostListener listener : mListeners) {
                listener.onLayerHostAttachedToVideoView(videoView);
            }
        }
    }


    public interface VideoLayerHostListener {
        void onLayerHostAttachedToVideoView(@NonNull VideoView videoView);
        void onLayerHostDetachedFromVideoView(@NonNull VideoView videoView);
    }
}
