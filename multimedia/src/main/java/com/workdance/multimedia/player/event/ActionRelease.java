

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionRelease extends Event {

    public ActionRelease() {
        super(PlayerEvent.Action.RELEASE);
    }
}
