package com.workdance.multimedia.player.source;

import android.media.MediaPlayer;

import androidx.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lombok.Data;

@Data
public class Track implements Serializable {
    public static final int TRACK_TYPE_UNKNOWN = MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_UNKNOWN;
    public static final int TRACK_TYPE_VIDEO = MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_VIDEO;
    public static final int TRACK_TYPE_AUDIO = MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO;

    private int trackType = TRACK_TYPE_UNKNOWN;
    private String url;
    private String fileHash;
    private int bitrate;
    private String fileId;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TRACK_TYPE_UNKNOWN, TRACK_TYPE_VIDEO, TRACK_TYPE_AUDIO})
    public @interface TrackType {
    }
}
