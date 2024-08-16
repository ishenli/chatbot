package com.workdance.multimedia.player.playback.view;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;

public class SurfaceDisplayView extends DisplayView {
    android.view.SurfaceView surfaceView;
    private SurfaceListener surfaceListener;

    public SurfaceDisplayView(Context context) {
        this.surfaceView = new SurfaceView(context);
    }

    @Override
    public int getViewType() {
        return DISPLAY_VIEW_TYPE_SURFACE_VIEW;
    }

    @Override
    public void setReuseSurface(boolean reuseSurface) {
    }
    @Override
    public boolean isReuseSurface() {
        return false;
    }

    @Override
    public Surface getSurface() {
        return surfaceView.getHolder().getSurface();
    }

    @Override
    public View getDisplayView() {
        return surfaceView;
    }


    @Override
    public void setSurfaceListener(SurfaceListener surfaceListener) {
        this.surfaceListener = surfaceListener;
    }
}