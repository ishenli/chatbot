package com.workdance.multimedia.scene.model;

import android.text.TextUtils;

import com.workdance.core.data.ExtraObject;
import com.workdance.multimedia.player.source.MediaSource;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class VideoItem extends ExtraObject implements Serializable {
    public static final String EXTRA_VIDEO_ITEM = "extra_video_item";
    public static final int SOURCE_TYPE_URL = MediaSource.SOURCE_TYPE_URL;
    public static final int SOURCE_TYPE_VID = MediaSource.SOURCE_TYPE_ID;
    public static final int SOURCE_TYPE_MODEL = MediaSource.SOURCE_TYPE_MODEL;

    private String vid;
    private long duration;
    private String cover;
    private String title;
    private MediaSource mediaSource;
    private int sourceType;
    private int playScene;
    private String url;
    private String urlCacheKey;
    private boolean syncProgress;

    public static VideoItem createVideoItem(String vid, long duration, String cover, String title, String url) {
        VideoItem videoItem = new VideoItem();
        videoItem.vid = vid;
        videoItem.duration = duration;
        videoItem.cover = cover;
        videoItem.title = title;
        videoItem.url = url;
        videoItem.sourceType = SOURCE_TYPE_URL;
        return videoItem;
    }


    public static MediaSource toMediaSource(VideoItem videoItem) {
        if (videoItem.mediaSource == null) {
            videoItem.mediaSource = createMediaSource(videoItem);
        }
        final MediaSource mediaSource = videoItem.mediaSource;
        VideoItem.set(mediaSource, videoItem);
        if (videoItem.syncProgress) {
            mediaSource.setSyncProgressId(videoItem.vid);
        }
        return mediaSource;
    }

    private static MediaSource createMediaSource(VideoItem videoItem) {
        if (videoItem.sourceType == SOURCE_TYPE_URL) {
            MediaSource mediaSource = MediaSource.createUrlSource(videoItem.vid, videoItem.getUrl(), videoItem.urlCacheKey);
            return mediaSource;
        }
        throw new IllegalArgumentException("Unsupported source type: " + videoItem.sourceType);
    }


    public static void set(MediaSource mediaSource, VideoItem videoItem) {
        if (mediaSource == null) return;

        if (!TextUtils.isEmpty(videoItem.cover)) {
            mediaSource.setCoverUrl(videoItem.cover);
        }
        if (videoItem.duration > 0) {
            mediaSource.setDuration(videoItem.duration);
        }
        mediaSource.putExtra(EXTRA_VIDEO_ITEM, videoItem);
    }

    public static void playScene(List<VideoItem> videoItems, int playScene) {
        for (VideoItem videoItem : videoItems) {
            playScene(videoItem, playScene);
        }
    }

    public static void playScene(VideoItem videoItem, int playScene) {
        if (videoItem == null) return;
        videoItem.playScene = playScene;
    }
}

