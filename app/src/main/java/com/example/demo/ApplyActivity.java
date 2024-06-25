package com.example.demo;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.alipay.mobile.antui.basic.AUTitleBar;
import com.example.demo.databinding.ActivityApplyBinding;

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
    }

}