

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionSetSpeed extends Event {
    public float speed;

    public ActionSetSpeed() {
        super(PlayerEvent.Action.SET_SPEED);
    }

    @Override
    public void recycle() {
        super.recycle();
        speed = 0;
    }

    public ActionSetSpeed init(float speed) {
        this.speed = speed;
        return this;
    }
}
