package com.workdance.chatbot.ui.user;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.workdance.chatbot.R;
import com.workdance.chatbot.core.BaseActivity;
import com.workdance.chatbot.core.widget.SimpleTextWatcher;
import com.workdance.chatbot.databinding.ActivityUserChangeMyNameBinding;
import com.workdance.chatbot.model.UserInfo;

public class ChangeMyNameActivity extends BaseActivity {
    private ActivityUserChangeMyNameBinding binding;
    private MenuItem confirmMenuItem;
    private UserViewModel userViewModel;
    private UserInfo userInfo;

    @Override
    protected void beforeViews() {
        binding = ActivityUserChangeMyNameBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserInfoAsync(userViewModel.getUserId(), false).observe(this, userInfo1 -> {
            userInfo = userInfo1;
            if (userInfo == null) {
                Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                binding.nameEditText.setText(userInfo.displayName);
            }
        });
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
            changeMyName();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeMyName() {
        String nikeName = binding.nameEditText.getText().toString().trim();
        userInfo.displayName = nikeName;
        userViewModel.modifyUserInfo(userInfo).observe(this, userInfo1 -> {
            if (userInfo1.isSuccess()) {
                Toast.makeText(ChangeMyNameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangeMyNameActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected String toolbarTitle() {
        return "修改昵称";
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
