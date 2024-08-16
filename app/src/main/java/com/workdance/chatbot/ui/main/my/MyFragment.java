package com.workdance.chatbot.ui.main.my;

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

import com.workdance.chatbot.R;
import com.workdance.chatbot.databinding.FragmentMyBinding;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.user.UserInfoActivity;
import com.workdance.chatbot.ui.user.UserViewModel;

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
        initEvent();
        initViewModel();
        return root;
    }

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setViewModel(userViewModel);
        userViewModel.getUserInfoByName(userViewModel.getDefaultUserName())
                .observe(getViewLifecycleOwner(), userInfoLiveDataObserver -> {
                    if (userInfoLiveDataObserver != null) {
                        userInfo = userInfoLiveDataObserver;
                    }
                });

    }

    private void initView() {
        initServiceBody();
    }

    public void initEvent() {
        binding.meInfo.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            intent.putExtra("userInfo", userInfo);
            startActivity(intent);
        });
    }


    private void initServiceBody() {
        ArrayList<ServiceItem> data = new ArrayList<>();
        data.add(new ServiceItem(R.drawable.icon_pay, "支付", ServiceItem.ServiceItemType.Pay));
        data.add(new ServiceItem(R.drawable.icon_qianbao, "钱包", ServiceItem.ServiceItemType.Wallet));
        data.add(new ServiceItem(R.drawable.icon_shop, "购物", ServiceItem.ServiceItemType.Shop));
        data.add(new ServiceItem(R.drawable.icon_sport, "运动", ServiceItem.ServiceItemType.Shop));
        data.add(new ServiceItem(R.drawable.icon_see, "看一看", ServiceItem.ServiceItemType.See));
        data.add(new ServiceItem(R.drawable.icon_yao, "摇一摇", ServiceItem.ServiceItemType.Shake));
        data.add(new ServiceItem(R.drawable.icon_ting, "听一听", ServiceItem.ServiceItemType.Music));
        data.add(new ServiceItem(R.drawable.icon_more, "更多", ServiceItem.ServiceItemType.Other));

        myViewModel.mData.setValue(data);

        RecyclerView recyclerView = binding.gridView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.setAdapter(new GridAdapter(getActivity(), myViewModel.getData().getValue()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}