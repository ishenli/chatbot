
package com.workdance.multimedia.player.event;

import com.workdance.core.event.Event;
import com.workdance.multimedia.player.PlayerEvent;

public class InfoSubtitleFileLoadFinish extends Event {
    public int success;
    public String info;

    public InfoSubtitleFileLoadFinish() {
        super(PlayerEvent.Info.SUBTITLE_FILE_LOAD_FINISH);
    }


    public InfoSubtitleFileLoadFinish init(int success, String info) {
        this.success = success;
        this.info = info;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.success = 0;
        this.info = null;
    }
}
