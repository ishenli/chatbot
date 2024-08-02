package com.workdance.chatbot.core.widget.CheckList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.workdance.chatbot.R;

import lombok.Setter;

public class NormalCheckListView extends LinearLayout implements CheckListAdapter.OnCheckListListener {
    private static final String TAG = "NormalCheckListView";
    private CheckListAdapter adapter;
    private ListView listView;
    @Setter
    private OnCheckListListener listener;
    private CheckListViewModel viewModel;

    public NormalCheckListView(Context context) {
        super(context);
        init(context, null);
    }

    public NormalCheckListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NormalCheckListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NormalCheckListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.widget_checklist, this);
        listView = view.findViewById(R.id.listView);
        adapter = new CheckListAdapter(context);
        adapter.setListener(this);
        listView.setAdapter(adapter);
    }

    public void bind(LifecycleOwner lifecycleOwner, CheckListViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.getItems().observe(lifecycleOwner, items -> {
            adapter.setItems(items);
            if (viewModel.getSelectedItem() != null) {
                adapter.setSelectItem(viewModel.getSelectedItem().getValue());
            }
            // listView.requestLayout();
        });
    }

    @Override
    public void onCheckboxClick(CheckListItem checkListItem) {
        Log.d(TAG, "onCheckboxClick: "  + checkListItem.getValue());
        // 更新 ViewModel
        viewModel.setSelectedItem(checkListItem);
        if (listener != null) {
            listener.onCheckListClick(checkListItem);
        }
    }

    public interface OnCheckListListener {
        void onCheckListClick(CheckListItem checkListItem);
    }

}
