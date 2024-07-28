package com.workdance.chatbot.ui.main.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.core.BaseFragment;
import com.workdance.chatbot.databinding.FragmentDashboardBinding;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.assistant.AssistantInfoActivity;

public class DashboardFragment extends BaseFragment {

    private FragmentDashboardBinding binding;
    private AssistantListAdapter myAdapter;
    private AssistantListViewModel assistantListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initViewModel() {
        assistantListViewModel = getActivityScopeViewModel(AssistantListViewModel.class);
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
        assistantListViewModel.assistantList.observe(getViewLifecycleOwner(), assistants -> {
            if (myAdapter == null) {
                myAdapter = new AssistantListAdapter(assistants, new AssistantListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Assistant item) {
                        // startActivity useInfoActivity
                        Intent intent = new Intent(getContext(), AssistantInfoActivity.class);
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