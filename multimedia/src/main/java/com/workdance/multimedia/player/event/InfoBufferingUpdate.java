

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoBufferingUpdate extends Event {

    public int percent;

    public InfoBufferingUpdate() {
        super(PlayerEvent.Info.BUFFERING_UPDATE);
    }

    public InfoBufferingUpdate init(int percent) {
        this.percent = percent;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        percent = 0;
    }
}
