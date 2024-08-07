package com.workdance.core.util;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class DisplayModeHelper {
    private static final String TAG = "DisplayModeHelper";

    /**
     * 画面宽高都充满控件，可能会变形
     */
    public static final int DISPLAY_MODE_DEFAULT = 0;
    /**
     * 画面宽充满控件，高按视频比例适配
     */
    public static final int DISPLAY_MODE_ASPECT_FILL_X = 1;
    /**
     * 画面宽充满控件，高按视频比例适配
     */
    public static final int DISPLAY_MODE_ASPECT_FILL_Y = 2;
    /**
     * 画面长边充满控件，短边按比例适配。保证画面不被裁剪，可能有黑边
     */
    public static final int DISPLAY_MODE_ASPECT_FIT = 3;
    /**
     * 画面短片充满控件，长边按比例适配。画面可能会被裁剪，没有黑边
     */
    public static final int DISPLAY_MODE_ASPECT_FILL = 4;

    private float mDisplayAspectRatio;

    private int mDisplayMode = DISPLAY_MODE_DEFAULT;

    private FrameLayout mContainerView;

    private View mDisplayView;


    public void setDisplayAspectRatio(float displayAspectRatio) {
        this.mDisplayAspectRatio = displayAspectRatio;
        apply();
    }

    public void setDisplayMode(int displayMode) {
        this.mDisplayMode = displayMode;
        apply();
    }

    public void setContainerView(FrameLayout containerView) {
        this.mContainerView = containerView;
        apply();
    }

    public void setDisplayView(View displayView) {
        this.mDisplayView = displayView;
        apply();
    }

    public void apply() {
        if (mDisplayView == null) return;
        mDisplayView.removeCallbacks(applyDisplayMode);
        mDisplayView.postOnAnimation(applyDisplayMode);
    }

    private final Runnable applyDisplayMode = new Runnable() {
        @Override
        public void run() {
            applyDisplayMode();
        }
    };


    private void applyDisplayMode() {
        final View containerView = mContainerView;
        if (containerView == null) {
            return;
        }

        final int containerWidth = containerView.getWidth();
        final int containerHeight = containerView.getHeight();

        final View displayView = mDisplayView;
        if (displayView == null) {
            return;
        }

        final int displayMode = mDisplayMode;
        float displayAspectRatio = mDisplayAspectRatio;

        if (displayAspectRatio <= 0)  return;

        final float containerRatio = (float) containerWidth / containerHeight;

        final int displayGravity = Gravity.CENTER;
        final int displayWidth;
        final int displayHeight;

        switch (displayMode) {
            case DISPLAY_MODE_DEFAULT:
                displayWidth = containerWidth;
                displayHeight = containerHeight;
                break;
            case DISPLAY_MODE_ASPECT_FILL_X:
                displayWidth = containerWidth;
                displayHeight = (int) (containerWidth / displayAspectRatio);
                break;
            case DISPLAY_MODE_ASPECT_FILL_Y:
                displayWidth = (int) (containerHeight * displayAspectRatio);
                displayHeight = containerHeight;
                break;
            case DISPLAY_MODE_ASPECT_FIT:
                if (displayAspectRatio >= containerRatio) {
                    displayWidth = containerWidth;
                    displayHeight = (int) (containerWidth / displayAspectRatio);
                } else {
                    displayWidth = (int) (containerHeight * displayAspectRatio);
                    displayHeight = containerHeight;
                }
                break;
            case DISPLAY_MODE_ASPECT_FILL:
                if (displayAspectRatio >= containerRatio) {
                    displayWidth = (int) (containerHeight * displayAspectRatio);
                    displayHeight = containerHeight;
                } else {
                    displayWidth = containerWidth;
                    displayHeight = (int) (containerWidth / displayAspectRatio);
                }
                break;
            default:
                throw new IllegalArgumentException("unknown displayMode = " + displayMode);
        }

        final FrameLayout.LayoutParams displayLP = (FrameLayout.LayoutParams) displayView.getLayoutParams();
        if (displayLP == null) return;
        if (displayLP.height != displayHeight
                || displayLP.width != displayWidth
                || displayLP.gravity != displayGravity) {
            displayLP.gravity = displayGravity;
            displayLP.width = displayWidth;
            displayLP.height = displayHeight;
            Log.i(TAG, "applyDisplayMode gravity=" +  displayLP.gravity + ",width=" + displayLP.width + ",height=" + displayLP.height);
            displayView.requestLayout();
        }
    }


    public static float calDisplayAspectRatio(int videoWidth, int videoHeight, float sampleAspectRatio) {
        float ratio = calRatio(videoWidth, videoHeight);
        if (sampleAspectRatio > 0) {
            return ratio * sampleAspectRatio;
        }
        return ratio;
    }

    private static float calRatio(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            return videoWidth / (float) videoHeight;
        }
        return 0;
    }

}
