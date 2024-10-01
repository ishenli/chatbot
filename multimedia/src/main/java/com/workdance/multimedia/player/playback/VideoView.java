package com.workdance.multimedia.player.playback;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.workdance.core.event.Dispatcher;
import com.workdance.core.event.Event;
import com.workdance.core.util.Asserts;
import com.workdance.core.util.DisplayModeHelper;
import com.workdance.core.util.LogUtils;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.multimedia.player.playback.view.DisplayView;
import com.workdance.multimedia.player.source.MediaSource;
import com.workdance.core.widget.RatioFrameLayout;

import java.util.concurrent.CopyOnWriteArrayList;

public class VideoView extends RatioFrameLayout implements DisplayView.SurfaceListener, Dispatcher.EventListener {
    private boolean mInterceptDispatchClick;
    private PlaybackController mController;
    private MediaSource mMediaSource;
    private GSYVideoPlayer mPlayer;
    private DisplayView mDisplayView;
    private final CopyOnWriteArrayList<VideoViewListener> mListeners = new CopyOnWriteArrayList<>();
    private VideoLayerHost mLayerHost;
    private OnClickListener mOnClickListener;
    private final DisplayModeHelper mDisplayModeHelper;

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDisplayModeHelper = new DisplayModeHelper();
        super.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            if (!mInterceptDispatchClick) {
                for (VideoViewListener listener : mListeners) {
                    listener.onVideoViewClick(VideoView.this);
                }
            }
        });
    }

    public void bindDataSource(@NonNull MediaSource mediaSource) {
        mMediaSource = mediaSource;
        if (mListeners != null) {
            for (VideoViewListener listener : mListeners) {
                listener.onVideoViewBindDataSource(mediaSource);
            }
        }
    }

    public MediaSource getDataSource() {
        return mMediaSource;
    }

    public void bindPlayer(@NonNull GSYVideoPlayer player) {
        mPlayer = player;
    }


    public void setInterceptDispatchClick(boolean interceptClick) {
        this.mInterceptDispatchClick = interceptClick;
    }

    public void startPlayback() {
        if (mController == null) return;
        mController.startPlayback();
    }

    public void pausePlayback() {
        if (mController == null) return;
        mController.pausePlayback();
    }

    public void stopPlayback() {
        if (mController == null) return;
        mController.stopPlayback();
    }

    public GSYVideoPlayer player() {
        return mPlayer;
    }


    public void unbindController(PlaybackController controller) {
        Asserts.checkNotNull(controller);
        if (mController != null && mController == controller) {
            mController = null;
            for (VideoViewListener listener : mListeners) {
                listener.onVideoViewUnbindController(controller);
            }
            controller.removePlaybackListener(this);
        }
    }

    public void bindController(PlaybackController controller) {
        Asserts.checkNotNull(controller);
        if (mController == null) {
            mController = controller;
            mController.addPlaybackListener(this);
            // 执行VideoView 绑定 Controller 成功后的事件
            for (VideoViewListener listener : mListeners) {
                listener.onVideoViewBindController(controller);
            }
        }
    }

    public void loadCover(String url) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(getContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(3000000)
                                .centerCrop())
                .load(url)
                .into(imageView);
        player().setThumbImageView(imageView);
    }

    public Surface getSurface() {
        if (mDisplayView != null) {
            return mDisplayView.getSurface();
        }
        return null;
    }

    public void selectDisplayView(int viewType) {
        DisplayView current = mDisplayView;
        // 已经存在显示视图，且类型不同
        if (current != null && current.getViewType() != viewType) {
            current.setReuseSurface(false);
            removeView(current.getDisplayView());
            mDisplayView = null;
        }

        if (mDisplayView == null) {
            mDisplayView = DisplayView.create(getContext(), viewType);
            mDisplayView.setSurfaceListener(this);
            if (mListeners != null) {
                for (VideoViewListener listener : mListeners) {
                    listener.onVideoViewDisplayViewCreated(mDisplayView.getDisplayView());
                }
            }
            addView(mDisplayView.getDisplayView(), 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            if (mListeners != null) {
                for (VideoViewListener listener : mListeners) {
                    listener.onVideoViewDisplayViewChanged(current == null ? null : current.getDisplayView(),
                            mDisplayView.getDisplayView());
                }
            }
        }
    }


    public void addVideoViewListener(VideoViewListener listener) {
        if (listener != null) {
            mListeners.addIfAbsent(listener);
        }
    }

    public void removeVideoViewListener(VideoViewListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    public PlaybackController controller() {
        return mController;
    }

    public void bindLayerHost(VideoLayerHost layerHost) {
        Asserts.checkNotNull(layerHost);
        if (this.mLayerHost == null) {
            mLayerHost = layerHost;
        }
    }

    public void unbindLayerHost(VideoLayerHost layerHost) {
        Asserts.checkNotNull(layerHost);
        if (this.mLayerHost != null && this.mLayerHost == layerHost) {
            this.mLayerHost = null;
        }
    }

    @Override
    public void onSurfaceAvailable(Surface surface, int width, int height) {
        for (VideoViewListener listener : mListeners) {
            listener.onSurfaceAvailable(surface, width, height);
        }
    }

    @Override
    public void onSurfaceSizeChanged(Surface surface, int width, int height) {
        for (VideoViewListener listener : mListeners) {
            listener.onSurfaceSizeChanged(surface, width, height);
        }
    }

    @Override
    public void onSurfaceUpdated(Surface surface) {

    }

    @Override
    public void onSurfaceDestroy(Surface surface) {
        for (VideoViewListener listener : mListeners) {
            listener.onSurfaceDestroy(surface);
        }
    }

    @Override
    public void onEvent(Event event) {
        LogUtils.d(this, "onEvent", event.toString());
        switch (event.code()) {
            case PlayerEvent.Action.SET_SURFACE:
                final Player player = event.owner(Player.class);
                if (player.isInPlaybackState()) {
                    mDisplayModeHelper.setDisplayAspectRatio(
                            DisplayModeHelper.calDisplayAspectRatio(player.getVideoWidth(), player.getVideoHeight(), player.getVideoSampleAspectRatio())
                    );
                }
        }
    }

    public void setReuseSurface(boolean reuseSurface) {
        if (mDisplayView != null) {
            mDisplayView.setReuseSurface(reuseSurface);
        }
    }

    public void setDisplayMode(int displayMode) {
        final int current = getDisplayMode();
        if (current != displayMode) {
            mDisplayModeHelper.setDisplayMode(displayMode);

            if (mListeners != null) {
                for (VideoViewListener listener : mListeners) {
                    listener.onVideoViewDisplayModeChanged(current, displayMode);
                }
            }
        }
    }

    public int getDisplayMode() {
        return mDisplayModeHelper.getDisplayMode();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDisplayModeHelper.apply();
    }

    @DisplayView.DisplayViewType
    public int getDisplayViewType() {
        if (mDisplayView != null) {
            return mDisplayView.getViewType();
        }
        return DisplayView.DISPLAY_VIEW_TYPE_NONE;
    }

    public boolean isReuseSurface() {
        if (mDisplayView != null) {
            return mDisplayView.isReuseSurface();
        }

        return false;
    }

    public interface ViewEventListener {
        void onConfigurationChanged(Configuration newConfig);

        void onWindowFocusChanged(boolean hasWindowFocus);
    }

    public interface VideoViewListener extends DisplayView.SurfaceListener, ViewEventListener {
        void onVideoViewBindController(PlaybackController controller);

        void onVideoViewUnbindController(PlaybackController controller);

        void onVideoViewBindDataSource(MediaSource dataSource);

        void onVideoViewClick(VideoView videoView);

        void onVideoViewDisplayModeChanged(int oldMode, int newMode);

        void onVideoViewDisplayViewCreated(View displayView);

        void onVideoViewDisplayViewChanged(View oldView, View newView);

        void onVideoViewPlaySceneChanged(int fromScene, int toScene);

        class Adapter implements VideoViewListener {

            @Override
            public void onSurfaceAvailable(Surface surface, int width, int height) {

            }

            @Override
            public void onSurfaceSizeChanged(Surface surface, int width, int height) {

            }

            @Override
            public void onSurfaceUpdated(Surface surface) {

            }

            @Override
            public void onSurfaceDestroy(Surface surface) {

            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {

            }

            @Override
            public void onWindowFocusChanged(boolean hasWindowFocus) {

            }

            @Override
            public void onVideoViewBindController(PlaybackController controller) {

            }

            @Override
            public void onVideoViewUnbindController(PlaybackController controller) {

            }

            @Override
            public void onVideoViewBindDataSource(MediaSource dataSource) {

            }

            @Override
            public void onVideoViewClick(VideoView videoView) {

            }

            @Override
            public void onVideoViewDisplayModeChanged(int oldMode, int newMode) {

            }

            @Override
            public void onVideoViewDisplayViewCreated(View displayView) {

            }

            @Override
            public void onVideoViewDisplayViewChanged(View oldView, View newView) {

            }

            @Override
            public void onVideoViewPlaySceneChanged(int fromScene, int toScene) {

            }
        }
    }
}
