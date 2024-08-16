

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;

public class StateIDLE extends Event {

    public StateIDLE() {
        super(PlayerEvent.State.IDLE);
    }
}
