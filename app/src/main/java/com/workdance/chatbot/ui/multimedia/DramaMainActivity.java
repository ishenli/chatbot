package com.workdance.chatbot.ui.multimedia;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ActivityMmDramaBinding;
import com.workdance.chatbot.ui.multimedia.drama.DramaMainAdapter;
import com.workdance.core.BaseActivity;
import com.workdance.core.util.ViewUtils;
import com.workdance.core.widget.viewpager2.ViewPager2Helper;

public class DramaMainActivity extends BaseActivity {

    private ActivityMmDramaBinding binding;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private DramaMainAdapter mAdapter;

    @Override
    protected void beforeViews() {
        binding = ActivityMmDramaBinding.inflate(getLayoutInflater());
    }

    public static void intentInto(MultimediaHomeActivity activity) {
        Intent intent = new Intent(activity, DramaMainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        Toolbar toolbar = findViewById(R.id.toolbar);
        mTabLayout = findViewById(R.id.tabLayout);

        ((ViewGroup.MarginLayoutParams) toolbar.getLayoutParams()).topMargin = ViewUtils.getStatusBarHeight(this);
        ((ViewGroup.MarginLayoutParams) mTabLayout.getLayoutParams()).topMargin = ViewUtils.getStatusBarHeight(this);

        mViewPager = findViewById(R.id.viewPager);
        ViewPager2Helper.setup(mViewPager);

        mAdapter = new DramaMainAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setCurrentItemTheme(position);
            }
        });
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mAdapter.getCurrentItemTitle(position))).attach();

        setCurrentItem(0);
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
    }

    /**
     * 根据位置调整 tabbar 的显示
     * @param position
     */
    private void setCurrentItemTheme(int position) {
        if (position == 0) {
            setActionBarTheme(
                    true,
                    true,
                    null,
                    Color.TRANSPARENT,
                    getResources().getColor(android.R.color.black));
            mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.mm_drama_main_tab_indicator_light));
        } else {
            setActionBarTheme(
                    true,
                    true,
                    null,
                    Color.TRANSPARENT,
                    getResources().getColor(android.R.color.black));
            mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.mm_drama_main_tab_indicator_dark));
        }
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
        setCurrentItemTheme(position);
    }


    protected void setActionBarTheme(boolean showActionBar,
                                   boolean immersiveStatusBar,
                                   String title,
                                   int bgColor,
                                   int textColor) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        if (showActionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setBackgroundColor(bgColor);
            toolbar.setNavigationIcon(ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.icon_back,
                    getTheme()));
            toolbar.setTitleTextColor(textColor);
            if (toolbar.getNavigationIcon() != null) {
                toolbar.getNavigationIcon().setTint(textColor);
            }
            actionBar.setTitle(title);
        } else {
            actionBar.hide();
        }
    }
}
