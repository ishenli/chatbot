package com.example.wechat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.databinding.ActivityChatBinding;
import com.example.wechat.ui.home.HomeViewModel;
import com.example.wechat.ui.home.chat.chatList.ChatListItemAdapter;
import com.example.wechat.ui.home.chat.chatList.ChatListItemVO;
import com.example.wechat.widget.inputPanel.EmotionKeyboardFragment;
import com.example.wechat.widget.inputPanel.InputPanelManager;
import com.example.wechat.widget.inputPanel.StageKeyboardFragment;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private HomeViewModel homeViewModel;
    private EmotionKeyboardFragment emotionKeyboard;
    private StageKeyboardFragment stageKeyboard;
    private Fragment activeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

//        initEvent();
    }

    private void initEvent() {
//        binding.bottomBar.setOnClickListener(v -> {
////            InputViewUtils.showSoftInput(binding.chatEditText, this);
//            Toast.makeText(this, "点击了输入框", Toast.LENGTH_LONG);
////            binding.chatStageView.setVisibility(View.VISIBLE);
//        });
        binding.chatEmoBtn.setOnClickListener(v -> {
            switchKeyboard(emotionKeyboard);
        });

        binding.chatAddBtn.setOnClickListener(v -> {
            switchKeyboard(stageKeyboard);
        });
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 设置返回按钮可见
        getSupportActionBar().setDisplayShowHomeEnabled(true); // 设置为默认的向上导航图标
        // 可选：设置标题
        getSupportActionBar().setTitle("Your Activity Title");
        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.setAdapter(new ChatListItemAdapter(homeViewModel.mChatList.getValue(), new ChatListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatListItemVO item) {
            }
        }));
        initInputPanel();
    }

    private void initInputPanel() {
        InputPanelManager inputPanelManager = new InputPanelManager(this, getSupportFragmentManager());
        inputPanelManager.bindContentView(binding.chatMessageBody)
                .bindInputEditText(binding.chatEditText)
                .bindMorePanelView(binding.fragmentKeyboardContainer)
                .bindListView(binding.recyclerView)
                .bindMotionBottom(binding.chatEmoBtn, new InputPanelManager.OnMotionClickListenerCallback() {
                    @Override
                    public void onMotionClick(int motionClickState) {
                        switch (motionClickState) {
//                            case MOTION_SOFT_KEY_BOARD_INPUT:
//                                // 软键盘输入状态
//                                btnEmotion.setText("表情");
//                                break;
//                            case MOTION_INPUT:
//                                // 录音状态
//                                btnEmotion.setText("键盘");
//                                break;
                        }
                    }
                })
                .bindVoiceBottom(binding.chatVoiceBtn, new InputPanelManager.OnVoiceClickListenerCallback() {
                    @Override
                    public void onVoiceClick(int voiceClickState) {
                        switch (voiceClickState) {
//                            case VOICE_CLICK_STATE_RECORD:
//                                // 录音状态
//                                break;
//                            case VOICE_CLICK_STATE_CANCEL:
//                                // 取消录音状态
//                                break;
                        }
                    }
                })
                .bindMoreBottom(binding.chatAddBtn)
                .bindMoreInputFragment(new StageKeyboardFragment())
                .bindMotionInputFragment(new EmotionKeyboardFragment());
    }

    private void initKeyboardView() {
        emotionKeyboard  = new EmotionKeyboardFragment();
        stageKeyboard = new StageKeyboardFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_keyboard_container, emotionKeyboard)
                .add(R.id.fragment_keyboard_container, stageKeyboard)
                .hide(emotionKeyboard)
                .hide(stageKeyboard)
                .commit();
    }

    public void switchKeyboard(Fragment activeFragment) {
        animFragmentLayout();
        if (activeFragment == emotionKeyboard) {
            getSupportFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .hide(emotionKeyboard)
                    .show(stageKeyboard)
                    .commit();
            activeFragment = stageKeyboard;
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(stageKeyboard)
                    .show(emotionKeyboard)
                    .commit();
            activeFragment = emotionKeyboard;
        }
    }

    public void animFragmentLayout() {
        // 创建一个ObjectAnimator来改变FrameLayout的高度
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator heightAnimator = ObjectAnimator.ofInt(binding.fragmentKeyboardContainer, "height", 200, 320); // 初始高度200dp，目标高度400dp

        // 设置动画时长
        heightAnimator.setDuration(2000); // 动画持续1秒

        // 添加动画监听器，可选
        heightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 动画开始前的操作
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后操作
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        // 开始动画
        heightAnimator.start();
    }

    // 重写 onOptionsItemSelected 方法处理返回按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 关闭当前Activity并返回上一个
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
