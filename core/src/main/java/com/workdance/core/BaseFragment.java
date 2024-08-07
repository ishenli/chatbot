package com.workdance.core;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.workdance.core.mvi.ViewModelScope;

public abstract class BaseFragment extends Fragment {

    private final ViewModelScope mViewModelScope = new ViewModelScope();
    //ViewModelProvider
    private ViewModelProvider mApplicationVMProvider;
    private ViewModelProvider mActivityVMProvider;
    private ViewModelProvider mFragmentVMProvider;

    protected abstract void initViewModel();

    protected void bindEvents() {}

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        bindEvents();
    }

    protected void bindViews(View view) {
    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        dataBinding.unbind();
//        dataBinding = null;
    }

    /**
     * 获取Application级的ViewModel
     * @return Application级的ViewModel
     */
    protected <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        return mViewModelScope.getApplicationScopeViewModel(modelClass);
    }

    /**
     * 获取Activity级的ViewModel
     *
     * @param viewModelClass ViewModel Class
     * @return Activity级的ViewModel
     */
    protected <T extends ViewModel> T getActivityScopeViewModel(Class<T> viewModelClass) {
        if (getActivity() == null) {
            throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
        }
        if (mActivityVMProvider == null) {
            mActivityVMProvider = new ViewModelProvider(getActivity());
        }
        return mActivityVMProvider.get(viewModelClass);
    }

    /**
     * 获取Fragment级的ViewModel
     *
     * @param viewModelClass ViewModel Class
     * @return Fragment级的ViewModel
     */
    protected <T extends ViewModel> T getFragmentScopeViewModel(Class<T> viewModelClass) {
        if (mFragmentVMProvider == null) {
            mFragmentVMProvider = new ViewModelProvider(this);
        }
        return mFragmentVMProvider.get(viewModelClass);
    }

}