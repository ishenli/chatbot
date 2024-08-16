package com.workdance.core.mm;

import android.graphics.Bitmap;

import lombok.Data;

@Data
public class MediaEntry {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    private int type;
    private String mediaUrl;
    private String mediaLocalPath;
    private String thumbnailUrl;
    // TODO 消息里的缩略图会被移除
    private Bitmap thumbnail;
    // private Message message;

    private int width;
    private int height;

    public MediaEntry() {
    }
}
