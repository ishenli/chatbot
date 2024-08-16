

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class StatePaused extends Event {
    public StatePaused() {
        super(PlayerEvent.State.PAUSED);
    }
}
