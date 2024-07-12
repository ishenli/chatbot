package com.example.wechat.main.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.R;
import com.example.wechat.databinding.FragmentMyBinding;
import com.example.wechat.model.UserInfo;
import com.example.wechat.user.UserInfoActivity;
import com.example.wechat.user.UserInfoCreateActivity;
import com.example.wechat.user.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    // 自动跟视图关联
    private FragmentMyBinding binding;
    private UserInfo userInfo;
    private MyViewModel myViewModel;
    private UserViewModel userViewModel;
    private Observer<List<UserInfo>> userInfoLiveDataObserver = new Observer<List<UserInfo>>() {
        @Override
        public void onChanged(@Nullable List<UserInfo> userInfos) {
            if (userInfos == null) {
                return;
            }
            for (UserInfo info : userInfos) {
                if (info.uid.equals(userViewModel.getUserId())) {
                    userInfo = info;
                    break;
                }
            }
        }
    };
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);

        binding = FragmentMyBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        View root = binding.getRoot();
        initView();
        bindEvents();
        init();
        return root;
    }

    private void init() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setViewModel(userViewModel);
        userViewModel.getUserInfoAsync(userViewModel.getUserId(), true)
                .observe(getViewLifecycleOwner(), userInfoLiveDataObserver -> {
                    if (userInfoLiveDataObserver != null) {
                        userInfo = userInfoLiveDataObserver;
                    }
                });

    }

    private void initView() {
        initServiceBody();
        binding.avatar.setRoundSize(16);
        binding.avatar.setRounded(true);
    }

    public void bindEvents() {
        binding.meInfo.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            intent.putExtra("userInfo", userInfo);
            startActivity(intent);
        });

        binding.addUserBtn.setOnClickListener( view -> {
            Intent intent = new Intent(getActivity(), UserInfoCreateActivity.class);
            startActivity(intent);
        });
    }


    private void initServiceBody() {
        ArrayList<ServiceItem> data = new ArrayList<>();
        data.add(new ServiceItem(R.drawable.icon_pay, "支付"));
        data.add(new ServiceItem(R.drawable.icon_qianbao, "钱包"));
        data.add(new ServiceItem(R.drawable.icon_shop, "购物"));
        data.add(new ServiceItem(R.drawable.icon_sport, "运动"));
        data.add(new ServiceItem(R.drawable.icon_see, "看一看"));
        data.add(new ServiceItem(R.drawable.icon_yao, "摇一摇"));
        data.add(new ServiceItem(R.drawable.icon_ting, "听一听"));
        data.add(new ServiceItem(R.drawable.icon_more, "更多"));

        myViewModel.mData.setValue(data);

        RecyclerView recyclerView = binding.gridView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.setAdapter(new GridAdapter(myViewModel.getData().getValue()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}