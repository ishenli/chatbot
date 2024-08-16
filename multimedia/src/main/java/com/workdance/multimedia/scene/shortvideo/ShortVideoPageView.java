package com.workdance.multimedia.scene.shortvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.ViewPager2;

import com.workdance.core.event.Dispatcher;
import com.workdance.core.widget.viewpager2.ViewPager2Helper;
import com.workdance.multimedia.player.playback.PlaybackController;
import com.workdance.multimedia.player.playback.VideoView;
import com.workdance.multimedia.scene.model.PlayScene;
import com.workdance.multimedia.scene.model.VideoItem;

import java.util.List;

/**
 * 视频播放,具备翻页功能
 */
public class ShortVideoPageView extends FrameLayout implements LifecycleEventObserver {
    private static final String TAG = "ShortVideoPageView";

    private final ViewPager2 mViewPager;
    private final ShortVideoAdapter mShortVideoAdapter;
    private final PlaybackController mController = new PlaybackController();
    private Lifecycle mLifeCycle;
    private boolean mInterceptStartPlaybackOnResume;

    public ShortVideoPageView(@NonNull Context context) {
        this(context, null);
    }
    public ShortVideoPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ShortVideoPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewPager = new ViewPager2(context);
        ViewPager2Helper.setup(mViewPager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        // 适配器
        mShortVideoAdapter = new ShortVideoAdapter();
        mShortVideoAdapter.setVideoViewFactory(new ShortVideoViewFactory());
        mViewPager.setAdapter(mShortVideoAdapter);

        // 翻页监听
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                togglePlayback(position);
            }
        });
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void togglePlayback(int position) {
        if (!mLifeCycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            return;
        }

        // VideoItem videoItem = mShortVideoAdapter.getItem(position);
        VideoView videoView = findVideoViewByPosition(mViewPager, position);
        // 判断不同的控制器状态
        if (mController.videoView() == null) {
            if (videoView != null) {
                mController.bind(videoView);
                mController.startPlayback();
            }
        } else {
            if (videoView != null && videoView != mController.videoView()) {
                mController.stopPlayback();
                mController.bind(videoView);
            }
            mController.startPlayback();
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        switch (event){
            case ON_RESUME:
                resume();
                break;
            case ON_PAUSE:
                pause();
                break;
            case ON_DESTROY:
                mLifeCycle.removeObserver(this);
                mLifeCycle = null;
                stop();
                break;
        }
    }


    public void play() {
        int currentPosition = mViewPager.getCurrentItem();
        if (currentPosition >= 0 && mShortVideoAdapter.getItemCount() > 0) {
            Log.d(TAG, "play: ShortVideoPageView play");
            togglePlayback(currentPosition);
        }
    }

    private void pause() {
        mController.pausePlayback();
    }

    public void resume() {
        if (!mInterceptStartPlaybackOnResume) {
            play();
        }
        mInterceptStartPlaybackOnResume = false;
    }

    public void stop() {
        mController.stopPlayback();
    }

    @Nullable
    private static VideoView findVideoViewByPosition(ViewPager2 pager, int position) {
        ShortVideoAdapter.ViewHolder viewHolder = findItemViewHolderByPosition(pager, position);
        return viewHolder == null ? null : viewHolder.videoView;
    }

    private static ShortVideoAdapter.ViewHolder findItemViewHolderByPosition(ViewPager2 pager, int position) {
        View itemView = ViewPager2Helper.findItemViewByPosition(pager, position);
        if (itemView != null) {
            return (ShortVideoAdapter.ViewHolder) itemView.getTag();
        }
        return null;
    }

    public void setLifeCycle(Lifecycle lifeCycle) {
        if (mLifeCycle != lifeCycle) {
            if (mLifeCycle != null) {
                mLifeCycle.removeObserver(this);
            }
            mLifeCycle = lifeCycle;
        }
        if (mLifeCycle != null) {
            mLifeCycle.addObserver(this);
        }
    }

    public void addPlaybackListener(Dispatcher.EventListener listener) {
        mController.addPlaybackListener(listener);
    }

    /**
     * 更新视频列表
     * @param videoItems
     */
    public void setItems(List<VideoItem> videoItems) {
        VideoItem.playScene(videoItems, PlayScene.SCENE_SHORT);
        mShortVideoAdapter.setItems(videoItems);
        ShortVideoStrategy.setItems(videoItems);
        mViewPager.post(this::play);
    }
}
