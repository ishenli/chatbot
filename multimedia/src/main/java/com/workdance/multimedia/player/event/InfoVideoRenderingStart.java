

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoVideoRenderingStart extends Event {
    public InfoVideoRenderingStart() {
        super(PlayerEvent.Info.VIDEO_RENDERING_START);
    }
}
