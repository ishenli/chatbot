package com.workdance.multimedia.camera;

import static com.workdance.core.util.FileUtils.saveBitmap;
import static com.workdance.multimedia.camera.CameraView.BUTTON_STATE_BOTH;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.workdance.core.BaseActivity;
import com.workdance.multimedia.Config;
import com.workdance.multimedia.R;
import com.workdance.multimedia.camera.listener.CameraListener;

import java.io.File;


public class TakePhotoActivity extends BaseActivity {
    public static final String MODE = "mode";

    private CameraView mCameraView;

    protected int toolbarDisplayType () {
        return TOOLBAR_HIDDEN;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected View contentLayout() {
        return View.inflate(this, R.layout.activity_take_photo, null);
    }

    private void initView() {
        mCameraView = findViewById(R.id.cameraView);
        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath());

        File file = new File(Config.VIDEO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        int mode = getIntent().getIntExtra(MODE, BUTTON_STATE_BOTH);
        mCameraView.setFeatures(mode);
        mCameraView.setSaveVideoPath(Config.VIDEO_SAVE_DIR);
        mCameraView.setCameraListener(new CameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = saveBitmap(bitmap, Config.PHOTO_SAVE_DIR);
                Intent data = new Intent();
                data.putExtra("take_photo", true);
                data.putExtra("path", path);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                // 获取成功录像后的视频路径
                Intent data = new Intent();
                data.putExtra("take_photo", false);
                data.putExtra("path", url);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraView != null) {
            mCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraView != null) {
            mCameraView.onPause();
        }
    }
}
