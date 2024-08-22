package com.workdance.core.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.workdance.core.R;
import com.workdance.core.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiImageView extends LinearLayout {
    public static int MAX_WIDTH = 0;
    private List<ImageItem> imagesList;
    private List<ImageView> imageViewList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    private static int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = ViewUtils.dip2Px(getContext(), 3);


    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && !imagesList.isEmpty()) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 根据布局大小和页面大小计算不同的图片布局
     *
     * @param lists
     * @throws IllegalArgumentException
     */
    public void setList(List<ImageItem> lists) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;

        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; // 解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();
        }

        initView();
    }

    private void initView() {
        this.setOrientation(LinearLayout.VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.isEmpty()) {
            return;
        }

        imageViewList.clear();

        if (imagesList.size() == 1) { // 就一张图片
            ImageView imageView = createImageView(0, false);
            addView(imageView);
            imageViewList.add(imageView);
        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;  // 4张2行显示
            } else {
                MAX_PER_ROW_COUNT = 3; // 超过4张3行显示
            }

            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT == 0 ? 0 : 1);
            for (int row = 0; row < rowCount; row++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(rowPara);
                if (row != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = row < rowCount - 1 ? MAX_PER_ROW_COUNT : allCount - (rowCount - 1) * MAX_PER_ROW_COUNT;

                if (row != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT; // 不是最后一行，默认图片放满
                }
                addView(rowLayout);

                int rowOffset = row * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    ImageView imageView = createImageView(position, true);
                    rowLayout.addView(imageView);
                    imageViewList.add(imageView);
                }

            }
        }
    }

    private ImageView createImageView(int position, final boolean isMultiImage) {
        ImageItem imageItem = imagesList.get(position);
        ImageView imageView = new ImageView(getContext());
        if (isMultiImage) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
        } else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            int expectW = imageItem.w;
            int expectH = imageItem.h;
            if (expectW == 0 || expectH == 0) {
                imageView.setLayoutParams(onePicPara);
            } else {
                int actualW = 0;
                int actualH = 0;
                float scale = ((float) expectH) / ((float) expectW);
                if (expectW > pxOneMaxWandH) {
                    actualW = pxOneMaxWandH;
                    actualH = (int) (actualW * scale);
                } else if (expectW < pxMoreWandH) {
                    actualW = pxMoreWandH;
                    actualH = (int) (actualW * scale);
                } else {
                    actualW = expectW;
                    actualH = expectH;
                }
                imageView.setLayoutParams(new LayoutParams(actualW, actualH));
            }
        }

        imageView.setId(imageItem.url.hashCode());
        imageView.setOnClickListener(new ImageOnClickListener(position));
        imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
        Glide.with(getContext()).load(imageItem.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        return imageView;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicPara = new LayoutParams(wrap, wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private class ImageOnClickListener implements OnClickListener {
        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
