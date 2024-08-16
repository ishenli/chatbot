

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class StateCompleted extends Event {
    public StateCompleted() {
        super(PlayerEvent.State.COMPLETED);
    }
}
