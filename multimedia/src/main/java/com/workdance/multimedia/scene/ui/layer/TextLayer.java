package com.workdance.multimedia.scene.ui.layer;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.workdance.multimedia.player.playback.VideoLayerHost;
import com.workdance.multimedia.scene.ui.layer.base.BaseLayer;

public class TextLayer extends BaseLayer {
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setText("TextLayer");
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    @Override
    protected void onBindLayerHost(@NonNull VideoLayerHost layerHost) {
        super.onBindLayerHost(layerHost);
        show();
    }
}
