package com.workdance.chatbot.ui.multimedia.drama;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.FragmentDramaGridCoverBinding;
import com.workdance.chatbot.remote.MediaClient;
import com.workdance.chatbot.ui.multimedia.DramaDetailVideoActivityResult;
import com.workdance.chatbot.ui.multimedia.model.DramaInfo;
import com.workdance.core.BaseFragment;
import com.workdance.core.data.Book;
import com.workdance.core.data.Page;
import com.workdance.core.util.ViewUtils;
import com.workdance.core.widget.load.RecycleViewLoadMoreHelper;

import java.util.List;

public class DramaGridCoverFragment extends BaseFragment {
    private FragmentDramaGridCoverBinding binding;
    private DramaGridCoverAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private final Book<DramaInfo> mBook = new Book<>(12);
    private RecycleViewLoadMoreHelper recycleViewLoadMoreHelper;
    public ActivityResultLauncher<DramaDetailVideoActivityResult.DramaDetailVideoInput> mDramaLauncher = registerForActivityResult(new DramaDetailVideoActivityResult(), result -> {
    });

    @Override
    protected void bindViews(View view) {
        super.bindViews(view);
        mAdapter = new DramaGridCoverAdapter() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                DramaGridCoverAdapter.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

                // 点击视频封面
                holder.itemView.setOnClickListener(v -> {
                    DramaInfo drama = mAdapter.getItem(holder.getAbsoluteAdapterPosition());
                    mDramaLauncher.launch(new DramaDetailVideoActivityResult.DramaDetailVideoInput(drama, 1, false));
                    // Intent intent = new Intent(getActivity(), DramaDetailVideoActivity.class);
                    // intent.putExtra("drama", "xx");
                    // startActivity(intent);
                });

                return holder;
            }
        };

        mRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        ((ViewGroup.MarginLayoutParams) mRefreshLayout.getLayoutParams()).topMargin =
                (int) (ViewUtils.getStatusBarHeight(requireActivity()) // status bar height
                        + ViewUtils.dip2Px(requireActivity(), 44)); // tab bar height

        mRecyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // 添加每一列的边距
        mRecyclerView.addItemDecoration(new DramaGridCoverItemDecoration());
        recycleViewLoadMoreHelper = new RecycleViewLoadMoreHelper(mRecyclerView);
        
        mRefreshLayout.setOnRefreshListener(this::refresh);
        recycleViewLoadMoreHelper.setOnLoadMoreListener(this::loadMore);
        this.refresh();
    }

    private void loadMore() {
        if (mBook.hasMore()) {
            // 正在加载中
            if (recycleViewLoadMoreHelper.isLoadingMore()) return;

            // 分页
            int pageIndex = mBook.nextPageIndex();
            MediaClient.listDrama(pageIndex, mBook.pageSize()).observe(getViewLifecycleOwner(), result -> {
                if (result == null) {
                    Toast.makeText(getActivity(), "请求数据错误", Toast.LENGTH_LONG).show();
                } else {
                    // 这里用 Page 先封装一下
                    Page<DramaInfo> page = new Page<>(result, pageIndex, Page.TOTAL_INFINITY);
                    List<DramaInfo> dramaInfoList = mBook.addPage(page);
                    mAdapter.appendItems(dramaInfoList);
                }
            });

        } else {
            recycleViewLoadMoreHelper.finishLoadingMore();
            mBook.end();
        }
    }

    private void refresh() {
        // 请求 API 获取数据
        MediaClient.listDrama(0, mBook.pageSize()).observe(getViewLifecycleOwner(), result -> {
            if (result == null) {
                Toast.makeText(getActivity(), "请求数据错误", Toast.LENGTH_LONG).show();
            } else {
                // 这里用 Page 先封装一下
                Page<DramaInfo> page = new Page<>(result, 0, Page.TOTAL_INFINITY);
                List<DramaInfo> dramaInfoList = mBook.firstPage(page);
                mAdapter.setItems(dramaInfoList);
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDramaGridCoverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
