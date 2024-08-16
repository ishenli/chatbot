

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class StatePrepared extends Event {

    public StatePrepared() {
        super(PlayerEvent.State.PREPARED);
    }
}
