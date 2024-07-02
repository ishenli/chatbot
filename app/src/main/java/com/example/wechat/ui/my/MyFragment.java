package com.example.wechat.ui.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.mobile.antui.basic.AURoundImageView;
import com.example.wechat.R;
import com.example.wechat.databinding.FragmentMyBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    // 自动跟视图关联
    private FragmentMyBinding binding;

    private MyViewModel myViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);

        binding = FragmentMyBinding.inflate(inflater, container, false);
        binding.setViewModel(myViewModel);
        binding.setLifecycleOwner(this);

        View root = binding.getRoot();

        initView();

        return root;
    }

    private void initView() {
        initAvatarHeader();
        initServiceBody();

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


    private void initAvatarHeader() {
        AURoundImageView round = binding.round;
        Picasso p = Picasso.get();
        p.setLoggingEnabled(true);
        p.load("https://himg.bdimg.com/sys/portrait/item/public.1.cd7083db.YCCdYhAb3c_HTkJ5oKEIuw.jpg")
                .into(round);
        round.setNeedMask(false);
        round.setRounded(true);
        round.setRoundSize(16);
        round.setBackground(null);

        // 这里是一种 watch 的方式，手动 set，直接用模板语法就好
        // myViewModel.getText().observe(getViewLifecycleOwner(), binding.myName::setText);
//        myViewModel.getNum().observe(getViewLifecycleOwner(), binding.myNum::setText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}