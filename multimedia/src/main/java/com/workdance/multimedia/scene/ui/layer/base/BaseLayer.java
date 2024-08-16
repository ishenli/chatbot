package com.workdance.multimedia.scene.ui.layer.base;

import androidx.annotation.NonNull;

import com.workdance.multimedia.player.playback.VideoLayer;

public abstract class BaseLayer extends VideoLayer {
    public void requestShow(@NonNull String reason) {
        show();
    }

    public void requestDismiss(@NonNull String reason) {
        dismiss();
    }

    public void requestHide(@NonNull String reason) {
        hide();
    }
}
