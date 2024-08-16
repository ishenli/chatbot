package com.workdance.multimedia.player.playback.event;


import com.workdance.core.event.Event;
import com.workdance.multimedia.player.playback.PlaybackEvent;

public class ActionStopPlayback extends Event {
    public ActionStopPlayback() {
        super(PlaybackEvent.Action.STOP_PLAYBACK);
    }
}
