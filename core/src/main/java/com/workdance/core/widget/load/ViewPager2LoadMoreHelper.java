package com.workdance.core.widget.load;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPager2LoadMoreHelper implements LoadMoreAble {
    private final ViewPager2 mViewPager;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoadingMore;
    private boolean isLoadMoreEnabled;

    public ViewPager2LoadMoreHelper(ViewPager2 viewPager) {
        this.mViewPager = viewPager;
        this.mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                final RecyclerView.Adapter<?> adapter = mViewPager.getAdapter();
                if (adapter == null) return;
                int count = adapter.getItemCount();
                if (position == count - 2 && !isLoadingMore()) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        isLoadMoreEnabled = enabled;
    }

    @Override
    public boolean isLoadMoreEnabled() {
        return isLoadMoreEnabled;
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    @Override
    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    @Override
    public void showLoadingMore() {
        isLoadingMore = true;
    }

    @Override
    public void dismissLoadingMore() {
        isLoadingMore = false;
    }

    @Override
    public void finishLoadingMore() {
        isLoadingMore = false;
    }
}
