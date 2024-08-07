package com.workdance.core.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {
    @BindingAdapter("app:svgSource")
    public static void setSvgSource(ImageView imageView, int resourceId) {
        // 根据资源ID设置ImageView的图像
        imageView.setImageResource(resourceId);
    }

    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        // 根据URL设置ImageView的图像
        // 使用Glide加载图片并应用圆角
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(8))) // 设置圆角半径
                .into(imageView);
    }

    @BindingAdapter(value = {"imageSrc", "imageRadius"}, requireAll = false)
    public static void loadImageWithRoundedCorners(ImageView imageView, String url, int imageRadius) {
        // 根据URL设置ImageView的图像
        // 使用Glide加载图片并应用圆角
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(imageRadius))) // 设置圆角半径
                .into(imageView);
    }
}
