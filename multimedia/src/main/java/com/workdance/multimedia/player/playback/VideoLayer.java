package com.workdance.multimedia.player.playback;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.workdance.core.util.Asserts;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.source.MediaSource;

public abstract class VideoLayer extends VideoView.VideoViewListener.Adapter implements VideoLayerHost.VideoLayerHostListener {
    private View mLayerView;
    private VideoLayerHost mLayerHost;

    protected abstract View createView(@NonNull ViewGroup parent);

    @Nullable
    public final <V extends View> V getView() {
        return (V) mLayerView;
    }

    @Nullable
    public final VideoView videoView() {
        return mLayerHost == null ? null : mLayerHost.videoView();
    }

    @Nullable
    public final Player player() {
        PlaybackController controller = controller();
        return controller == null ? null : controller.player();
    }

    @Nullable
    public final PlaybackController controller() {
        VideoView videoView = videoView();
        return videoView == null ? null : videoView.controller();
    }

    @Nullable
    public final MediaSource dataSource() {
        VideoView videoView = videoView();
        if (videoView != null) {
            return videoView.getDataSource();
        }
        return null;
    }


    public void bindLayerHost(VideoLayerHost layerHost) {
        if (mLayerHost == null) {
            mLayerHost = layerHost;
            // 注册 layerHost 的事件，具体实现在下方
            layerHost.addVideoLayerHostListener(this);
            onBindLayerHost(layerHost);
            final VideoView videoView = layerHost.videoView();
            if (videoView != null) {
                bindVideoView(videoView);
            }
        }
    }

    void unbindLayerHost(VideoLayerHost layerHost) {
        if (mLayerHost != null && mLayerHost == layerHost) {
            VideoView videoView = layerHost.videoView();
            unbindVideoView(videoView);
            layerHost.removeVideoLayerHostListener(this);
            mLayerHost = null;
            onUnbindLayerHost(layerHost);
        }
    }

    void bindVideoView(VideoView videoView) {
        if (videoView != null) {
            videoView.addVideoViewListener(this);
            onBindVideoView(videoView);
            final PlaybackController controller = videoView.controller();
            if (controller != null) {
                bindController(controller);
            }
        }
    }

    void unbindVideoView(VideoView videoView) {
        if (videoView != null) {
            final PlaybackController controller = videoView.controller();
            if (controller != null) {
                unbindController(videoView.controller());
            }
            videoView.removeVideoViewListener(this);
            onUnBindVideoView(videoView);
        }
    }

    private void bindController(PlaybackController controller) {
        if (controller != null) {
            onBindPlaybackController(controller);
        }
    }
    void unbindController(PlaybackController controller) {
        if (controller != null) {
            onUnbindPlaybackController(controller);
        }
    }

    @CallSuper
    public void show() {
        if (isShowing()) return;
        final VideoLayerHost layerHost = mLayerHost;
        if (layerHost == null) return;

        final View layerView = createView(layerHost);

        layerHost.addLayerView(this);

        if (layerView != null && layerView.getVisibility() != View.VISIBLE) {
            layerView.setVisibility(View.VISIBLE);
        }
    }

    public void dismiss() {
        final VideoLayerHost layerHost = mLayerHost;
        if (layerHost == null) return;
        layerHost.removeLayerView(this);
    }

    public void hide() {
        if (!isShowing()) return;
        if (mLayerView != null && mLayerView.getVisibility() != View.GONE) {
            mLayerView.setVisibility(View.GONE);
        }
    }

    public final boolean isShowing() {
        return mLayerView != null
                && mLayerView.getVisibility() == View.VISIBLE
                && mLayerHost != null
                && mLayerHost.indexOfLayerView(this) >= 0;
    }
    @Nullable
    public final View createView() {
        final VideoLayerHost layerHost = mLayerHost;
        if (layerHost == null) return null;

        return createView(layerHost);
    }

    private View createView(VideoLayerHost layerHost) {
        Asserts.checkNotNull(layerHost);
        if (mLayerView == null) {
            ViewGroup hostView = layerHost.hostView();
            final long start = System.currentTimeMillis();
            mLayerView = createView(hostView);
        }
        return mLayerView;
    }

    public final void startPlayback() {
        if (mLayerHost != null) {
            VideoView videoView = mLayerHost.videoView();
            if (videoView != null) {
                videoView.startPlayback();
            }
        }
    }


    // 各种 VideoLayer 提供的钩子
    protected void onBindPlaybackController(PlaybackController controller) {}
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
    }
    protected void onBindVideoView(VideoView videoView) {}
    protected void onUnBindVideoView(VideoView videoView) {}

    protected void onBindLayerHost(@NonNull VideoLayerHost layerHost) {}
    protected void onUnbindLayerHost(@NonNull VideoLayerHost layerHost) {}


    // 下面是各种事件钩子，方法的调用实在 VideoView 侧，此处负责实现
    @Override
    public final void onLayerHostAttachedToVideoView(@NonNull VideoView videoView) {
        bindVideoView(videoView);
    }

    @Override
    public final void onLayerHostDetachedFromVideoView(@NonNull VideoView videoView) {
        unbindVideoView(videoView);
    }

    @Override
    public void onVideoViewBindController(PlaybackController controller) {
        bindController(controller);
    }

    @Override
    public void onVideoViewUnbindController(PlaybackController controller) {
        unbindController(controller);
    }

    // 下面是各种事件钩子，方法的调用实在 VideoLayerHost 侧，此处负责实现
    protected void onViewAddedToHostView(ViewGroup hostView) {
    }

    protected void onViewRemovedFromHostView(ViewGroup hostView) {
    }


}
