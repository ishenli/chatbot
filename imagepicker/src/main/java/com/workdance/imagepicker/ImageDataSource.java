package com.workdance.imagepicker;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.workdance.imagepicker.model.ImageFolder;
import com.workdance.imagepicker.model.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageDataSource implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ALL = 0;         // 加载所有图片
    public static final int LOADER_CATEGORY = 1;    // 分类加载图片
    private final FragmentActivity activity;
    private int loaderId;
    private Bundle loaderArgs;

    private ArrayList<ImageFolder> imageFolders = new ArrayList<>();

    private final String[] IMAGE_PROJECTION = {     // 查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   // 图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           // 图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           // 图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          // 图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         // 图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      // 图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};

    private OnImageLoadListener loadedListener;

    public ImageDataSource(FragmentActivity activity, String path, OnImageLoadListener loadedListener) {
        this.activity = activity;
        this.loadedListener = loadedListener;

        LoaderManager loaderManager = LoaderManager.getInstance(activity);
        if (path == null) {
            this.loaderId = LOADER_ALL;
            loaderManager.initLoader(LOADER_ALL, null, this);
            this.loaderArgs = null;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            this.loaderArgs = bundle;
            this.loaderId = LOADER_CATEGORY;
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader cursorLoader = null;
        if (id == LOADER_ALL) {
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        }
        // 扫描某个图片文件夹
        if (id == LOADER_CATEGORY) {
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && !data.isAfterLast()) {
            imageFolders.clear();
            ArrayList<ImageItem> allImages = new ArrayList<>();
            while (data.moveToNext()) {
                // 查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                // 封装实体
                ImageItem imageItem = new ImageItem();
                imageItem.setName(imageName);
                imageItem.setPath(imagePath);
                imageItem.setSize(imageSize);
                imageItem.setWidth(imageWidth);
                imageItem.setHeight(imageHeight);
                imageItem.setMimeType(imageMimeType);
                imageItem.setCreateTime(imageAddTime);
                allImages.add(imageItem);
                // 根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.setName(imageParentFile.getName());
                imageFolder.setPath(imageParentFile.getAbsolutePath());

                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<ImageItem> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.setCover(imageItem);
                    imageFolder.setImages(images);
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).getImages().add(imageItem);
                }
            }

            // 防止没有图片异常
            if (data.getCount() > 0) {
                ImageFolder allImagesFolder = new ImageFolder();
                allImagesFolder.setName("全部图片");
                allImagesFolder.setPath("/");
                allImagesFolder.setCover(allImages.get(0));
                allImagesFolder.setImages(allImages);
                imageFolders.add(0, allImagesFolder);
            }
        }

        // 回调接口，通知图片数据准备完成
        loadedListener.onImageLoad(imageFolders);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImageLoadListener {
        void onImageLoad(List<ImageFolder> imageFolders);
    }
}
