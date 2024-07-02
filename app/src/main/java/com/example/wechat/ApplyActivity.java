package com.example.wechat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alipay.mobile.antui.basic.AUTitleBar;
import com.alipay.mobile.antui.utils.PublicResources;
import com.example.wechat.databinding.ActivityApplyBinding;

public class ApplyActivity extends AppCompatActivity {

    private ActivityApplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityApplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        AUTitleBar titleBar = binding.titleBar;
        titleBar.setBackground(null);
        titleBar.setTitleText("借钱");
        titleBar.setLeftButtonIcon(getResources().getString(PublicResources.String_iconfont_add_user));
    }

}