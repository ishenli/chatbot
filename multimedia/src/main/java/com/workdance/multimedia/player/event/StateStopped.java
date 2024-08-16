package com.workdance.multimedia.player.event;


import com.workdance.core.event.Event;
import com.workdance.multimedia.player.PlayerEvent;

public class StateStopped extends Event {
    public StateStopped() {
        super(PlayerEvent.State.STOPPED);
    }
}
