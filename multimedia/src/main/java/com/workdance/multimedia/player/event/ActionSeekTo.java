

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class ActionSeekTo extends Event {

    public long from;
    public long to;

    public ActionSeekTo() {
        super(PlayerEvent.Action.SEEK_TO);
    }

    public ActionSeekTo init(long from, long seekTo) {
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
