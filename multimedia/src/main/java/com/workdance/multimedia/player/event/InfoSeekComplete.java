

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoSeekComplete extends Event {
    public InfoSeekComplete() {
        super(PlayerEvent.Info.SEEK_COMPLETE);
    }
}
