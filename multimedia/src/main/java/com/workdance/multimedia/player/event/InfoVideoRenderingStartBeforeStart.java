

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;

public class InfoVideoRenderingStartBeforeStart extends Event {
    public InfoVideoRenderingStartBeforeStart() {
        super(PlayerEvent.Info.VIDEO_RENDERING_START_BEFORE_START);
    }
}
