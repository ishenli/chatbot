package com.workdance.multimedia.camera.state;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.workdance.multimedia.camera.CameraInterface;
import com.workdance.multimedia.camera.CameraView;

class PreviewState implements State {
    public static final String TAG = "PreviewState";

    private CameraMachine machine;

    PreviewState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void start(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
    }

    @Override
    public void stop() {
        CameraInterface.getInstance().doStopPreview();
    }


    @Override
    public void focus(float x, float y, CameraInterface.FocusCallback callback) {
        Log.i(TAG, "preview state foucs");
        if (machine.getView().handlerFoucs(x, y)) {
            CameraInterface.getInstance().handleFocus(machine.getContext(), x, y, callback);
        }
    }

    @Override
    public void switchTo(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().switchCamera(holder, screenProp);
    }

    @Override
    public void restart() {

    }

    @Override
    public void capture() {
        CameraInterface.getInstance().takePicture(new CameraInterface.TakePictureCallback() {
            @Override
            public void captureResult(Bitmap bitmap, boolean isVertical) {
                machine.getView().showPicture(bitmap, isVertical);
                machine.setState(machine.getBorrowPictureState());
                Log.i(TAG, "capture");
            }
        });
    }

    @Override
    public void record(Surface surface, float screenProp) {
        CameraInterface.getInstance().startRecord(surface, screenProp, null);
    }

    @Override
    public void stopRecord(final boolean isShort, long time) {
        CameraInterface.getInstance().stopRecord(isShort, new CameraInterface.StopRecordCallback() {
            @Override
            public void recordResult(String url, Bitmap firstFrame) {
                if (isShort) {
                    machine.getView().resetState(CameraView.TYPE_SHORT);
                } else {
                    machine.getView().playVideo(firstFrame, url);
                    machine.setState(machine.getBorrowVideoState());
                }
            }
        });
    }

    @Override
    public void cancel(SurfaceHolder holder, float screenProp) {
        Log.i(TAG, "浏览状态下,没有 cancle 事件");
    }

    @Override
    public void confirm() {
        Log.i(TAG, "浏览状态下,没有 confirm 事件");
    }

    @Override
    public void zoom(float zoom, int type) {
        Log.i(TAG, "zoom");
        CameraInterface.getInstance().setZoom(zoom, type);
    }

    @Override
    public void flash(String mode) {
        CameraInterface.getInstance().setFlashMode(mode);
    }
}