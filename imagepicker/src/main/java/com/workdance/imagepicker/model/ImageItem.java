package com.workdance.imagepicker.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ImageItem implements Serializable {
    private String name;
    private String path;
    private long size;
    private int width;
    private int height;
    private String mimeType;
    private long createTime;
}
