package com.workdance.multimedia.player.source;

import static com.workdance.multimedia.player.source.Track.TRACK_TYPE_AUDIO;
import static com.workdance.multimedia.player.source.Track.TRACK_TYPE_VIDEO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.workdance.core.data.ExtraObject;
import com.workdance.core.util.Asserts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import lombok.Data;

@Data
public class MediaSource extends ExtraObject implements Serializable {
    public static final int SOURCE_TYPE_URL = 0;
    public static final int SOURCE_TYPE_ID = 1;
    public static final int SOURCE_TYPE_MODEL = 2;

    /**
     * Video media type.
     */
    public static final int MEDIA_TYPE_VIDEO = 0;
    /**
     * Audio media type.
     */
    public static final int MEDIA_TYPE_AUDIO = 1;


    private final String uniqueId;
    private String mediaId;
    private String coverUrl;
    private long duration;
    private String videoUrl;
    private String title;
    private final int sourceType;
    private List<Track> tracks;
    private int mediaType;
    private final Map<Integer, List<Track>> tracksMap = new HashMap<>();
    private String syncProgressId;

    public MediaSource(@Nullable String mediaId, int sourceType) {
        this(UUID.randomUUID().toString(), mediaId, sourceType);
    }

    public MediaSource(@NonNull String uniqueId, @Nullable String mediaId, int sourceType) {
        Asserts.checkNotNull(uniqueId);
        this.uniqueId = uniqueId;
        this.mediaId = mediaId == null ? uniqueId : mediaId;
        this.sourceType = sourceType;
    }

    public static MediaSource createUrlSource(String mediaId, String url, @Nullable String cacheKey) {
        MediaSource mediaSource = new MediaSource(mediaId, SOURCE_TYPE_URL);
        Track track = new Track();
        track.setTrackType(TRACK_TYPE_VIDEO);
        track.setUrl(url);
        track.setFileHash(cacheKey);
        mediaSource.setTracks(Arrays.asList(track));
        return mediaSource;
    }

    private void setTracks(List<Track> tracks) {
        setTracks(tracks, (o1, o2) -> Integer.compare(o1.getBitrate(), o2.getBitrate()));
    }

    public void setTracks(List<Track> tracks, Comparator<Track> comparator) {
        synchronized (this) {
            final List<Track> list = new ArrayList<>(tracks);
            if (comparator != null) {
                Collections.sort(list, comparator);
            }
            tracksMap.clear();
            this.tracks = Collections.unmodifiableList(list);
        }
    }

    public static boolean mediaEquals(MediaSource a, MediaSource b) {
        return Objects.equals(a == null ? null : a.mediaId,
                b == null ? null : b.mediaId);
    }

    public static int mediaType2TrackType(@NonNull MediaSource mediaSource) {
        return mediaSource.getMediaType() == MediaSource.MEDIA_TYPE_AUDIO ? TRACK_TYPE_AUDIO : TRACK_TYPE_VIDEO;
    }

    public List<Track> getTracks( int trackType) {
        synchronized (this) {
            if (tracks == null) return null;
            List<Track> result = tracksMap.get(trackType);
            if (result == null) {
                result = new ArrayList<>();
                for (Track track : tracks) {
                    if (track.getTrackType() == trackType) {
                        result.add(track);
                    }
                }
                result = Collections.unmodifiableList(result);
                tracksMap.put(trackType, result);
            }
            return result;
        }
    }
}
