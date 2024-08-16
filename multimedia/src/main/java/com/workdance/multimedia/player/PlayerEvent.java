
package com.workdance.multimedia.player;

public interface PlayerEvent {

    /**
     * Player action event type constants.
     *
     * @see Event#code()
     */
    class Action {
        /**
         * @see com.workdance.multimedia.player.event.ActionSetSurface
         */
        public static final int SET_SURFACE = 1001;
        /**
         * @see com.workdance.multimedia.player.event.ActionPrepare
         */
        public static final int PREPARE = 1002;
        /**
         * @see com.workdance.multimedia.player.event.ActionStart
         */
        public static final int START = 1003;
        /**
         * @see com.workdance.multimedia.player.event.ActionPause
         */
        public static final int PAUSE = 1004;
        /**
         * @see com.workdance.multimedia.player.event.ActionStop
         */
        public static final int STOP = 1005;
        /**
         * @see com.workdance.multimedia.player.event.ActionRelease
         */
        public static final int RELEASE = 1006;
        /**
         * @see com.workdance.multimedia.player.event.ActionSeekTo
         */
        public static final int SEEK_TO = 1007;
        /**
         * @see com.workdance.multimedia.player.event.ActionSetLooping
         */
        public static final int SET_LOOPING = 1008;
        /**
         * @see com.workdance.multimedia.player.event.ActionSetSpeed
         */
        public static final int SET_SPEED = 1009;
    }

    /**
     * Player state event type constants.
     *
     * @see Event#code()
     */
    class State {
        /**
         * @see com.workdance.multimedia.player.event.StateIDLE
         */
        public static final int IDLE = 2001;
        /**
         * @see com.workdance.multimedia.player.event.StatePreparing
         */
        public static final int PREPARING = 2002;
        /**
         * @see com.workdance.multimedia.player.event.StatePrepared
         */
        public static final int PREPARED = 2003;
        /**
         * @see com.workdance.multimedia.player.event.StateStarted
         */
        public static final int STARTED = 2004;
        /**
         * @see com.workdance.multimedia.player.event.StatePaused
         */
        public static final int PAUSED = 2005;
        /**
         * @see com.workdance.multimedia.player.event.StateStopped
         */
        public static final int STOPPED = 2006;
        /**
         * @see com.workdance.multimedia.player.event.StateReleased
         */
        public static final int RELEASED = 2007;
        /**
         * @see com.workdance.multimedia.player.event.StateCompleted
         */
        public static final int COMPLETED = 2008;
        /**
         * @see com.workdance.multimedia.player.event.StateError
         */
        public static final int ERROR = 2009;
    }

    /**
     * Player info event type constants.
     *
     * @see Event#code()
     */
    class Info {
        /**
         * @see com.workdance.multimedia.player.event.InfoDataSourceRefreshed
         */
        public static final int DATA_SOURCE_REFRESHED = 3001;
        /**
         * @see com.workdance.multimedia.player.event.InfoVideoSizeChanged
         */
        public static final int VIDEO_SIZE_CHANGED = 3002;
        /**
         * @see com.workdance.multimedia.player.event.InfoVideoSARChanged
         */
        public static final int VIDEO_SAR_CHANGED = 3003;
        /**
         * @see com.workdance.multimedia.player.event.InfoVideoRenderingStart
         */
        public static final int VIDEO_RENDERING_START = 3004;
        /**
         * @see com.workdance.multimedia.player.event.InfoAudioRenderingStart
         */
        public static final int AUDIO_RENDERING_START = 3005;

        /**
         * @see com.workdance.multimedia.player.event.InfoVideoRenderingStartBeforeStart
         */
        public static final int VIDEO_RENDERING_START_BEFORE_START = 3006;

        /**
         * @see com.workdance.multimedia.player.event.InfoBufferingStart
         */
        public static final int BUFFERING_START = 3007;
        /**
         * @see com.workdance.multimedia.player.event.InfoBufferingEnd
         */
        public static final int BUFFERING_END = 3008;
        /**
         * @see com.workdance.multimedia.player.event.InfoBufferingUpdate
         */
        public static final int BUFFERING_UPDATE = 3009;
        /**
         * @see com.workdance.multimedia.player.event.InfoSeekingStart
         */
        public static final int SEEKING_START = 3010;
        /**
         * @see com.workdance.multimedia.player.event.InfoSeekComplete
         */
        public static final int SEEK_COMPLETE = 3011;
        /**
         * @see com.workdance.multimedia.player.event.InfoProgressUpdate
         */
        public static final int PROGRESS_UPDATE = 3012;
        /**
         * @see com.workdance.multimedia.player.event.InfoTrackInfoReady
         */
        public static final int TRACK_INFO_READY = 3013;
        /**
         * @see com.workdance.multimedia.player.event.InfoTrackWillChange
         */
        public static final int TRACK_WILL_CHANGE = 3014;
        /**
         * @see com.workdance.multimedia.player.event.InfoTrackChanged
         */
        public static final int TRACK_CHANGED = 3015;
        /**
         * @see com.workdance.multimedia.player.event.InfoCacheUpdate
         */
        public static final int CACHE_UPDATE = 3016;

        public static final int SUBTITLE_STATE_CHANGED = 3017;

        public static final int SUBTITLE_LIST_INFO_READY = 3018;

        public static final int SUBTITLE_FILE_LOAD_FINISH = 3019;

        public static final int SUBTITLE_WILL_CHANGE = 3020;

        public static final int SUBTITLE_TEXT_UPDATE = 3021;

        public static final int SUBTITLE_CHANGED = 3022;

        public static final int FRAME_INFO_UPDATE = 3023;
    }
}
