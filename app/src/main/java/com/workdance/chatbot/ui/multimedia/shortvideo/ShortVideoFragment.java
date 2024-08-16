package com.workdance.chatbot.ui.multimedia.shortvideo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.workdance.chatbot.R;
import com.workdance.chatbot.remote.VideoClient;
import com.workdance.core.BaseFragment;
import com.workdance.core.data.Book;
import com.workdance.core.data.Page;
import com.workdance.multimedia.scene.model.VideoItem;
import com.workdance.multimedia.scene.shortvideo.ShortVideoSceneView;

import java.util.List;

public class ShortVideoFragment extends BaseFragment {
    private ShortVideoSceneView mSceneView;
    private final Book<VideoItem> mBook = new Book<>(10);

    public static Fragment newInstance() {
        return new ShortVideoFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_short_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSceneView = (ShortVideoSceneView) view;
        mSceneView.getPageView().setLifeCycle(getLifecycle());
        mSceneView.getPageView().addPlaybackListener(event -> {
            // if (event.code() == PlayerEvent.State.COMPLETED) {
            //     onPlayerStateCompleted(event);
            // }
        });
        mSceneView.setOnRefreshListener(this::refresh);
        refresh();
    }

    /**
     * 获取后台视频数据
     */
    private void refresh() {
        mSceneView.showRefreshing();
        VideoClient.listVideo(0, mBook.pageSize()).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                Page<VideoItem> page = result.getResult();
                if (page != null) {
                    List<VideoItem> videoItems = mBook.firstPage(page);
                    mSceneView.dismissRefreshing();
                    mSceneView.getPageView().setItems(videoItems);
                }
            } else {
                mSceneView.dismissRefreshing();
            }

        });
    }
}

