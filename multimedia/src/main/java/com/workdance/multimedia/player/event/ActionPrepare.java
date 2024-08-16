package com.workdance.multimedia.player.event;

import com.workdance.core.event.Event;
import com.workdance.multimedia.player.source.MediaSource;
import com.workdance.multimedia.player.PlayerEvent;


public class ActionPrepare extends Event {

    public MediaSource mediaSource;

    public ActionPrepare() {
        super(PlayerEvent.Action.PREPARE);
    }

    public ActionPrepare init(MediaSource source) {
        this.mediaSource = source;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        mediaSource = null;
    }
}
