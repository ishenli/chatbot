

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionSetLooping extends Event {

    public boolean isLooping;

    public ActionSetLooping() {
        super(PlayerEvent.Action.SET_LOOPING);
    }

    @Override
    public void recycle() {
        super.recycle();
        isLooping = false;
    }

    public ActionSetLooping init(boolean isLooping) {
        this.isLooping = isLooping;
        return this;
    }
}
