package com.workdance.chatbot.ui.explore.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.model.Circle;
import com.workdance.chatbot.remote.ExploreClient;
import com.workdance.core.data.Book;
import com.workdance.core.data.LoadingStatus;
import com.workdance.core.data.Page;

import java.util.List;

public class CircleViewModel extends ViewModel {
    private final Book<Circle> mBook = new Book<>(1);
    private final MutableLiveData<List<Circle>> mCycleList = new MutableLiveData<>();

    public LiveData<List<Circle>> getCycleList() {
        return mCycleList;
    }
    /**
     * 获取后台视频数据
     */
    public LiveData<LoadingStatus> refresh() {
        MutableLiveData<LoadingStatus> mLoading = new MutableLiveData<>(LoadingStatus.START);
        ExploreClient.listCycle(0, mBook.pageSize()).observeForever(result -> {
            if (result.isSuccess()) {
                Page<Circle> page = result.getResult();
                if (page != null) {
                    List<Circle> items = mBook.firstPage(page);
                    mCycleList.postValue(items);
                    mLoading.setValue(LoadingStatus.SUCCESS);
                }
            } else {
                mLoading.setValue(LoadingStatus.ERROR);
            }
        });
        return mLoading;
    }

    public LiveData<LoadingStatus> loadMore() {
        MutableLiveData<LoadingStatus> mLoading = new MutableLiveData<>(LoadingStatus.START);
        if (mBook.hasMore()) {
            ExploreClient.listCycle(mBook.nextPageIndex(), mBook.pageSize()).observeForever(result -> {
                if (result.isSuccess()) {
                    Page<Circle> page = result.getResult();
                    if (page != null) {
                        List<Circle> newItems = mBook.addPage(page);
                        List<Circle> oldItems = mCycleList.getValue();
                        if (oldItems != null && !oldItems.isEmpty()) {
                            oldItems.addAll(newItems);
                            mCycleList.postValue(oldItems);
                        }
                        mLoading.setValue(LoadingStatus.SUCCESS);
                    }
                } else {
                    mLoading.setValue(LoadingStatus.ERROR);
                }
            });
        } else {
            mBook.end();
            mLoading.setValue(LoadingStatus.END);
        }
        return mLoading;
    }
}
