

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionStop extends Event {
    public ActionStop() {
        super(PlayerEvent.Action.STOP);
    }
}
