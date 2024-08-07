package com.workdance.core.widget.checkList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CheckListViewModel extends ViewModel {
    private MutableLiveData<List<CheckListItem>> items;
    private MutableLiveData<CheckListItem> selectedItem;
    public CheckListViewModel() {

        items = new MutableLiveData<>(new ArrayList<>());
        selectedItem = new MutableLiveData<>();
    }

    public LiveData<List<CheckListItem>> getItems() {
        return items;
    }

    public void setItems(List<CheckListItem> items) {
        this.items.setValue(items);
    }

    public LiveData<CheckListItem> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(CheckListItem selectedItem) {
        this.selectedItem.setValue(selectedItem);
    }
}
