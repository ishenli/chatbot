

package com.workdance.multimedia.player.event;


import com.workdance.core.event.Event;
import com.workdance.multimedia.player.PlayerEvent;

public class ActionPause extends Event {

    public ActionPause() {
        super(PlayerEvent.Action.PAUSE);
    }
}
