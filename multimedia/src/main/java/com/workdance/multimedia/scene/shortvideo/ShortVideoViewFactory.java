package com.workdance.multimedia.scene.shortvideo;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.workdance.core.util.DisplayModeHelper;
import com.workdance.multimedia.player.playback.view.DisplayView;
import com.workdance.multimedia.player.playback.VideoLayerHost;
import com.workdance.multimedia.player.playback.VideoView;
import com.workdance.multimedia.scene.VideoViewFactory;
import com.workdance.multimedia.scene.shortvideo.layer.ShortVideoProgressBarLayer;
import com.workdance.multimedia.scene.ui.layer.PauseLayer;

public class ShortVideoViewFactory implements VideoViewFactory {
    @Override
    public VideoView createVideoView(ViewGroup parent, Object o) {
        // StandardGSYVideoPlayer videoView = new StandardGSYVideoPlayer(parent.getContext());
        VideoView videoView = new VideoView(parent.getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // lp.bottomMargin = ViewUtil.dip2Px(parent.getContext(), 12);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        parent.addView(videoView, lp);

        // 添加各种视频播放控件
        VideoLayerHost layerHost = new VideoLayerHost(parent.getContext());
        layerHost.addLayer(new PauseLayer());
        layerHost.addLayer(new ShortVideoProgressBarLayer());
        layerHost.attachToVideoView(videoView);
        videoView.setDisplayMode(DisplayModeHelper.DISPLAY_MODE_ASPECT_FILL);
        videoView.setBackgroundColor(parent.getResources().getColor(android.R.color.black));
        videoView.selectDisplayView(DisplayView.DISPLAY_VIEW_TYPE_TEXTURE_VIEW);

        return videoView;
    }
}
