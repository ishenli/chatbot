package com.workdance.core;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;

import com.workdance.core.mvi.ViewModelScope;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    private final ViewModelScope mViewModelScope = new ViewModelScope();

    public Toolbar toolbar;
    public TextView toolBarText;
    protected boolean disableBaseBar;

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

    protected void bindEvents() {

    }

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


    protected boolean disableBaseBar() {
        return false;
    }

    protected void setToolbarTheme() {}

    private void initToolBar() {
        if (disableBaseBar()) {
            return;
        }
        setSupportActionBar(toolbar);
        setToolbarTheme();
        if (toolbarTitle() != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // 清空Toolbar默认标题
            toolBarText = toolbar.findViewById(R.id.toolbar_title);
            // 可以在这里动态设置标题
            toolBarText.setText(toolbarTitle());
        }
        customToolbarAndStatusBarBackgroundColor(false);
//        SharedPreferences sp = getSharedPreferences("chat_kit_config", Context.MODE_PRIVATE);
//        if (sp.getBoolean("darkTheme", true)) {
//            // dark
//            toolbar.getContext().setTheme(R.style.AppTheme_DarkAppbar);
//            customToolbarAndStatusBarBackgroundColor(true);
//        } else {
//            // light
//            toolbar.getContext().setTheme(R.style.AppTheme_LightAppbar);
//            customToolbarAndStatusBarBackgroundColor(false);
//        }

    }



    protected void customToolbarAndStatusBarBackgroundColor(boolean isDarkTheme) {
    }


    /**
     * 设置状态栏和标题栏的颜色
     *
     * @param resId 颜色资源id
     */
    protected void setTitleBackgroundResource(int resId, boolean dark) {
        toolbar.setBackgroundResource(resId);
        setStatusBarTheme(this, dark);
    }

    public static void setStatusBarTheme(final Activity pActivity, final boolean pIsDark) {
        // Fetch the current flags.
        final int lFlags = pActivity.getWindow().getDecorView().getSystemUiVisibility();
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        pActivity.getWindow().getDecorView().setSystemUiVisibility(pIsDark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }

    protected boolean showHomeMenuItem() {
        return true;
    }


    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getActivityScopeViewModel(this, modelClass);
    }

    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getApplicationScopeViewModel(modelClass);
    }
}
