package com.workdance.multimedia.player.playback;


import static com.workdance.multimedia.player.source.MediaSource.mediaEquals;

import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.MainThread;

import com.workdance.core.event.Dispatcher;
import com.workdance.core.event.Dispatcher.EventListener;
import com.workdance.core.event.Event;
import com.workdance.core.util.Asserts;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.playback.event.ActionPreparePlayback;
import com.workdance.multimedia.player.playback.event.ActionStartPlayback;
import com.workdance.multimedia.player.playback.event.ActionStopPlayback;
import com.workdance.multimedia.player.playback.event.StateBindPlayer;
import com.workdance.multimedia.player.playback.event.StateBindVideoView;
import com.workdance.multimedia.player.playback.event.StateUnbindPlayer;
import com.workdance.multimedia.player.playback.view.DisplayView;
import com.workdance.multimedia.player.source.MediaSource;

import java.lang.ref.WeakReference;

public class PlaybackController {

    private static final String TAG = "PlaybackController";
    private final SurfaceListener mSurfaceListener;
    private VideoView mVideoView;
    private Player mPlayer;
    private final Dispatcher mDispatcher;
    private PlayerPool mPlayerPool;
    private PlayerListener mPlayerListener;

    public PlaybackController() {
        mPlayerPool = new PlayerPool();
        mDispatcher = new Dispatcher(Looper.getMainLooper());
        mPlayerListener = new PlayerListener(this);
        mSurfaceListener = new SurfaceListener(this);
    }

    public VideoView videoView() {
        return mVideoView;
    }

    public Player player() {
        return mPlayer;
    }

    @MainThread
    public final void startPlayback() {
        startPlayback(true);
    }

    @MainThread
    public final void preparePlayback() {
        startPlayback(false);
    }

    /**
     * 开始播放，功能核心入口
     */
    @MainThread
    public void startPlayback(boolean startWhenPrepared) {
        Log.d(TAG, "startPlayback: PlaybackController");
        Asserts.checkMainThread();
        final VideoView attachedView = mVideoView;
        if (attachedView == null) {
            Log.e(TAG, "startPlayback: VideoView not bind!");
            return;
        }

        // 获取 data source
        final MediaSource attachedSource = attachedView.getDataSource();
        if (attachedSource == null) {
            Log.e(TAG, "startPlayback: MediaSource not bind!");
            return;
        }

        if (startWhenPrepared) {
            mDispatcher.obtain(ActionStartPlayback.class, this).dispatch();
        } else {
            mDispatcher.obtain(ActionPreparePlayback.class, this).dispatch();
        }

        if (mPlayer != null) {
            if (mPlayer.isReleased() || mPlayer.isError()) {
                unbindPlayer(true);
            } else if (!mediaEquals(mPlayer.getDataSource(), attachedSource)) {
                unbindPlayer(true);
            }
        }

        final Player attachedPlayer;
        if (mPlayer == null) {
            attachedPlayer = mPlayerPool.acquire(attachedSource);
            bindPlayer(attachedPlayer);
        } else {
            attachedPlayer = mPlayer;
        }

        Asserts.checkNotNull(attachedPlayer);

        startPlayback(startWhenPrepared, attachedPlayer, attachedView);
    }

    public void startPlayback(boolean startWhenPrepared, Player player, VideoView videoView) {
        Asserts.checkMainThread();
        MediaSource viewSource = videoView.getDataSource();
        final Surface surface = videoView.getSurface();
        if (viewSource == null) return;

        // 绑定 player 到视图
        // player.attachToVideoView(videoView);
        player.setSurface(surface);

        @Player.PlayerState final int playState = player.getState();

        Log.d(TAG, "startPlayback: " + playState);

        switch (playState) {
            case Player.STATE_IDLE:
                // 播放
                player.setStartWhenPrepared(startWhenPrepared);
                player.prepare(viewSource);
                break;
            case Player.STATE_PREPARING: {
                if (!player.isStartWhenPrepared()) {
                    if (startWhenPrepared) {
                        player.setStartWhenPrepared(true);
                    }
                }
                break;
            }
            case Player.STATE_STARTED:
                break;
            case Player.STATE_PREPARED:
            case Player.STATE_PAUSED:
            case Player.STATE_COMPLETED: {
                // 播放
                if (startWhenPrepared) {
                    player.start();
                }
                break;
            }
            case Player.STATE_ERROR:
            case Player.STATE_STOPPED:
            case Player.STATE_RELEASED:
            default:
                throw new IllegalStateException("Invalid playback state: " + playState);
        }
    }

    public void stopPlayback() {
        Asserts.checkMainThread();
        final VideoView attachedView = mVideoView;
        final Player attachedPlayer = mPlayer;

        final MediaSource attachedSource = attachedView.getDataSource();

        if (attachedView != null) {
            attachedView.setReuseSurface(false);
        }

        if (attachedPlayer != null) {
            mDispatcher.obtain(ActionStopPlayback.class, this).dispatch();
            unbindPlayer(true);
        }

    }


