package com.workdance.chatbot.ui.main.home;

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

import com.workdance.chatbot.ui.chat.ChatActivity;
import com.workdance.chatbot.ui.chat.conversationlist.ConversationListViewModel;
import com.workdance.chatbot.databinding.FragmentHomeBinding;
import com.workdance.chatbot.ui.chat.conversationlist.ConversationListAdapter;
import com.workdance.chatbot.ui.chat.conversationlist.ConversationListItemVO;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ConversationListAdapter adapter;
    private ConversationListViewModel conversationListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        conversationListViewModel = new ViewModelProvider(this).get(ConversationListViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        initView();
        return root;
    }

    private void initView() {
        adapter = new ConversationListAdapter(this);
        RecyclerView recyclerView = binding.chatView;
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, 0, 0, 0);

        conversationListViewModel.conversationListLiveData().observe(getViewLifecycleOwner(), conversationList -> {
            adapter.setChatList(conversationList);
        });
        adapter.setOnClickConversationItemListener(new ConversationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ConversationListItemVO item) {
                try {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("conversationTitle", item.name);
                    intent.putExtra("chatId", item.id);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}