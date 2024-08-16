package com.workdance.multimedia.player.playback.event;

import com.workdance.core.event.Event;
import com.workdance.multimedia.player.playback.PlaybackEvent;

public class ActionPreparePlayback extends Event {

    public ActionPreparePlayback() {
        super(PlaybackEvent.Action.PREPARE_PLAYBACK);
    }
}
