package com.example.wechat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wechat.databinding.ActivityAntBinding;

public class AntActivity extends AppCompatActivity {

    private ActivityAntBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAntBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
//        AUTitleBar titleBar = binding.titleBar;
//        titleBar.setBackground(null);
//        titleBar.setTitleText("借钱");
//        titleBar.setLeftButtonIcon(getResources().getString(PublicResources.String_iconfont_add_user));
    }

}