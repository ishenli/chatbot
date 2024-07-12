package com.example.wechat.user;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.wechat.R;
import com.example.wechat.core.BaseActivity;
import com.example.wechat.core.widget.SimpleTextWatcher;
import com.example.wechat.databinding.ActivityUserCreateBinding;
import com.example.wechat.model.UserInfo;

public class UserInfoCreateActivity extends BaseActivity {
    private ActivityUserCreateBinding binding;
    private MenuItem confirmMenuItem;
    private UserViewModel userViewModel;

    @Override
    protected void beforeViews() {
        binding = ActivityUserCreateBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void bindEvents() {
        super.bindEvents();
        binding.nameEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            handleInputNameChange();
            }
        });
    }

    private void handleInputNameChange() {
        if (confirmMenuItem != null) {
            if (!binding.nameEditText.getText().toString().trim().isEmpty()) {
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
        String nikeName = binding.nameEditText.getText().toString().trim();
        String name = binding.nickNameEditText.getText().toString().trim();
        String avatar = binding.avatarEditText.getText().toString().trim();
        UserInfo userInfo = new UserInfo();
        userInfo.displayName = nikeName;
        userInfo.name =  name;
        userInfo.portrait = avatar;
        userViewModel.addUserInfo(userInfo).observe(this, userInfo1 -> {
            if (userInfo1.isSuccess()) {
                Toast.makeText(UserInfoCreateActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(UserInfoCreateActivity.this, "新增失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected String toolbarTitle() {
        return "新增用户";
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

}
