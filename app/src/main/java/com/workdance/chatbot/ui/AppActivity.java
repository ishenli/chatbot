package com.workdance.chatbot.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.workdance.chatbot.R;
import com.workdance.core.BaseActivity;

public class AppActivity extends BaseActivity {
    @Override
    protected View contentLayout() {
        return this.contentLayout();
    }

    @Override
    protected void setToolbarTheme() {
        toolbar.getContext().setTheme(R.style.AppTheme_LightAppbar);
    }

    @Override
    protected void customToolbarAndStatusBarBackgroundColor(boolean darkTheme) {
        int toolbarBackgroundColorResId = darkTheme ? R.color.color_primary : R.color.bg_hover_gray;
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_back);
        if (darkTheme) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.WHITE);
            }
            toolbar.setTitleTextColor(Color.WHITE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTintList(null);
            }
            if (toolBarText != null) {
                toolBarText.setTextColor(Color.BLACK);
            }
        }
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        if (showHomeMenuItem()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitleBackgroundResource(toolbarBackgroundColorResId, darkTheme);
    }
}
