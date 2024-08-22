package com.workdance.core.widget.image;

public class ImageItem {
    // 内部图片在整个手机界面的位置
    public String url;
    public int w;
    public int h;

    public ImageItem(String url, int width, int height) {
        this.url = url;
        this.w = width;
        this.h = height;
    }
}
