

package com.workdance.multimedia.player.event;

import android.view.Surface;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionSetSurface extends Event {

    public Surface surface;

    public ActionSetSurface() {
        super(PlayerEvent.Action.SET_SURFACE);
    }

    public ActionSetSurface init(Surface surface) {
        this.surface = surface;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        surface = null;
    }
}
