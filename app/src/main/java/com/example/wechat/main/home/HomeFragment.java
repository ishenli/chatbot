package com.example.wechat.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.chat.ChatActivity;
import com.example.wechat.databinding.FragmentHomeBinding;
import com.example.wechat.main.home.chat.chatList.ChatListItemAdapter;
import com.example.wechat.main.home.chat.chatList.ChatListItemVO;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        binding.applyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ApplyActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        binding.antBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), AntActivity.class);
//                startActivity(intent);
//            }
//        });

        initView();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void initView() {

        RecyclerView recyclerView = binding.chatView;
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.setAdapter(new ChatListItemAdapter(homeViewModel.mChatList.getValue(), new ChatListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatListItemVO item) {
                try {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("chatId", item.id);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}