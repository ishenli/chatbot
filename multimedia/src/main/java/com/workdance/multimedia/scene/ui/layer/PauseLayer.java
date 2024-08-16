package com.workdance.multimedia.scene.ui.layer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.workdance.core.event.Dispatcher;
import com.workdance.core.event.Event;
import com.workdance.multimedia.R;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.multimedia.player.playback.PlaybackController;
import com.workdance.multimedia.player.playback.PlaybackEvent;
import com.workdance.multimedia.player.playback.VideoView;
import com.workdance.multimedia.scene.ui.layer.base.BaseLayer;

public class PauseLayer extends BaseLayer {
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vevod_pause_layer, parent, false);
        view.setOnClickListener(null);
        view.setClickable(false);
        return view;
    }

    @Override
    public void onVideoViewClick(VideoView view) {
        final Player player = player();
        if (player != null && player.isInPlaybackState()) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        } else {
            startPlayback();
        }

    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(mPlaybackListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(mPlaybackListener);
    }

    private final Dispatcher.EventListener mPlaybackListener = new Dispatcher.EventListener() {
        @Override
        public void onEvent(Event event) {
            switch (event.code()) {
                case PlaybackEvent.Action.START_PLAYBACK:
                case PlayerEvent.Action.START:
                case PlayerEvent.Info.VIDEO_RENDERING_START:
                    dismiss();
                    break;
                case PlayerEvent.Action.STOP:
                case PlayerEvent.Action.RELEASE:
                    dismiss();
                    break;
                case PlayerEvent.Action.PAUSE:
                    show();
                    break;
            }
        }
    };
}
