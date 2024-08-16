

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class StateReleased extends Event {
    public StateReleased() {
        super(PlayerEvent.State.RELEASED);
    }
}
