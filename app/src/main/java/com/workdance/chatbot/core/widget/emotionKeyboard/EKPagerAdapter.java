package com.workdance.chatbot.core.widget.emotionKeyboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class EKPagerAdapter extends FragmentStateAdapter {

    public EKPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return EKPageFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
