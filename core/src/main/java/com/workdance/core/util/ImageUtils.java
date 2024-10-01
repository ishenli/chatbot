package com.workdance.core.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;

import com.workdance.core.AppKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    private static final String THUMB_IMG_DIR_PATH = AppKit.getAppKit().getContext().getCacheDir().getAbsolutePath();
    private static final int IMG_WIDTH = 480; //超過此寬、高則會 resize圖片
    private static final int IMG_HEIGHT = 800;
    private static final int COMPRESS_QUALITY = 70; //壓縮 JPEG使用的品質(70代表壓縮率為 30%)


    public static File genThumbImgFile(String srcImgPath) {
        File thumbImgDir = new File(THUMB_IMG_DIR_PATH);
        if (!thumbImgDir.exists()) {
            thumbImgDir.mkdirs();
        }
        String thumbImgName = SystemClock.currentThreadTimeMillis() + FileUtils.getFileNameFromPath(srcImgPath);
        File imageFileThumb = null;

        try {
            InputStream is = new FileInputStream(srcImgPath);
            Bitmap bmpSource = BitmapFactory.decodeStream(is);
            Bitmap bmpTarget = ThumbnailUtils.extractThumbnail(bmpSource, 200, 200, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            if (bmpTarget == null) {
                return null;
            }
            imageFileThumb = new File(thumbImgDir, thumbImgName);
            imageFileThumb.createNewFile();

            FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);

            bmpTarget.compress(Bitmap.CompressFormat.JPEG, 100, fosThumb);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFileThumb;
    }


    /**
     * 儲存图片到相册
     * @param context
     * @param mediaFile
     * @param isImage
     */
    public static void saveMediaToAlbum(Context context, File mediaFile, boolean isImage) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + "-" + mediaFile.getName());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, isImage ? "image/jpg" : "video/mp4");

        // 开始存储
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        try (FileOutputStream fos = (FileOutputStream) contentResolver.openOutputStream(uri);
             FileInputStream fis = new FileInputStream(mediaFile)
        ) {
            fos.getChannel().transferFrom(fis.getChannel(), 0, mediaFile.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentValues.clear();
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0);
        contentResolver.update(uri, contentValues, null, null);
    }
}
