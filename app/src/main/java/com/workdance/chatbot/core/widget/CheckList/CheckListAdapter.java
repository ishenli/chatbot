package com.workdance.chatbot.core.widget.CheckList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.workdance.chatbot.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Setter;

public class CheckListAdapter extends ArrayAdapter<CheckListItem> {

    private List<CheckListItem> items = new ArrayList<>();
    private CheckListItem selectedItem;

    @Setter
    private OnCheckListListener listener;

    public CheckListAdapter(Context context) {
        super(context, 0, new ArrayList<>());
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CheckListItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.widget_checklist_item, parent, false);
        }

        CheckListItem item = items.get(position);
        ConstraintLayout constraintLayout = convertView.findViewById(R.id.checkListItem);
        ImageView checkBox = convertView.findViewById(R.id.checkBox);
        TextView textView = convertView.findViewById(R.id.textView);

        if (this.selectedItem != null && Objects.equals(this.selectedItem.getValue(), item.getValue())) {
            // checkBox.setChecked(true);
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        textView.setText(item.getText());

        constraintLayout.setOnClickListener(v -> {
            this.selectedItem = item;
            // item.setChecked(!item.isChecked());
            if (listener != null) {
                listener.onCheckboxClick(item);
            }
            notifyDataSetChanged();
        });

        return convertView;
    }

    public void setItems(List<CheckListItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setSelectItem(CheckListItem value) {
        this.selectedItem = value;
    }

    public interface OnCheckListListener {
        void onCheckboxClick(CheckListItem checkListItem);
    }
}