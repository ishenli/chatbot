package com.workdance.chatbot.ui.multimedia.drama;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.workdance.chatbot.R;

public class DramaMainAdapter extends FragmentStateAdapter {
    private final FragmentActivity mContext;

    public DramaMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.mContext = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new DramaGridCoverFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public String getCurrentItemTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.vevod_mini_drama_main_tab_title_theater);
        } else {
            return mContext.getString(R.string.vevod_mini_drama_main_tab_title_recommend);
        }
    }
}