    @MainThread
    public void pausePlayback() {
        Asserts.checkMainThread();
        final Player player = mPlayer;
        if (player != null) {
            if (player.isInPlaybackState()) {
                player.pause();
            } else if (player.isPreparing()) {
                player.setStartWhenPrepared(false);
            }
        }
    }

    private void bindPlayer(Player attachedPlayer) {
        if (mPlayer == null && attachedPlayer != null&& !attachedPlayer.isReleased()) {
            mPlayer = attachedPlayer;
            mPlayer.addPlayerListener(mPlayerListener);
            // 触发 layer 的 PlaybackEvent.State.BIND_PLAYER 的事件
            mDispatcher.obtain(StateBindPlayer.class, this).init(attachedPlayer).dispatch();
        }
    }

    private void unbindPlayer(boolean recycle) {
        if (mPlayer != null) {
            if (recycle) {
                mPlayer.setSurface(null);
                mPlayerPool.recycle(mPlayer);
            }
            mPlayer.removePlayerListener(mPlayerListener);
            final Player toUnbind = mPlayer;
            mPlayer = null;
            mDispatcher.obtain(StateUnbindPlayer.class, this).init(toUnbind).dispatch();
        }
    }

    @MainThread
    public void bind(VideoView videoView) {
        Asserts.checkMainThread();
        if (mVideoView != null && mVideoView != videoView) {
            unbindVideoView();
        }
        bindVideoView(videoView);
    }

    @MainThread
    public void unbind() {
        Asserts.checkMainThread();
        unbindPlayer(false);
        unbindVideoView();
    }

    private void bindVideoView(VideoView newVideoView) {
        if (mVideoView == null && newVideoView != null) {
            mVideoView = newVideoView;
            mVideoView.addVideoViewListener(mSurfaceListener);
            mVideoView.bindController(this);
            mDispatcher.obtain(StateBindVideoView.class, this).init(newVideoView).dispatch();
        }
    }

    private void unbindVideoView() {
        if (mVideoView != null) {
            VideoView toUnbind = mVideoView;
            mVideoView = null;
            toUnbind.unbindController(this);
        }
    }

    @MainThread
    public final void addPlaybackListener(EventListener listener) {
        Asserts.checkMainThread();
        mDispatcher.addEventListener(listener);
    }

    @MainThread
    public final void removePlaybackListener(EventListener listener) {
        Asserts.checkMainThread();
        mDispatcher.removeEventListener(listener);
    }

    // 事件接口类
    public static final class PlayerListener implements EventListener {
        private final WeakReference<PlaybackController> controllerRef;

        PlayerListener(PlaybackController controller) {
            this.controllerRef = new WeakReference<>(controller);
        }
        @Override
        public void onEvent(Event event) {
            final PlaybackController controller = controllerRef.get();
            if (controller != null) {
                final Dispatcher dispatcher = controller.mDispatcher;
                if (dispatcher != null) {
                    dispatcher.dispatchEvent(event);
                }
            }
        }
    }

    // 处理 surface 变化
    static final class SurfaceListener extends VideoView.VideoViewListener.Adapter {
        final WeakReference<PlaybackController> controllerRef;

        SurfaceListener(PlaybackController controller) {
            this.controllerRef = new WeakReference<>(controller);
        }

        @Override
        public void onSurfaceAvailable(Surface surface, int width, int height) {
            final PlaybackController controller = controllerRef.get();
            if (controller != null) {
                final Player player = controller.mPlayer;
                if (player != null) {
                    player.setSurface(surface);
                }
            }
        }

        @Override
        public void onSurfaceDestroy(Surface surface) {
            final PlaybackController controller = controllerRef.get();
            if (controller != null) {
                final VideoView videoView = controller.videoView();
                if (videoView == null) return;
                final Player player = controller.player();
                if (player == null) return;

                final int type = videoView.getDisplayViewType();
                switch (type) {
                    case DisplayView.DISPLAY_VIEW_TYPE_SURFACE_VIEW:
                        if (player.getSurface() == surface) {
                            player.setSurface(null);
                        }
                        break;
                    case DisplayView.DISPLAY_VIEW_TYPE_TEXTURE_VIEW:
                        if (!videoView.isReuseSurface()) {
                            if (player.getSurface() == surface) {
                                player.setSurface(null);
                            }
                        }
                        break;
                    case DisplayView.DISPLAY_VIEW_TYPE_NONE:
                        break;
                    default:
                        throw new IllegalStateException("Invalid display view type: " + type);
                }
            }
        }

        @Override
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            final PlaybackController controller = controllerRef.get();
            if (controller != null) {
                if (hasWindowFocus) {
                    final Player player = controller.mPlayer;
                    if (player != null && player.isInPlaybackState() && player.getSurface() != null) {
                        player.setSurface(player.getSurface());
                    }
                }

            }
        }
    }
}
