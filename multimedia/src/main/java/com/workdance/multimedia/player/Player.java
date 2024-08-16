package com.workdance.multimedia.player;

import android.view.Surface;

import androidx.annotation.IntDef;

import com.workdance.core.event.Dispatcher;
import com.workdance.multimedia.player.source.MediaSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Player {
    int STATE_IDLE = 0;
    int STATE_PREPARING = 1;
    int STATE_PREPARED = 2;
    int STATE_STARTED = 3;
    int STATE_PAUSED = 4;
    int STATE_COMPLETED = 5;
    int STATE_ERROR = 6;
    int STATE_STOPPED = 7;
    int STATE_RELEASED = 8;

    long getCurrentPosition();

    int getState();

    void prepare(MediaSource viewSource);

    void onPrepared();

    boolean isInPlaybackState();

    boolean isCompleted();

    boolean isError();

    boolean isReleased();

    MediaSource getDataSource();

    boolean isStartWhenPrepared();

    void setStartWhenPrepared(boolean startWhenPrepared);

    void start();

    void release();

    boolean isPlaying();

    boolean isPrepared();

    boolean isPreparing();

    void setSurface(Surface surface);

    Surface getSurface();

    void seekTo(long seedTo);

    void pause();

    boolean isPaused();

    long getDuration();

    int getBufferedPercentage();

    long getBufferedDuration();

    int getVideoWidth();

    int getVideoHeight();

    float getVideoSampleAspectRatio();

    String dump();

    void addPlayerListener(Dispatcher.EventListener mPlayerListener);

    void removePlayerListener(Dispatcher.EventListener mPlayerListener);

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_IDLE,
            STATE_PREPARING,
            STATE_PREPARED,
            STATE_STARTED,
            STATE_PAUSED,
            STATE_COMPLETED,
            STATE_ERROR,
            STATE_STOPPED,
            STATE_RELEASED})
    @interface PlayerState {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FRAME_TYPE_UNKNOWN,
            FRAME_TYPE_VIDEO,
            FRAME_TYPE_AUDIO})
    @interface FrameType {
    }

    int FRAME_TYPE_UNKNOWN = 0;
    int FRAME_TYPE_VIDEO = 1;
    int FRAME_TYPE_AUDIO = 2;
}
