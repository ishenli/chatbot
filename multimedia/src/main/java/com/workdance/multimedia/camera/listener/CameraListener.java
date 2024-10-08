package com.workdance.multimedia.camera.listener;

import android.graphics.Bitmap;

public interface CameraListener {
    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame);
}
