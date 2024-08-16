

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoVideoSizeChanged extends Event {

    public int videoWidth;
    public int videoHeight;

    public InfoVideoSizeChanged() {
        super(PlayerEvent.Info.VIDEO_SIZE_CHANGED);
    }

    public InfoVideoSizeChanged init(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        videoWidth = 0;
        videoHeight = 0;
    }
}
