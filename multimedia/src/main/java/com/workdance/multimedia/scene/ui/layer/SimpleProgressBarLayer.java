package com.workdance.multimedia.scene.ui.layer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.workdance.core.event.Dispatcher;
import com.workdance.core.event.Event;
import com.workdance.multimedia.R;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.multimedia.player.event.InfoProgressUpdate;
import com.workdance.multimedia.player.playback.PlaybackController;
import com.workdance.multimedia.player.playback.PlaybackEvent;
import com.workdance.multimedia.player.playback.VideoLayerHost;
import com.workdance.multimedia.player.source.MediaSource;
import com.workdance.multimedia.scene.ui.layer.base.BaseLayer;
import com.workdance.multimedia.scene.ui.widget.MediaSeekBar;

public class SimpleProgressBarLayer extends BaseLayer {
    public static final String ACTION_ENTER_FULLSCREEN = "com.workdance.multimedia.ui.video.layer/enter_full_screen";
    public static final String EXTRA_MEDIA_SOURCE = "extra_media_source";

    private MediaSeekBar mSeekBar;
    private VideoLayerHost mLayerHost;
    private View mFullScreenView;

    @Override
    public void show() {
        super.show();
        syncProgress();
    }

    @Override
    protected View createView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vevod_simple_progress_layer, parent, false);
        mSeekBar = view.findViewById(R.id.mediaSeekBar);
        mSeekBar.setOnSeekListener(new MediaSeekBar.OnUserSeekListener() {

            @Override
            public void onUserSeekStart(long startPosition) {

            }

            @Override
            public void onUserSeekPeeking(long peekPosition) {

            }

            @Override
            public void onUserSeekStop(long startPosition, long seekToPosition) {
                final Player player = player();
                if (player == null) return;

                if (player.isInPlaybackState()) {
                    if (player.isCompleted()) {
                        player.start();
                        player.seekTo(seekToPosition);
                    } else {
                        player.seekTo(seekToPosition);
                    }
                }
            }
        });

        mFullScreenView = view.findViewById(R.id.fullScreen);
        mFullScreenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFullScreenClick(v);
            }
        });

        return view;
    }

    private void onFullScreenClick(View v) {
        MediaSource mediaSource = dataSource();
        if (mediaSource == null) return;

        Intent intent = new Intent();
        intent.setAction(ACTION_ENTER_FULLSCREEN);
        intent.putExtra(EXTRA_MEDIA_SOURCE, mediaSource);
        LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);
    }

    private void syncProgress() {
        final PlaybackController controller = this.controller();
        if (controller != null) {
            final Player player = controller.player();
            if (player != null) {
                if (player.isInPlaybackState()) {
                    setProgress(player.getCurrentPosition(), player.getDuration(), player.getBufferedPercentage());
                }
            }
        }
    }

    private void setProgress(long currentPosition, long duration, int bufferPercent) {
        if (mSeekBar != null) {
            if (duration >= 0) {
                mSeekBar.setDuration(duration);
            }
            if (currentPosition >= 0) {
                mSeekBar.setCurrentPosition(currentPosition);
            }
            if (bufferPercent >= 0) {
                mSeekBar.setCachePercent(bufferPercent);
            }
        }
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(mPlaybackListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(mPlaybackListener);
        dismiss();
    }

    private final Dispatcher.EventListener mPlaybackListener = new Dispatcher.EventListener() {
        @Override
        public void onEvent(Event event) {
            switch (event.code()) {
                case PlaybackEvent.State.BIND_PLAYER:
                    break;
                case PlayerEvent.Action.START:
                    if (event.owner(Player.class).isPaused()) {
                        // animateShow(false);
                    }
                    break;
                case PlayerEvent.State.PREPARED:
                    show();
                    break;
                case PlayerEvent.State.STARTED: {
                    syncProgress();
                    break;
                }
                case PlayerEvent.State.COMPLETED:
                    break;
                case PlayerEvent.Info.PROGRESS_UPDATE: {
                    InfoProgressUpdate e = event.cast(InfoProgressUpdate.class);
                    setProgress(e.currentPosition, e.duration, -1);
                    break;
                }
            }
        }
    };
}
