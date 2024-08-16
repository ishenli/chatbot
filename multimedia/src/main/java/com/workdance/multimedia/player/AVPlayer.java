package com.workdance.multimedia.player;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.workdance.core.data.ExtraObject;
import com.workdance.core.event.Dispatcher;
import com.workdance.core.util.Asserts;
import com.workdance.core.util.LogUtils;
import com.workdance.multimedia.player.cache.CacheKeyFactory;
import com.workdance.multimedia.player.event.ActionPause;
import com.workdance.multimedia.player.event.ActionPrepare;
import com.workdance.multimedia.player.event.ActionRelease;
import com.workdance.multimedia.player.event.ActionStart;
import com.workdance.multimedia.player.event.InfoProgressUpdate;
import com.workdance.multimedia.player.event.StateCompleted;
import com.workdance.multimedia.player.event.StateIDLE;
import com.workdance.multimedia.player.event.StatePaused;
import com.workdance.multimedia.player.event.StatePrepared;
import com.workdance.multimedia.player.event.StatePreparing;
import com.workdance.multimedia.player.event.StateReleased;
import com.workdance.multimedia.player.event.StateStarted;
import com.workdance.multimedia.player.source.MediaSource;
import com.workdance.multimedia.player.source.Track;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 对于 ExoVideoPlayer 的封装
 * 1. 添加了状态机
 * 2. 添加了事件分发
 */
@UnstableApi public class AVPlayer extends ExtraObject implements Player {
    public static final String TAG = "AVPlayer";
    private final Dispatcher mDispatcher;
    private final Context mContext;
    private ExoPlayer mPlayer;
    @Player.PlayerState
    private int mState;
    private MediaSource mMediaSource;
    private Surface mSurface;
    private boolean mStartWhenPrepared;
    private final SparseArray<Track> mSelectedTrack = new SparseArray<>();
    private final SparseArray<Track> mPendingTrack = new SparseArray<>();
    private long mStartTime;
    private boolean mPostProgress;
    private final Handler mHandler;
    private float mVideoSampleAspectRatio;

