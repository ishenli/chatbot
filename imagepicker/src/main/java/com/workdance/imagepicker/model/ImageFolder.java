package com.workdance.imagepicker.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class ImageFolder implements Serializable {
    private String name;
    private String path;
    private ImageItem cover; // 文件还的缩略图
    private ArrayList<ImageItem> images;
}
