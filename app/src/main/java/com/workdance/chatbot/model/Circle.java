package com.workdance.chatbot.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import lombok.Data;

@Data
public class Circle {
    private String id;
    private String content;
    private String imageUrl;
    private UserInfo user;
    private List<PhotoItem> photos;
    private String gmtCreate;

    @ViewType
    private String viewType;

    @Data
    public static class PhotoItem {
        private String url;
        private int width;
        private int height;
    }

    public final static String TYPE_URL = "1";
    public final static String TYPE_TEXT = "2";
    public final static String TYPE_IMAGE = "3";
    public final static String TYPE_VIDEO = "4";
    public final static String TYPE_AUDIO = "5";
    public final static String TYPE_UNKNOWN = "6";

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_URL, TYPE_IMAGE, TYPE_VIDEO, TYPE_AUDIO})
    public @interface ViewType {
    }
}
