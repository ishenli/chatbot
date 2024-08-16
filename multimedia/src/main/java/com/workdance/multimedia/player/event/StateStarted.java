

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class StateStarted extends Event {
    public StateStarted() {
        super(PlayerEvent.State.STARTED);
    }
}
