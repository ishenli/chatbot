package com.workdance.chatbot.ui.assistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.ActivityAssistantCreateBinding;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.Brain;
import com.workdance.chatbot.ui.AppActivity;
import com.workdance.chatbot.ui.AppStatusViewModel;
import com.workdance.core.util.ImageUtils;
import com.workdance.core.widget.SimpleTextWatcher;
import com.workdance.core.widget.checkList.CheckListItem;
import com.workdance.imagepicker.ImagePicker;
import com.workdance.imagepicker.model.ImageItem;

import java.io.File;
import java.util.ArrayList;

public class AssistantCreateActivity extends AppActivity implements AssistantModalBottomSheet.OnDialogListener {
    private ActivityAssistantCreateBinding binding;
    private MenuItem confirmMenuItem;
    private AssistantViewModel assistantViewModel;
    private AppStatusViewModel appStatusViewModel;
    private CheckListItem checkListItem;
    private String avatarUrl;

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    @Override
    protected void beforeViews() {
        binding = ActivityAssistantCreateBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        assistantViewModel = getActivityScopeViewModel(AssistantViewModel.class);
        appStatusViewModel = getApplicationScopeViewModel(AppStatusViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(assistantViewModel);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void bindEvents() {
        super.bindEvents();
        binding.editAssistantName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                handleInputNameChange();
            }
        });

        binding.selectModel.setOnClickListener(v -> modelSelect());
        binding.btnSetAvatar.setOnClickListener(v -> avatarSelect());
    }

    private void modelSelect() {
        AssistantModalBottomSheet modalBottomSheet = new AssistantModalBottomSheet(this);
        // 设置背景透明
        // modalBottomSheet.get.findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        modalBottomSheet.show(getSupportFragmentManager(), AssistantModalBottomSheet.TAG);
        modalBottomSheet.setOnDialogListener(this);
    }

    private void avatarSelect() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
        }
        Activity activity = this;
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (activity.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 100);
                    return;
                }
            }
        }
        ImagePicker.picker().pick(this, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (imageItems == null || imageItems.isEmpty()) {
                Toast.makeText(this, "更新头像失败", Toast.LENGTH_SHORT);
                return;
            }
            File thumbImgFile = ImageUtils.genThumbImgFile(imageItems.get(0).getPath());
            if (thumbImgFile == null) {
                Toast.makeText(this, "更新头像失败: 生成缩略图失败", Toast.LENGTH_SHORT).show();
                return;
            }
            String imagePath = thumbImgFile.getAbsolutePath();
            assistantViewModel.updateAssistantAvatar(imagePath).observe(this, result -> {
                if (result != null) {
                    avatarUrl = result;
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleInputNameChange() {
        if (confirmMenuItem != null) {
            if (!binding.editAssistantName.getText().toString().trim().isEmpty()) {
                confirmMenuItem.setEnabled(true);
            } else {
                confirmMenuItem.setEnabled(false);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String name = binding.editAssistantName.getText().toString().trim();
        String desc = binding.editAssistantDesc.getText().toString().trim();
        Assistant assistant = new Assistant();
        assistant.setName(name);
        assistant.setLogo(avatarUrl);
        assistant.setDescription(desc);
        assistant.setModel(checkListItem.getValue());
        assistant.setBrainType(Brain.BrainTypeEnum.BASIC.getValue());
        assistantViewModel.addAssistant(assistant).observeForever(result -> {
            if (result.isSuccess()) {
                Toast.makeText(AssistantCreateActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                // 通知列表页面刷新
                appStatusViewModel.setShouldRefreshDashboard(true);
                finish();
            } else {
                Toast.makeText(AssistantCreateActivity.this, "新增失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected String toolbarTitle() {
        return "新增助理";
    }

    @Override
    protected int menu() {
        return R.menu.user_change_my_name;
    }

    @Override
    protected void afterMenus(Menu menu) {
        confirmMenuItem = menu.findItem(R.id.save);
        confirmMenuItem.setEnabled(false);
    }

    @Override
    public void onSelectListener(CheckListItem checkListItem) {
        binding.modelName.setText(checkListItem.getText());
        this.checkListItem = checkListItem;
    }
}
