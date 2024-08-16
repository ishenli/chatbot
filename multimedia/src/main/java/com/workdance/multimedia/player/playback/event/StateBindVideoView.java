package com.workdance.multimedia.player.playback.event;

import com.workdance.core.event.Event;
import com.workdance.multimedia.player.playback.PlaybackEvent;
import com.workdance.multimedia.player.playback.VideoView;

public class StateBindVideoView extends Event {

    public VideoView videoView;

    public StateBindVideoView() {
        super(PlaybackEvent.State.BIND_VIDEO_VIEW);
    }

    public StateBindVideoView init(VideoView videoView) {
        this.videoView = videoView;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.videoView = null;
    }
}
