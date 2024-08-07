package com.workdance.chatbot.ui.multimedia;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.workdance.core.BaseActivity;

public class VideoActivity extends BaseActivity {
    private static final String EXTRA_VIDEO_SCENE = "extra_video_scene";
    private static final String EXTRA_ARGS = "extra_args";

    public static void intentInto(Activity activity, int scene) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_SCENE, scene);
        activity.startActivity(intent);
    }

    @Override
    protected View contentLayout() {
        return null;
    }
}
