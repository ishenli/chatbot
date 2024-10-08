

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoSeekingStart extends Event {

    public long from;
    public long to;

    public InfoSeekingStart() {
        super(PlayerEvent.Info.SEEKING_START);
    }

    public InfoSeekingStart init(long from, long seekTo) {
        this.from = from;
        this.to = seekTo;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        from = 0;
        to = 0;
    }
}
