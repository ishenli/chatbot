package com.workdance.multimedia.player.playback.event;


import com.workdance.core.event.Event;
import com.workdance.multimedia.player.playback.PlaybackEvent;

public class ActionStartPlayback extends Event {

    public ActionStartPlayback() {
        super(PlaybackEvent.Action.START_PLAYBACK);
    }
}
