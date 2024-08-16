

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoAudioRenderingStart extends Event {

    public InfoAudioRenderingStart() {
        super(PlayerEvent.Info.AUDIO_RENDERING_START);
    }
}
