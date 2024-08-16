

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;


public class InfoDataSourceRefreshed extends Event {

    public static final int REFRESHED_TYPE_PLAY_INFO_FETCHED = 1;
    public static final int REFRESHED_TYPE_SUBTITLE_INFO_FETCHED = 2;
    public static final int REFRESHED_TYPE_MASK_INFO_FETCHED = 3;

    public int mRefreshedType;

    public InfoDataSourceRefreshed() {
        super(PlayerEvent.Info.DATA_SOURCE_REFRESHED);
    }

    public InfoDataSourceRefreshed init(int refreshedType) {
        this.mRefreshedType = refreshedType;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        mRefreshedType = 0;
    }
}
