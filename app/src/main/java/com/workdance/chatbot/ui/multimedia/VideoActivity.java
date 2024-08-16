package com.workdance.chatbot.ui.multimedia;

import static com.workdance.multimedia.scene.model.PlayScene.SCENE_SHORT;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ActivityVideoBinding;
import com.workdance.chatbot.ui.multimedia.shortvideo.ShortVideoFragment;
import com.workdance.core.BaseActivity;
import com.workdance.core.util.ViewUtils;

public class VideoActivity extends BaseActivity {
    private ActivityVideoBinding binding;
    private static final String EXTRA_VIDEO_SCENE = "extra_video_scene";
    private static final String EXTRA_ARGS = "extra_args";
    private int mScene;

    public static void intentInto(Activity activity, int scene) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_SCENE, scene);
        activity.startActivity(intent);
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        mScene = getIntent().getIntExtra(EXTRA_VIDEO_SCENE, SCENE_SHORT);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        ((ViewGroup.MarginLayoutParams) toolbar.getLayoutParams()).topMargin = ViewUtils.getStatusBarHeight(this);
        final String tag = getTag(mScene);

        // 设置主题和颜色
        setThemeByScene(mScene);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = createFragment(mScene);
            fm.beginTransaction().add(R.id.container, fragment, tag).commit();
        } else {
            fm.beginTransaction().attach(fragment).commit();
        }
    }

    private void setThemeByScene(int mScene) {
        switch (mScene) {
            case SCENE_SHORT: {
                setActionBarTheme(true, false, "短视频", Color.TRANSPARENT, Color.WHITE);
                ViewUtils.setSystemBarTheme(this, Color.TRANSPARENT, true, true, Color.BLACK, false, false);
            }
        }
    }

    private Fragment createFragment(int scene) {
        switch (scene) {
            case SCENE_SHORT: {
                return ShortVideoFragment.newInstance();
            }
        }
        throw new IllegalArgumentException("unsupported " + scene);
    }

    private String getTag(int scene) {
        switch (scene) {
            case SCENE_SHORT:
                return ShortVideoFragment.class.getName();
            // case SCENE_FEED:
            //     return FeedVideoFragment.class.getName();
            // case SCENE_LONG:
            //     return LongVideoFragment.class.getName();
            // case SCENE_DETAIL:
            //     return DetailVideoFragment.class.getName();
            // case SCENE_FULLSCREEN:
            //     return FullScreenVideoFragment.class.getName();
        }
        throw new IllegalArgumentException("unsupported " + scene);
    }
}
