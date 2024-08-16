

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoBufferingEnd extends Event {

    public int bufferId;

    public InfoBufferingEnd() {
        super(PlayerEvent.Info.BUFFERING_END);
    }

    public InfoBufferingEnd init(int bufferId) {
        this.bufferId = bufferId;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        bufferId = 0;
    }
}
