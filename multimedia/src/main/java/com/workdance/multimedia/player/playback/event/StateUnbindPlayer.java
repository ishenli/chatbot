package com.workdance.multimedia.player.playback.event;

import com.workdance.core.event.Event;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.playback.PlaybackEvent;

public class StateUnbindPlayer extends Event {

    public Player player;

    public StateUnbindPlayer() {
        super(PlaybackEvent.State.UNBIND_PLAYER);
    }

    public StateUnbindPlayer init(Player player) {
        this.player = player;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.player = null;
    }
}
