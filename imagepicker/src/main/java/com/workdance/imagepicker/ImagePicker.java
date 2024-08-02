package com.workdance.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.workdance.imagepicker.ui.ImageGridActivity;

import java.io.Serializable;

public class ImagePicker implements Serializable {
    public static final int REQUEST_CODE_CHOOSE = 100;
    public static final int REQUEST_CODE_CAMERA = 101;
    public static final int REQUEST_CODE_PREVIEW = 102;
    public static final int REQUEST_CODE_TAKE = 1001;


    public static final String EXTRA_RESULT_ITEMS = "extra_result_items";
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selected_image_position";
    public static final String EXTRA_IMAGE_ITEMS = "extra_image_items";
    public static final String EXTRA_COMPRESS = "extra_compress";

    private boolean multiMode = false;    //图片选择模式
    private int limit = 9;         //最大选择图片数量
    private boolean showCamera = false;   //显示相机

    public static ImagePicker picker() {
        return new ImagePicker();
    }
    private ImagePicker() {}

    public void pick(Activity activity, int resultCode) {
        activity.startActivityForResult(buildPickIntent(activity), resultCode);
    }

    private Intent buildPickIntent(Context context) {
        Intent intent = new Intent(context, ImageGridActivity.class);
        intent.putExtra("multiMode", multiMode);
        intent.putExtra("limit", limit);
        intent.putExtra("showCamera", showCamera);
        return intent;
    }
}
