package com.workdance.chatbot.ui.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.workdance.chatbot.R;
import com.workdance.chatbot.remote.AssistantClient;
import com.workdance.core.widget.checkList.CheckListItem;
import com.workdance.core.widget.checkList.CheckListViewModel;
import com.workdance.core.widget.checkList.NormalCheckListView;
import com.workdance.chatbot.databinding.FragmentAssistantSheetBinding;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public class AssistantModalBottomSheet extends BottomSheetDialogFragment implements NormalCheckListView.OnCheckListListener{
    public static String TAG = "AssistantModalBottomSheet";
    private final Activity activity;
    private FragmentAssistantSheetBinding binding;
    private CheckListViewModel checkListViewModel;
    private AssistantModalBottomSheet dialog;
    @Setter
    private OnDialogListener onDialogListener;
    @Setter
    @Getter
    private CheckListItem checkListItem;

    public AssistantModalBottomSheet(Activity activity) {
        this.activity = activity;
        this.dialog = this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAssistantSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        checkListViewModel = new ViewModelProvider(getActivity()).get(CheckListViewModel.class);
        NormalCheckListView normalCheckListView = binding.checklist;
        normalCheckListView.bind(getViewLifecycleOwner(), checkListViewModel);
        normalCheckListView.setListener(this);
        AssistantClient.getAssistantModel().observe(getViewLifecycleOwner(), model -> {
            if (model != null) {
                List<CheckListItem> checkListItemList = model.stream().map(item -> {
                    CheckListItem checkListItem = new CheckListItem(item.getName(), item.getCode());
                    return checkListItem;
                }).collect(Collectors.toList());
                checkListViewModel.setItems(checkListItemList);
            }
        });
    }

    @Override
    public void onCheckListClick(CheckListItem checkListItem) {
        dialog.setCheckListItem(checkListItem);
        if (onDialogListener != null) {
            onDialogListener.onSelectListener(checkListItem);
        }
        dialog.dismiss();
    }

    public interface OnDialogListener {
        void onSelectListener(CheckListItem checkListItem);
    }
}
