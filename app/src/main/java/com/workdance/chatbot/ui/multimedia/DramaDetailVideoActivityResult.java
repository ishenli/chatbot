package com.workdance.chatbot.ui.multimedia;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;

import com.workdance.chatbot.ui.multimedia.model.DramaInfo;
import com.workdance.multimedia.scene.model.VideoItem;

import java.io.Serializable;

public class DramaDetailVideoActivityResult extends ActivityResultContract<DramaDetailVideoActivityResult.DramaDetailVideoInput, DramaDetailVideoActivityResult.DramaDetailVideoOutput> {
    public static final String EXTRA_INPUT = "extra_input";
    public static final String EXTRA_OUTPUT = "extra_output";

    @Override
    public Intent createIntent(Context context,  DramaDetailVideoInput input) {
        Intent intent = new Intent(context, DramaDetailVideoActivity.class);
        intent.putExtra(EXTRA_INPUT, input);
        return intent;
    }

    @Override
    public DramaDetailVideoOutput parseResult(int resultCode, Intent intent) {
        if (intent == null) return null;
        return (DramaDetailVideoOutput) intent.getSerializableExtra(EXTRA_OUTPUT);
    }

    public static class DramaDetailVideoInput implements Serializable{
        public VideoItem currentVideoItem;
        public DramaInfo dramaInfo;
        public int episodeNumber;
        public boolean continuesPlayback;

        public DramaDetailVideoInput(DramaInfo dramaInfo, int episodeNumber, boolean continuesPlayback) {
            this.dramaInfo = dramaInfo;
            this.episodeNumber = episodeNumber;
            this.continuesPlayback = continuesPlayback;
            this.currentVideoItem = null;
        }
    }

    public static class DramaDetailVideoOutput implements Serializable {
    }
}
