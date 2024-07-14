package com.example.wechat.main.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.databinding.FragmentDashboardBinding;
import com.example.wechat.main.dashboard.assistantList.AssistantListAdapter;
import com.example.wechat.model.UserInfo;
import com.example.wechat.user.UserInfoActivity;
import com.example.wechat.user.UserViewModel;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private UserViewModel userViewModel;
    private AssistantListAdapter myAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initView();
    }

    @SuppressLint("NotifyDataSetChanged")
    void initView() {
        RecyclerView recyclerView = binding.assistantList;

        recyclerView.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, 0, 0, 0);

        userViewModel.getAllUserInfo().observe(getViewLifecycleOwner(), userInfos -> {
            if (myAdapter == null) {
                myAdapter = new AssistantListAdapter(userInfos, new AssistantListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(UserInfo item) {
                        // startActivity useInfoActivity
                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                        intent.putExtra("userInfo", item);
                        startActivity(intent);

                    }
                });
                recyclerView.setAdapter(myAdapter);
            } else {
                myAdapter.setItems(userInfos);
                myAdapter.notifyDataSetChanged(); // 或使用 DiffUtil 进行更高效的更新
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}