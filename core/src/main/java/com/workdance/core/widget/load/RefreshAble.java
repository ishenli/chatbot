package com.workdance.core.widget.load;

public interface RefreshAble {

    void setRefreshEnabled(boolean enabled);

    boolean isRefreshEnabled();

    void setOnRefreshListener(OnRefreshListener listener);

    boolean isRefreshing();

    void showRefreshing();

    void dismissRefreshing();

    interface OnRefreshListener {
        void onRefresh();
    }
}
