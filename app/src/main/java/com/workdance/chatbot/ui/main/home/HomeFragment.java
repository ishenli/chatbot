package com.workdance.chatbot.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.core.BaseFragment;
import com.workdance.chatbot.databinding.FragmentHomeBinding;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.ui.chat.ChatActivity;
import com.workdance.chatbot.ui.main.dashboard.AssistantListViewModel;
import com.workdance.chatbot.ui.main.home.conversationlist.ConversationListAdapter;
import com.workdance.chatbot.ui.main.home.conversationlist.ConversationListItemVO;
import com.workdance.chatbot.ui.main.home.conversationlist.ConversationListViewModel;
import com.workdance.chatbot.ui.user.UserViewModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private ConversationListAdapter adapter;
    private ConversationListViewModel conversationListViewModel;
    private AssistantListViewModel assistantListViewModel;
    private UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initView();
        return root;
    }

    @Override
    protected void initViewModel() {
        conversationListViewModel = getActivityScopeViewModel(ConversationListViewModel.class);
        assistantListViewModel = getActivityScopeViewModel(AssistantListViewModel.class);
        userViewModel = getApplicationScopeViewModel(UserViewModel.class);
    }

    private void initView() {
        adapter = new ConversationListAdapter(this);
        RecyclerView recyclerView = binding.chatView;
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, 0, 0, 0);

        conversationListViewModel.conversationListLiveData().observe(getViewLifecycleOwner(), conversationList -> {
            if (conversationList == null) {
                return;
            }
            List<ConversationListItemVO> list = conversationList.stream().peek(item -> {
                if (TextUtils.isEmpty(item.avatar)) {
                    List<Assistant> assistantList = assistantListViewModel.getAssistantList().getValue();
                    if (assistantList != null && !assistantList.isEmpty()) {
                        Optional<Assistant> matchAssistant = assistantList.stream().filter(assistant -> assistant.getBrainId().equals(item.brainId))
                                .findFirst();
                        matchAssistant.ifPresent(assistant -> item.avatar = assistant.getLogo());
                    }
                }
            }).collect(Collectors.toList());
            adapter.setChatList(list);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}