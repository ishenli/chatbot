package com.workdance.multimedia.scene.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.workdance.multimedia.R;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RatioFrameLayout 不是 Android 标准库的一部分，但它是一个自定义的布局容器，通常用于保持其内部视图的宽高比固定。
 * 这种布局对于显示视频、图片或需要特定宽高比的内容特别有用，例如全屏视频播放器或图像预览。
 */
public class RatioFrameLayout extends FrameLayout {
    public static final int RATIO_MODE_FIXED_WIDTH = 0;
    public static final int RATIO_MODE_FIXED_HEIGHT = 1;

    private float mRatio;

    @RatioMode
    private int mRatioMode;

    public RatioFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout, defStyleAttr, 0);
        try {
            mRatioMode = a.getInt(R.styleable.RatioFrameLayout_ratioMode, RATIO_MODE_FIXED_WIDTH);
            mRatio = a.getFloat(R.styleable.RatioFrameLayout_ratio, 0f);
        } finally {
            a.recycle();
        }
    }

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            RATIO_MODE_FIXED_WIDTH,
            RATIO_MODE_FIXED_HEIGHT,
    })
    public @interface RatioMode {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio <= 0 || mRatioMode < 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            if (mRatioMode == RATIO_MODE_FIXED_WIDTH) {
                final int width = MeasureSpec.getSize(widthMeasureSpec);
                final int height = (int) (width / mRatio);
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            } else if (mRatioMode == RATIO_MODE_FIXED_HEIGHT) {
                final int height = MeasureSpec.getSize(heightMeasureSpec);
                final int width = (int) (height * mRatio);
                super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
            } else {
                throw new IllegalArgumentException("unsupported ratio mode! " + mRatioMode);
            }
        }

    }

    /**
     * @param ratio width/height
     */
    public void setRatio(float ratio) {
        if (mRatio != ratio) {
            this.mRatio = ratio;
            requestLayout();
        }
    }

    public float getRatio() {
        return mRatio;
    }

    public int getRatioMode() {
        return mRatioMode;
    }

    public void setRatioMode(@RatioMode int ratioMode) {
        if (mRatioMode != ratioMode) {
            this.mRatioMode = ratioMode;
            requestLayout();
        }
    }
}

