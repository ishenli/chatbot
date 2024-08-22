package com.workdance.chatbot.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.workdance.chatbot.databinding.FragmentExploreCycleBinding;
import com.workdance.chatbot.model.Circle;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.explore.viewModel.CircleCover;
import com.workdance.chatbot.ui.explore.viewModel.CircleCoverViewModel;
import com.workdance.chatbot.ui.explore.viewModel.CircleViewModel;
import com.workdance.chatbot.ui.user.UserViewModel;
import com.workdance.core.BaseFragment;
import com.workdance.core.data.LoadingStatus;
import com.workdance.core.util.SafeObserver;
import com.workdance.core.widget.load.RecycleViewLoadMoreHelper;

import java.util.List;

public class CircleFragment extends BaseFragment {
    public static final String TAG = "CycleFragment";
    private FragmentExploreCycleBinding binding;
    private CircleViewModel cycleViewModel;
    private CircleCoverViewModel cycleCoverViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CircleListAdapter mCycleListAdapter;
    private RecyclerView mRecyclerView;
    private RecycleViewLoadMoreHelper recycleViewLoadMoreHelper;
    private UserViewModel userViewModel;


    @Override
    protected void initViewModel() {
        cycleViewModel = getActivityScopeViewModel(CircleViewModel.class);
        cycleCoverViewModel = getActivityScopeViewModel(CircleCoverViewModel.class);
        userViewModel = getApplicationScopeViewModel(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreCycleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = binding.swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(this::refresh);

        // 处理 RecyclerView
        mRecyclerView = binding.recyclerView;
        mRecyclerView.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setPadding(0, 0, 0, 0);
        // 通过模型的监听，来创建 adapter
        cycleViewModel.getCycleList().observe(getViewLifecycleOwner(), new SafeObserver<List<Circle>>() {
            @Override
            protected void onSafeChanged(List<Circle> cycleList) {
                // 1.0 创建 adapter
                if (mCycleListAdapter == null) {
                    if (cycleList != null) {
                        mCycleListAdapter = new CircleListAdapter(new CircleListAdapter.DiffCallback(), getContext());
                        mCycleListAdapter.setOnItemClickListener(new CircleListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Circle item) {
                                // 点击
                            }
                        });
                        mRecyclerView.setAdapter(mCycleListAdapter);
                        mCycleListAdapter.setCircleCoverViewModel(cycleCoverViewModel);
                        mCycleListAdapter.submitList(cycleList);
                    }
                } else {
                    // 2.0 更新 adapter
                    mCycleListAdapter.submitList(cycleList);
                }
            }
        });

        UserInfo userInfo = userViewModel.getDefaultUser();
        cycleCoverViewModel.setCircleCover(new CircleCover(userInfo.displayName, userInfo.portrait, userInfo.portrait));

        // 加载更多的 Helper
        // recycleViewLoadMoreHelper = new RecycleViewLoadMoreHelper(mRecyclerView);
        // recycleViewLoadMoreHelper.setOnLoadMoreListener(this::loadMore);

        refresh();
    }

    /**
     * 刷新
     */
    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        cycleViewModel.refresh().observe(getViewLifecycleOwner(), loadingStatus -> {
            if (loadingStatus == LoadingStatus.SUCCESS) {
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            } else if(loadingStatus == LoadingStatus.ERROR) {
                Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void loadMore() {
        if (recycleViewLoadMoreHelper.isLoadingMore()) {
            return;
        }
        recycleViewLoadMoreHelper.showLoadingMore();
        cycleViewModel.loadMore().observe(getViewLifecycleOwner(), loadingStatus -> {
            if (loadingStatus == LoadingStatus.SUCCESS) {
                recycleViewLoadMoreHelper.finishLoadingMore();
            } else {
                recycleViewLoadMoreHelper.dismissLoadingMore();
                Toast.makeText(getContext(), "加载更多失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
