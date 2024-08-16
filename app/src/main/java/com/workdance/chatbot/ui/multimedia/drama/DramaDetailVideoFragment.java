package com.workdance.chatbot.ui.multimedia.drama;

import static com.workdance.chatbot.ui.multimedia.DramaDetailVideoActivityResult.EXTRA_INPUT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.workdance.chatbot.ui.multimedia.DramaDetailVideoActivityResult;
import com.workdance.chatbot.ui.multimedia.DramaDetailVideoActivityResult.DramaDetailVideoInput;
import com.workdance.chatbot.ui.multimedia.model.DramaInfo;
import com.workdance.multimedia.scene.model.VideoItem;
import com.workdance.core.BaseFragment;

public class DramaDetailVideoFragment extends BaseFragment {
    public static final String TAG = "DramaDetailVideoFragment";
    private VideoItem mVideoItem;
    private DramaInfo mDramaInfo;
    private int mEpisodeNumber;
    private boolean mContinuesPlayback;

    @Override
    protected void initViewModel() {
        DramaDetailVideoInput input = parseInput();
        if (input != null) {
            mVideoItem = input.currentVideoItem;
            mDramaInfo = input.dramaInfo;
            if (mDramaInfo != null && mVideoItem != null) {
                mEpisodeNumber = input.episodeNumber;
                mContinuesPlayback = input.continuesPlayback;
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // mSceneView = view.findViewById(R.id.shor)
    }

    private  DramaDetailVideoInput parseInput() {
        Intent intent = requireActivity().getIntent();
        DramaDetailVideoInput input = (DramaDetailVideoActivityResult.DramaDetailVideoInput) intent.getSerializableExtra(EXTRA_INPUT);
        return input;
    }
}
