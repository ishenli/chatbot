package com.workdance.multimedia.player.playback.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;

public class TextureDisplayView extends DisplayView {

    private final android.view.TextureView textureView;
    private TextureSurface ttSurface;
    private SurfaceListener surfaceListener;
    private boolean reuseSurface = false;

    TextureDisplayView(Context context) {
        textureView = new android.view.TextureView(context);
        textureView.setSurfaceTextureListener(new android.view.TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
                if (reuseSurface && ttSurface != null && ttSurface.isValid()) {
                    textureView.setSurfaceTexture(ttSurface.getSurfaceTexture());
                } else {
                    if (ttSurface != null) {
                        ttSurface.releaseDeep();
                    }
                    ttSurface = new TextureSurface(surfaceTexture);
                }
                if (surfaceListener != null) {
                    surfaceListener.onSurfaceAvailable(ttSurface, width, height);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
                if (surfaceListener != null) {
                    surfaceListener.onSurfaceSizeChanged(ttSurface, width, height);
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                if (!reuseSurface) {
                    if (surfaceListener != null) {
                        surfaceListener.onSurfaceDestroy(ttSurface);
                    }
                    ttSurface.releaseDeep();
                    ttSurface = null;
                }
                return !reuseSurface;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
                if (surfaceListener != null) {
                    surfaceListener.onSurfaceUpdated(ttSurface);
                }
            }
        });
    }

    @Override
    public Surface getSurface() {
        return ttSurface;
    }

    @Override
    public void setReuseSurface(boolean reuseSurface) {
        this.reuseSurface = reuseSurface;
        if (!reuseSurface && !textureView.isAttachedToWindow() && ttSurface != null) {
            if (surfaceListener != null) {
                surfaceListener.onSurfaceDestroy(ttSurface);
            }
            ttSurface.releaseDeep();
            ttSurface = null;
        } else {
        }
    }

    @Override
    public boolean isReuseSurface() {
        return reuseSurface;
    }

    @Override
    public int getViewType() {
        return DISPLAY_VIEW_TYPE_TEXTURE_VIEW;
    }

    @Override
    public View getDisplayView() {
        return textureView;
    }

    @Override
    public void setSurfaceListener(SurfaceListener surfaceListener) {
        this.surfaceListener = surfaceListener;
    }

    static class TextureSurface extends Surface {

        private SurfaceTexture mSurfaceTexture;

        @SuppressLint("Recycle")
        TextureSurface(SurfaceTexture surfaceTexture) {
            super(surfaceTexture);
            mSurfaceTexture = surfaceTexture;
        }

        SurfaceTexture getSurfaceTexture() {
            return mSurfaceTexture;
        }

        @Override
        public void release() {
            if (mSurfaceTexture != null) {
                super.release();
                mSurfaceTexture = null;
            }
        }

        public void releaseDeep() {
            if (mSurfaceTexture != null) {
                super.release();
                mSurfaceTexture.release();
                mSurfaceTexture = null;
            }
        }

        @Override
        public boolean isValid() {
            return super.isValid() && mSurfaceTexture != null;
        }
    }
}