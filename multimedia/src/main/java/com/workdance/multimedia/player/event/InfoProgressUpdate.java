

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoProgressUpdate extends Event {

    public long currentPosition;
    public long duration;

    public InfoProgressUpdate() {
        super(PlayerEvent.Info.PROGRESS_UPDATE);
    }

    public InfoProgressUpdate init(long currentPosition, long duration) {
        this.currentPosition = currentPosition;
        this.duration = duration;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        currentPosition = 0;
        duration = 0;
    }
}