    public AVPlayer(Context context, MediaSource source, Looper eventLooper) {
        this.mHandler = new Handler(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
        this.mContext = context.getApplicationContext();
        this.mDispatcher = new Dispatcher(eventLooper);
        this.mMediaSource = source;
        this.mPlayer = new ExoPlayer.Builder(context).build();
        this.mPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
        this.mStartWhenPrepared = false;
        setState(Player.STATE_IDLE);
        bindPlayerListener(this.mPlayer);

    }

    private void bindPlayerListener(ExoPlayer mPlayer) {
        Player player = this;
        mPlayer.addListener(new ExoPlayer.Listener() {
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == ExoPlayer.STATE_IDLE) {

                }
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    Log.d(TAG, "onPlayerStateChanged: STATE_BUFFERING");
                }
                if (playbackState == ExoPlayer.STATE_READY) {
                    player.onPrepared();
                }
                if (playbackState == ExoPlayer.STATE_ENDED) {

                }
            }

            @Override
            public void onPositionDiscontinuity(androidx.media3.common.Player.PositionInfo oldPosition, androidx.media3.common.Player.PositionInfo newPosition, int reason) {
                androidx.media3.common.Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
                onCurrentPlaybackTimeUpdate();
            }
        });
    }

    Runnable progressTask = new Runnable() {
        @Override
        public void run() {
            onCurrentPlaybackTimeUpdate();
            if (mPostProgress) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private void onCurrentPlaybackTimeUpdate() {
        long currentPosition = getCurrentPosition();
        onProgressUpdate(currentPosition);
    }


    @Override
    public void setStartWhenPrepared(boolean startWhenPrepared) {
        if (mStartWhenPrepared != startWhenPrepared) {
            mStartWhenPrepared = startWhenPrepared;
            if (isPrepared () && startWhenPrepared) {
                start();
            }
        }
    }

    @Override
    public void onPrepared() {
        if (mPlayer == null) return;
        if (isPreparing()) {
            setState(STATE_PREPARED);
            if (isStartWhenPrepared()) {
                start();
            }
        } else {
            LogUtils.w(this, "onPrepared", "wrong state", dump());
        }
    }


    @Override
    public void prepare(MediaSource source) {
        Asserts.checkState(getState(), Player.STATE_IDLE, Player.STATE_STOPPED);
        // 事件通知
        mDispatcher.obtain(ActionPrepare.class, this).init(source).dispatch();
        mMediaSource = source;

        setState(STATE_PREPARING);
        // prepare 是一个异步操作
        prepareAsync(source);
    }

    /**
     * 针对各种不同的播放源，选择合适的播放轨道
     * @param source
     */
    private void prepareAsync(@NotNull MediaSource source) {
        final int sourceType = source.getSourceType();
        switch (sourceType) {
            case MediaSource.SOURCE_TYPE_ID:
            case MediaSource.SOURCE_TYPE_MODEL:
                break;
            case MediaSource.SOURCE_TYPE_URL:
                prepareDirectUrl(source);
                break;
            default:
                throw new IllegalArgumentException("Unsupported source type: " + sourceType);
        }

    }

    /**
     * 播放在线视频
     * @param source
     */
    private void prepareDirectUrl(MediaSource source) {
        final Track playTrack = selectPlayTrack(source);
        if (playTrack != null) {
            prepareDirectUrl(source, playTrack, isStartWhenPrepared());
        } else {
            throw new IllegalArgumentException("No playable track found");
        }
    }

    /**
     * 调用 exoplayer 完成相关的工作
     * @param source
     * @param playTrack
     */
    private void prepareDirectUrl(MediaSource source, Track playTrack, boolean startWhenPrepared) {

        MediaItem mediaItem = new MediaItem.Builder()
                .setMediaId(source.getMediaId())
                .setUri(playTrack.getUrl())
                .setCustomCacheKey(CacheKeyFactory.DEFAULT.generateCacheKey(source, playTrack))
                .build();

        mPlayer.setMediaItem(mediaItem);

        if (startWhenPrepared && isPrepared()) {
            mPlayer.play();
            // mPlayer.play(); 在 onPrepare 中调用播放
        } else {
            mPlayer.prepare();
        }
    }


    @Override
    public void start() {
        Asserts.checkState(getState(), Player.STATE_PREPARED, Player.STATE_STARTED,
                Player.STATE_PAUSED, Player.STATE_COMPLETED);

        if(isPlaying()) return;

        mDispatcher.obtain(ActionStart.class, this).dispatch();

        if (isInState(Player.STATE_STARTED)) {
            return;
        }

        if (isInState(Player.STATE_PREPARED)) {
            if (mStartWhenPrepared) {
                mPlayer.play();
            }
        } else {
            mPlayer.play();
        }
        setState(STATE_STARTED);
        mDispatcher.obtain(ActionStart.class, this).dispatch();
    }


    @Override
    public long getCurrentPosition() {
        final int state = getState();
        switch (state) {
            case STATE_IDLE:
            case STATE_PREPARING:
                return mStartTime > 0 ? mStartTime : 0;
            case STATE_PREPARED:
                return mPlayer.getCurrentPosition();
            case STATE_ERROR:
                return 0l;

        }
        return mPlayer.getCurrentPosition();
    }

    /**
     * 播放进度更新
     * @param position
     */
    public void onProgressUpdate(long position) {
        // Log.d(TAG, "onProgressUpdate: " + position);
        final long duration = getDuration();
        if (duration > 0 && position > 0) {
            recordProgress();
            notifyProgressUpdate(position, duration);
        }
    }

    private void notifyProgressUpdate(long currentPosition, long duration) {
        currentPosition = Math.max(currentPosition, 0);
        duration = Math.max(duration, 0);
        mDispatcher.obtain(InfoProgressUpdate.class, this).init(currentPosition, duration).dispatch();
    }


    /**
     * 记录播放记录
     */
    private void recordProgress() {
        final MediaSource mediaSource = mMediaSource;
        if (mediaSource == null) return;
        if (isInPlaybackState() && !isCompleted() || isError()) {
            long position = getCurrentPosition();
            long duration = getDuration();
            if (position > 1000 && duration > 0 && position < position - 1000) {
                ProgressRecorder.recordProgress(mediaSource.getMediaId(), position);
            }
        }
    }

    private void clearProgress() {
        final MediaSource mediaSource = mMediaSource;
        if (mediaSource == null) return;
        ProgressRecorder.removeProgress(mediaSource.getSyncProgressId());
    }


    @PlayerState
    @Override
    public int getState() {
        return mState;
    }

    private void setState(@Player.PlayerState int newState) {
        int state;
        synchronized (this) {
            state = this.mState;
            this.mState = newState;
            onStateChange(newState);
        }
    }

    /**
     * AVPlayer 的状态处理
     * @param newState
     */
    public void onStateChange(@Player.PlayerState int newState) {
        switch (newState) {
            case STATE_IDLE:
                mDispatcher.obtain(StateIDLE.class, this).dispatch();
                break;
            case STATE_PREPARING:
                resetProgressAndTime();
                mDispatcher.obtain(StatePreparing.class, this).dispatch();
                break;
            case STATE_PREPARED:
                mDispatcher.obtain(StatePrepared.class, this).dispatch();
                break;
            case STATE_STARTED:
                startProgressTimer();
                mDispatcher.obtain(StateStarted.class, this).dispatch();
                break;
            case STATE_PAUSED:
                mDispatcher.obtain(StatePaused.class, this).dispatch();
                break;
            case STATE_COMPLETED:
                mDispatcher.obtain(StateCompleted.class, this).dispatch();
                break;
            case STATE_STOPPED:
                cancelProgressTimer();
                break;
        }
    }

    private void startProgressTimer() {
        cancelProgressTimer();
        mPostProgress = true;
        mHandler.postDelayed(progressTask, 300);
    }

    private void cancelProgressTimer() {
        mPostProgress = false;
        mHandler.removeCallbacks(progressTask);
    }

    private void resetProgressAndTime() {
    }




    public MediaSource getDataSource() {
        return mMediaSource;
    }

    @Override
    public void setSurface(Surface surface) {
        mSurface = surface;
        mPlayer.setVideoSurface(surface);
    }

    @Override
    public Surface getSurface() {
        return mSurface;
    }

    private Track selectPlayTrack(MediaSource source) {
        final int trackType = MediaSource.mediaType2TrackType(source);
        final List<Track> tracks = source.getTracks(trackType);
        if (tracks != null && !tracks.isEmpty()) {
            Track selected = getSelectedTrack(trackType);
            if (selected == null) {
                selected = tracks.get(0);
                setSelectedTrack(trackType, selected);
                setPendingTrack(trackType, selected);
            } else {
                setPendingTrack(trackType, selected);
            }
            return selected;
        }

        return null;
    }

    private void setPendingTrack(int trackType, Track track) {
        synchronized (this) {
            mPendingTrack.put(trackType, track);
        }
    }

    private void setSelectedTrack(int trackType, Track track) {
        synchronized (this) {
            mSelectedTrack.put(trackType, track);
        }
    }

    public Track getSelectedTrack(int trackType) {
        synchronized (this) {
            return mSelectedTrack.get(trackType);
        }
    }


    @Override
    public boolean isInPlaybackState() {
        switch (mState) {
            case STATE_PREPARED:
            case STATE_STARTED:
            case STATE_PAUSED:
            case STATE_COMPLETED:
                return true;
        }
        return false;
    }

    private void resetInner() {
        mSurface = null;
        mMediaSource = null;
        mStartTime = 0;
        mVideoSampleAspectRatio = 0;
        clearExtras();
    }

    @Override
    public void release() {
        setState(Player.STATE_STOPPED);
        mDispatcher.obtain(ActionRelease.class, this).dispatch();
        recordProgress();
        resetInner();
        if (isInState(Player.STATE_RELEASED)) return;
        mPlayer.release();
        mPlayer = null;
        setState(Player.STATE_RELEASED);
        mDispatcher.obtain(StateReleased.class, this).dispatch();
        mDispatcher.release();
    }

    @Override
    public boolean isReleased() {
        synchronized (this) {
            return mState == STATE_RELEASED;
        }
    }

    @Override
    public boolean isError() {
        synchronized (this) {
            return mState == STATE_ERROR;
        }
    }

    @Override
    public boolean isCompleted() {
        return mState == Player.STATE_COMPLETED;
    }

    @Override
    public boolean isPreparing() {
        synchronized (this) {
            return mState == STATE_PREPARING;
        }
    }

    @Override
    public boolean isPlaying() {
        synchronized (this) {
            return mState == STATE_STARTED;
        }
    }

    @Override
    public boolean isPrepared() {
        synchronized (this) {
            return mState == STATE_PREPARED;
        }
    }

    public boolean isStartWhenPrepared() {
        return mStartWhenPrepared;
    }

    @Override
    public void seekTo(long seedTo) {
        mPlayer.seekTo(seedTo);
    }

    @Override
    public void pause() {
        Asserts.checkState(getState(), Player.STATE_PREPARING, Player.STATE_PREPARED, Player.STATE_STARTED
        , Player.STATE_PAUSED, Player.STATE_COMPLETED);
        mDispatcher.obtain(ActionPause.class, this).dispatch();
        recordProgress();
        mPlayer.pause();
        setState(Player.STATE_PAUSED);

    }

    @Override
    public boolean isPaused() {
        return mState == STATE_PAUSED;
    }

    @Override
    public long getDuration() {
        final int state = getState();
        switch (state) {
            case STATE_PREPARED:
            case STATE_STARTED:
            case STATE_PAUSED:
            case STATE_COMPLETED:
            case STATE_STOPPED:
                return mPlayer.getDuration();
            default:
                return 0;
        }
    }

    @Override
    public int getBufferedPercentage() {
        return mPlayer.getBufferedPercentage();
    }

    @Override
    public long getBufferedDuration() {
        return mPlayer.getTotalBufferedDuration();
    }

    @Override
    public int getVideoWidth() {
        return mPlayer.getVideoSize().width;
    }

    @Override
    public int getVideoHeight() {
        return mPlayer.getVideoSize().height;
    }

    @Override
    public float getVideoSampleAspectRatio() {
        return mVideoSampleAspectRatio;
    }

    @Override
    public String dump() {
        String playInfo = LogUtils.obj2String(mPlayer.getDeviceInfo());
        return String.format("%s %s %s", LogUtils.obj2String(this), mState, playInfo);
    }


    // 挂在 Player 的事件
    @Override
    public void addPlayerListener(Dispatcher.EventListener mPlayerListener) {
        mDispatcher.addEventListener(mPlayerListener);
    }

    @Override
    public void removePlayerListener(Dispatcher.EventListener mPlayerListener) {
        mDispatcher.removeEventListener(mPlayerListener);
    }

    private boolean isInState(int... states) {
        synchronized (this) {
            for (int state : states) {
                if (mState == state) {
                    return true;
                }
            }
            return false;
        }
    }
}
