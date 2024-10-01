package com.workdance.core;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModel;

import com.workdance.core.mvi.ViewModelScope;
import com.workdance.core.util.ViewUtils;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    private final ViewModelScope mViewModelScope = new ViewModelScope();

    public Toolbar toolbar;
    public TextView toolBarText;
    public final static int TOOLBAR_HIDDEN = 1;
    public final static int TOOLBAR_DEFAULT = 2;
    public final static int TOOLBAR_TRANSPARENT = 3;
    public final static int TOOLBAR_LIGHT = 4;
    public final static int TOOLBAR_DARK = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViews();
        setContentView(contentLayout());
        bindViews();
        bindEvents();
        initToolBar();
        afterViews();
    }

    protected void bindViews() {
        toolbar = findViewById(R.id.toolbar);
    }

    protected void bindEvents() {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu() != 0) {
            getMenuInflater().inflate(menu(), menu);
        }
        afterMenus(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideInputMethod();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * @return 布局文件
     */
    protected abstract View contentLayout();

    /**
     * 标题栏标题
     * @return
     */
    protected String toolbarTitle() {
        return null;
    }
    /**
     * @return menu
     */
    protected @MenuRes
    int menu() {
        return 0;
    }

    /**
     * {@link AppCompatActivity#setContentView(int)}之前调用
     */
    protected void beforeViews() {

    }

    /**
     * {@link AppCompatActivity#setContentView(int)}之后调用
     * <p>
     */
    protected void afterViews() {
    }

    /**
     * {@code getMenuInflater().inflate(menu(), menu);}之后调用
     *
     * @param menu
     */
    protected void afterMenus(Menu menu) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInputMethod();
    }


    protected int toolbarDisplayType() {
        return TOOLBAR_DEFAULT;
    }

    protected void addStatusBarHeight(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = ViewUtils.getStatusBarHeight(this);
        view.setLayoutParams(params);
    }

    /**
     * 初始化Toolbar
     */
    private void initToolBar() {
        int displayType = toolbarDisplayType();
        if (displayType == TOOLBAR_HIDDEN) {
            return;
        }

        if (displayType >= TOOLBAR_DEFAULT) {
            // 1.0 显示工具栏并显示文字
            setSupportActionBar(toolbar);
            ((ViewGroup.MarginLayoutParams) toolbar.getLayoutParams()).topMargin = ViewUtils.getStatusBarHeight(this);
            // 1.1 显示工具栏的返回
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            // 1.2 设置工具栏的标题文字
            setToolBarText(toolbarTitle());

            // 2.0 设置主题, 明亮主题
            if (displayType == TOOLBAR_LIGHT) {
                toolbar.getContext().setTheme(R.style.AppTheme_LightAppbar);
                if (toolBarText != null) {
                    toolBarText.setTextColor(Color.BLACK);
                }
            }
            // 2.1 设置主题, 黑暗主题
            if (displayType == TOOLBAR_DARK) {
                toolbar.getContext().setTheme(R.style.AppTheme_DarkAppbar);
                setToolBarTextColor(Color.WHITE);
            }


            // 2.2 设置主题, 透明主题
            if (displayType == TOOLBAR_TRANSPARENT) {
                toolbar.setBackgroundColor(Color.TRANSPARENT);
            }

        }
    }

    /**
     * 设置工具栏颜色
     * @param color
     */
    private void setToolBarTextColor(int color) {
        if (toolbarTitle() != null) {
            toolBarText = toolbar.findViewById(R.id.toolbar_title);
            toolBarText.setTextColor(color);
        } else {
            toolbar.setTitleTextColor(color);
        }
    }

    /**
     * 设置工具栏文字
     * @param title
     */
    private void setToolBarText(String title) {
        if (toolbarTitle() != null) { // 居中标题
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // 清空Toolbar默认标题
            toolBarText = toolbar.findViewById(R.id.toolbar_title);
            // 可以在这里动态设置标题
            toolBarText.setText(title);
        } else {
            ActionBar actionBar = getSupportActionBar(); // 默认的标题
            actionBar.setTitle(title);
        }
    }

    /**
     * 专门用于手动导航栏设置
     * @param showActionBar
     * @param immersiveStatusBar
     * @param title
     * @param bgColor
     * @param textColor
     */
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
            setToolBarText(title);
            setToolBarTextColor(textColor);
            if (toolbar.getNavigationIcon() != null) {
                toolbar.getNavigationIcon().setTint(textColor);
            }
        } else {
            actionBar.hide();
        }
    }


    /**
     * 获取 ActivityScope 的ViewModel
     */
    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getActivityScopeViewModel(this, modelClass);
    }

    /**
     * 获取 ApplicationScope 的ViewModel
     */
    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getApplicationScopeViewModel(modelClass);
    }

    /**
     *********************
     * 权限相关的
     *********************
     */
    public boolean checkPermission(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            granted = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                break;
            }
        }
        return granted;
    }
}
