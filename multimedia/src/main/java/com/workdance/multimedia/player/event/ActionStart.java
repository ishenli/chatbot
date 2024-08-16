

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionStart extends Event {
    public ActionStart() {
        super(PlayerEvent.Action.START);
    }
}
