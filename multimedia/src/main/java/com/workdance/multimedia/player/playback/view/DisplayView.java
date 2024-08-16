package com.workdance.multimedia.player.playback.view;

import android.content.Context;
import android.view.Surface;
import android.view.View;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class DisplayView {
    public static final int DISPLAY_VIEW_TYPE_NONE = -1;
    public static final int DISPLAY_VIEW_TYPE_TEXTURE_VIEW = 0;
    public static final int DISPLAY_VIEW_TYPE_SURFACE_VIEW = 1;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DISPLAY_VIEW_TYPE_NONE, DISPLAY_VIEW_TYPE_TEXTURE_VIEW, DISPLAY_VIEW_TYPE_SURFACE_VIEW})
    public @interface DisplayViewType {
    }

    public static DisplayView create(Context context, int viewType) {
        if (viewType == DISPLAY_VIEW_TYPE_SURFACE_VIEW) {
            return new SurfaceDisplayView(context);
        } else {
            // return new DisplayView.TextureDisplayView(context);
            return new TextureDisplayView(context);
        }
    }

    public abstract boolean isReuseSurface();
    public abstract Surface getSurface();
    public abstract int getViewType();
    public abstract void setReuseSurface(boolean reuseSurface);
    public abstract View getDisplayView();
    public abstract void setSurfaceListener(SurfaceListener surfaceListener);static

    public interface SurfaceListener {
        void onSurfaceAvailable(Surface surface, int width, int height);

        void onSurfaceSizeChanged(Surface surface, int width, int height);

        void onSurfaceUpdated(Surface surface);

        void onSurfaceDestroy(Surface surface);
    }
}
