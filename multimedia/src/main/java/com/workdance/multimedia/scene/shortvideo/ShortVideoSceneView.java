package com.workdance.multimedia.scene.shortvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.workdance.core.widget.load.LoadMoreAble;
import com.workdance.core.widget.load.RefreshAble;
import com.workdance.core.widget.load.ViewPager2LoadMoreHelper;

/**
 * 短视频的 View 视图层
 */
public class ShortVideoSceneView extends FrameLayout implements LoadMoreAble, RefreshAble {
    private final ShortVideoPageView mPageView;
    private final SwipeRefreshLayout mSwipeRefreshLayout;
    private OnRefreshListener onRefreshListener;
    private ViewPager2LoadMoreHelper mLoadMoreHelper;
    private OnLoadMoreListener onLoadMoreListener;

    public ShortVideoPageView getPageView() {
        return mPageView;
    }

    public ShortVideoSceneView(@NonNull Context context) {
        this(context, null);
    }
    public ShortVideoSceneView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ShortVideoSceneView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPageView = new ShortVideoPageView(context);
        mSwipeRefreshLayout = new SwipeRefreshLayout(context);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh();
            }
        });

        // 动态添加各个子视图组件
        mSwipeRefreshLayout.addView(mPageView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // 把刷新组件添加到页面中
        addView(mSwipeRefreshLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        // 加载更多的功能,拿到 ViewPager2
        // mLoadMoreHelper = new ViewPager2LoadMoreHelper(mPageView.viewPager);

    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreHelper.setLoadMoreEnabled(enabled);
    }

    @Override
    public boolean isLoadMoreEnabled() {
        return mLoadMoreHelper.isLoadMoreEnabled();
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    @Override
    public boolean isLoadingMore() {
        return mLoadMoreHelper.isLoadingMore();
    }

    @Override
    public void showLoadingMore() {
        mLoadMoreHelper.showLoadingMore();
    }

    @Override
    public void dismissLoadingMore() {
        mLoadMoreHelper.dismissLoadingMore();
    }

    @Override
    public void finishLoadingMore() {
        mLoadMoreHelper.finishLoadingMore();
    }

    @Override
    public void setRefreshEnabled(boolean enabled) {
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    @Override
    public boolean isRefreshEnabled() {
        return mSwipeRefreshLayout.isEnabled();
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        onRefreshListener = listener;
    }

    @Override
    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void showRefreshing() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
