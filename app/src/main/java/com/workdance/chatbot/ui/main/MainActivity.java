package com.workdance.chatbot.ui.main;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workdance.chatbot.R;
import com.workdance.chatbot.core.BaseActivity;
import com.workdance.chatbot.databinding.ActivityMainBinding;
import com.workdance.chatbot.ui.AppStatusViewModel;
import com.workdance.chatbot.ui.main.dashboard.AssistantListViewModel;
import com.workdance.chatbot.ui.main.home.conversationlist.ConversationListViewModel;
import com.workdance.chatbot.ui.user.UserViewModel;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private AssistantListViewModel assistantListViewModel;
    private ConversationListViewModel conversationListViewModel;
    private AppStatusViewModel appStatusViewModel;
    private UserViewModel userViewModel;

    @Override
    protected boolean disableBaseBar() { return true;}

    @Override
    protected void beforeViews() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected View contentLayout() {
        return binding.getRoot();
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void afterViews() {
        appStatusViewModel = getApplicationScopeViewModel(AppStatusViewModel.class);
        userViewModel = getApplicationScopeViewModel(UserViewModel.class);
        assistantListViewModel = getActivityScopeViewModel(AssistantListViewModel.class);
        conversationListViewModel = getActivityScopeViewModel(ConversationListViewModel.class);
        assistantListViewModel.loadAssistantList(userViewModel.getUserId());
        conversationListViewModel.loadConversationList(userViewModel.getUserId());
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: shouldRefreshHomeLiveData is " + appStatusViewModel.shouldRefreshHomeLiveData().getValue());
        super.onResume();
        if (Boolean.TRUE.equals(appStatusViewModel.shouldRefreshHomeLiveData().getValue())) {
            conversationListViewModel.loadConversationList(userViewModel.getUserId());
            appStatusViewModel.setShouldRefreshHome(false);
        }

        if (Boolean.TRUE.equals(appStatusViewModel.shouldRefreshDashboardLiveData().getValue())) {
            assistantListViewModel.loadAssistantList(userViewModel.getUserId());
            appStatusViewModel.setShouldRefreshHome(false);
        }

    }
}