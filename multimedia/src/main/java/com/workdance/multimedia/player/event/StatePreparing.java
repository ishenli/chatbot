

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class StatePreparing extends Event {
    public StatePreparing() {
        super(PlayerEvent.State.PREPARING);
    }
}
