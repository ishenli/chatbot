package com.workdance.chatbot.ui.main.dashboard;

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

import com.workdance.chatbot.databinding.FragmentDashboardBinding;
import com.workdance.chatbot.ui.main.dashboard.assistantList.AssistantListAdapter;
import com.workdance.chatbot.ui.main.dashboard.assistantList.AssistantListViewModel;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.user.UserInfoActivity;
import com.workdance.chatbot.ui.user.UserViewModel;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private UserViewModel userViewModel;
    private AssistantListAdapter myAdapter;
    private AssistantListViewModel assistantListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        assistantListViewModel = new ViewModelProvider(this).get(AssistantListViewModel.class);

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

        String userId = userViewModel.getUserId();

        assistantListViewModel.getAllAssistant(userId).observe(getViewLifecycleOwner(), assistants -> {
            if (myAdapter == null) {
                myAdapter = new AssistantListAdapter(assistants, new AssistantListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Assistant item) {
                        // startActivity useInfoActivity
                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                        UserInfo userInfo = new UserInfo();
                        userInfo.displayName = item.getName();
                        userInfo.uid = item.getBrainId();
                        userInfo.name = item.getDescription();
                        userInfo.portrait = item.getLogo();
                        intent.putExtra("userInfo", userInfo);
                        startActivity(intent);

                    }
                });
                recyclerView.setAdapter(myAdapter);
            } else {
                myAdapter.setItems(assistants);
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