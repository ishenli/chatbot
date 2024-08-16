package com.workdance.multimedia.scene;

import android.view.ViewGroup;

import com.workdance.multimedia.player.playback.VideoView;


public interface VideoViewFactory {
    VideoView createVideoView(ViewGroup parent, Object o);
}
