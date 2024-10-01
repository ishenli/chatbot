package com.workdance.chatbot.ui.explore;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ActivityExploreCycleBinding;
import com.workdance.chatbot.ui.AppActivity;
import com.workdance.core.BaseActivity;
import com.workdance.core.base.BaseDialog;
import com.workdance.core.util.ImageUtils;
import com.workdance.core.util.ViewUtils;
import com.workdance.core.widget.dialog.MenuDialog;
import com.workdance.multimedia.camera.TakePhotoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppActivity {
    private static final String TAG = "UserInfoActivity";
    private ActivityExploreCycleBinding binding;

    public static void intentInto(Activity activity) {
        Intent intent = new Intent(activity, ExploreActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void beforeViews() {
        binding = ActivityExploreCycleBinding.inflate(getLayoutInflater());
    }

    @Override
    protected int toolbarDisplayType() {
        return BaseActivity.TOOLBAR_DEFAULT;
    }

    @Override
    protected String toolbarTitle() {
        return "文字居中";
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
    }


    @Override
    protected void afterViews() {
        setSupportActionBar(toolbar);
        ViewUtils.setSystemBarTheme(this, Color.TRANSPARENT, true, true, Color.WHITE, false, false);
        setActionBarTheme(true, false, "探索", Color.TRANSPARENT, Color.BLACK);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(binding.container.getId(), new CircleFragment(), CircleFragment.TAG)
                .commit();
    }

    @Override
    protected int menu() {
        return R.menu.cycle_optionmenu;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_take_photo) {
            List<String> data = new ArrayList<>();
            data.add("文字");
            data.add("拍摄");
            data.add("从相册选择");
            // 底部选择框
            new MenuDialog.Builder(this)
                    // 设置 null 表示不显示取消按钮
                    //.setCancel(getString(R.string.common_cancel))
                    // 设置点击按钮后不关闭对话框
                    //.setAutoDismiss(false)
                    .setList(data)
                    .setListener(new MenuDialog.OnListener<String>() {

                        @Override
                        public void onSelected(BaseDialog dialog, int position, String string) {
                            Log.d(TAG, "位置：" + position + "，文本：" + string);
                            switch (position) {
                                case 0:
                                    break;
                                case 1: // 选择相机
                                    openCamera();
                                    break;
                            }
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            Log.d(TAG, "取消了");
                        }
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 拍照
     */
    public void openCamera() {
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        if (!checkPermission(permissions)) {
            this.requestPermissions(permissions, 100);
            return;
        }
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            String path = data.getStringExtra("path");
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT);
                return;
            }

            if (data.getBooleanExtra("take_photo", true)) {
                File file = new File(path);
                ImageUtils.saveMediaToAlbum(this, file, true);
            } else {
                File file = new File(path);
                ImageUtils.saveMediaToAlbum(this, file, false);
            }

            Toast.makeText(this, "拍照成功：" + path, Toast.LENGTH_SHORT);
        }
    }
}
