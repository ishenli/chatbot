package com.workdance.chatbot.ui.chat.inputPanel;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workdance.chatbot.R;
import com.workdance.chatbot.core.widget.emotionKeyboard.EmotionFragment;

public class InputPanelManager {

    private static final int SOFT_KEY_BOARD_STATE_SHOW = 1;
    private static final int SOFT_KEY_BOARD_STATE_HIND = 2;
    private static final int CLICK_HINT_SOFT_KEY_BOARD = 3;
    private static final int MORE_PANEL_STATE_HIND = 4;
    private static final int MORE_PANEL_STATE_SHOW = 5;
    private static final int VOICE_SOFT_KEY_BOARD_INPUT = 6;
    public static final int MOTION_SOFT_KEY_BOARD_INPUT = 7;
    private static final int CLICK_UN_HINT_SOFT_KEY_BOARD = 8;
    private static final int LOAD_SHOW_MORE_INPUT_PANEL = 9;
    private static final int LOAD_SHOW_MOTION_INPUT_PANEL = 10;
    private static final int VOICE_INPUT = 21;
    public static final int MOTION_INPUT = 22;
    private static final String SHARE_PREFERENCE_NAME = "input_more_util";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";

    // 默认配置项
    private int mMorePanelState = MORE_PANEL_STATE_HIND;
    private int mSoftKeyBoardState = SOFT_KEY_BOARD_STATE_HIND;
    private int mCurrentShowMoreInputPanel = -1;
    private int mCurrentClickState = -1;
    private int mCurrentVoiceClickState = VOICE_SOFT_KEY_BOARD_INPUT;
    private int mCurrentMotionClickState = MOTION_SOFT_KEY_BOARD_INPUT;

    private float DEFAULT_SOFT_KEY_BOARD_HEIGHT = 741f;  // 十进制的116
    private float mSoftKeyBoardHeight;
    private int DURATION = 200;
    private Long mOldTimeLong = 0L;

    // 各类布局元素
    private final Activity mActivity;
    private View mContentView;
    private View mMoreBottom;
    private View mVoiceBottom;
    private ImageView mMotionBtn;
    private EditText mInputEditText;
    private Button mSendButton;
    private RecyclerView mListView;
    private InputMethodManager mInputManager;
    private SharedPreferences sp;

    // 各个 fragment
    private FragmentManager mFragmentManager = null;
    private FrameLayout mMoreContentView;
    private Fragment mMorePanelFragment = null;
    private Fragment mMotionPanelFragment = null;


    private OnMotionClickListenerCallback mOnMotionClickListenerCallBack;
    private OnVoiceClickListenerCallback mOnVoiceClickListenerCallBack;

    public InputPanelManager(Activity activity, FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
        this.mActivity = activity;
        this.init();
    }

    private void init() {
        // Listen for soft keyboard height changes
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.i("InputPanelManager", "keyBoardShow: " + height);
                mSoftKeyBoardState = SOFT_KEY_BOARD_STATE_SHOW;
                mActivity.findViewById(R.id.chat_message_body).postDelayed(() -> {
                    resetMoreContentPanelHeight(height);
                }, DURATION + 50);
                mMoreContentView.getLayoutParams().height = height;
                saveSoftKeyHeightToCache(height);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.i("InputPanelManager", "keyBoardHide: " + height);
                mSoftKeyBoardState = SOFT_KEY_BOARD_STATE_HIND;
                mSoftKeyBoardHeight = (float) height;
                saveSoftKeyHeightToCache((float) height);
                mMoreContentView.getLayoutParams().height = height;
                if (mCurrentClickState == CLICK_HINT_SOFT_KEY_BOARD) {
                    mCurrentClickState = -1;
                    hintMorePanel();
                }
            }
        });

        mInputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        sp = mActivity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        // Get the saved soft keyboard height
        mSoftKeyBoardHeight = getCacheSoftKeyHeight();
    }

    private float getCacheSoftKeyHeight() {
        return sp.getFloat(
                SHARE_PREFERENCE_SOFT_INPUT_HEIGHT,
                DEFAULT_SOFT_KEY_BOARD_HEIGHT
        );
    }


    @SuppressLint("CommitPrefEdits")
    public void saveSoftKeyHeightToCache(float mSoftHeight) {
        if (mSoftHeight > 0) {
            sp.edit().putFloat(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, mSoftHeight);
        }
    }


    /**
     * 重置更多内容面板的高度，以适应软键盘的高度变化。
     * 当软键盘高度发生变化时，需要调整更多内容面板的高度，以保持界面的布局合理性。
     * 此方法通过比较当前软键盘高度和之前记录的软键盘高度的差值，来决定是隐藏还是显示更多内容面板，
     * 以及如何调整其高度。
     *
     * @param mSoftKeyHeight 当前软键盘的高度，用于计算面板高度的调整值。
     */
    private void resetMoreContentPanelHeight(float mSoftKeyHeight) {
        // 计算当前软键盘高度与之前记录的高度之差
        float v = mSoftKeyBoardHeight - mSoftKeyHeight;

        // 如果差值大于0，说明软键盘高度增加，需要隐藏更多内容面板
        if (v > 0) {
            showMorePanel(-mSoftKeyBoardHeight, -mSoftKeyHeight);
            // 如果差值小于0，说明软键盘高度减少，需要显示更多内容面板
        } else if (v < 0) {
            showMorePanel(mSoftKeyBoardHeight, -mSoftKeyHeight);
        }

        // 更新记录的软键盘高度值
        this.mSoftKeyBoardHeight = mSoftKeyHeight;
    }


    public InputPanelManager bindContentView(View mContentView) {
        this.mContentView = mContentView;
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    public InputPanelManager bindInputEditText(EditText mInputEditText) {
        this.mInputEditText = mInputEditText;
        this.mInputEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isNotDoubleClick()) {
                    if (isScroll()) {
                        mInputEditText.postDelayed(this::inputEditClick, 200);
                    } else {
                        inputEditClick();
                    }
                }
                return true;
            }
            return true;
        });
        this.mInputEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                afterInputTextChanged(editable);
            }
        });
        return this;
    }

    private void onInputTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void afterInputTextChanged(Editable editable) {
        EditText editText = mInputEditText;
        if (editText.getText().toString().trim().length() > 0) {
            if (mActivity.getCurrentFocus() == editText) {
//                notifyTyping(TypingMessageContent.TYPING_TEXT);
            }
            mSendButton.setVisibility(View.VISIBLE);
            mMoreBottom.setVisibility(View.GONE);
        } else {
            mSendButton.setVisibility(View.GONE);
            mMoreBottom.setVisibility(View.VISIBLE);
        }
    }


    private boolean isNotDoubleClick() {
        if ((System.currentTimeMillis() - mOldTimeLong) < 500) {
            return false;
        }
        mOldTimeLong = System.currentTimeMillis();
        return true;
    }

    /**
     * 判断是否需要滚动列表到最后一项。
     * 此方法用于检查列表视图是否已经滚动到了最后一项，如果不是，则平滑滚动到最后一项。
     * 这对于确保用户能够看到列表中的所有内容，或者在加载新数据时自动滚动到最新内容是非常有用的。
     * @return 如果列表滚动到了最后一项，则返回false；否则返回true，表示需要滚动到最后一项。
     */
    private boolean isScroll() {
        // 检查mListView是否为空，如果非空，则继续检查
        if (this.mListView != null) {
            // 检查列表是否有适配器，即是否有数据展示
            // 获取LinearLayoutManager实例，用于后续判断和滚动操作
            LinearLayoutManager layoutManager = (LinearLayoutManager) this.mListView.getLayoutManager();
            // 检查layoutManager是否为空
            if (layoutManager != null) {
                // 判断当前滚动位置是否为最后一项，如果不是，则滚动到最后一项并返回true
                if (layoutManager.findLastVisibleItemPosition() != layoutManager.getItemCount() - 1) {
                    this.mListView.smoothScrollToPosition(layoutManager.getItemCount() - 1);
                    return true;
                }
            }
        }
        // 如果列表为空，或者适配器为空，或者已经滚动到最后一项，则返回false
        return false;
    }


    private void inputEditClick() {
        mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD;
        switch (mSoftKeyBoardState) {
            case SOFT_KEY_BOARD_STATE_HIND:
                // Soft keyboard is hidden
                switch (mMorePanelState) {
                    case MORE_PANEL_STATE_HIND:
                        // More panel is hidden
                        morePanelGONE();
                        showMorePanel();
                        mVoiceBottom.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showSoftInput();
                            }
                        }, 50);
                        break;
                    case MORE_PANEL_STATE_SHOW:
                        // If the panel is already displayed, just show the soft keyboard
                        showSoftInput();
                        break;
                }
                break;
            case SOFT_KEY_BOARD_STATE_SHOW:
                // Soft keyboard is displayed
                break;
        }
    }

    private void showSoftInput() {
        this.mInputEditText.requestFocus();
        this.mInputEditText.post(() -> {
            mInputManager.showSoftInput(this.mInputEditText, 0);
        });
    }

    private void hintMorePanel() {
        hintMorePanel(-mSoftKeyBoardHeight, 0f);
    }

    private void performAnimation(float startValue, float endValue) {
        Log.d("InputPanelManager", " startValue: " + String.valueOf(startValue) + "endValue: " + String.valueOf(endValue));
        if (mContentView != null) {
            ValueAnimator mValueAnimation = ValueAnimator.ofFloat(startValue, endValue);
            mValueAnimation.setDuration(DURATION);
            mValueAnimation.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                mContentView.setTranslationY(v);
                if (mMoreContentView != null) {
                    mMoreContentView.setTranslationY(v);
                }
            });
            mValueAnimation.setInterpolator(new LinearInterpolator());
            mValueAnimation.start();
        }
    }


    private void hintMorePanel(Float startValue, Float endValue) {
        mMorePanelState = MORE_PANEL_STATE_HIND;
        performAnimation(startValue, endValue);
    }

    private void showMorePanel(Float startValue, Float endValue) {
        mMorePanelState = MORE_PANEL_STATE_SHOW;
        performAnimation(startValue, endValue);
    }


    private void showMorePanel() {
        showMorePanel(0f, -mSoftKeyBoardHeight);
    }

    private void morePanelGONE() {
        this.mMoreContentView.setVisibility(View.GONE);
    }


    public InputPanelManager bindMorePanelView(FrameLayout fragmentKeyboardContainer) {
        this.mMoreContentView = fragmentKeyboardContainer;
        this.mMoreContentView.getLayoutParams().height = (int) mSoftKeyBoardHeight;
        morePanelGONE();
        return this;
    }

    /**
     * 绑定更多操作面板UI
     */
    public InputPanelManager bindMoreInputFragment(StageKeyboardFragment mMoreInputFragment) {
        this.mMorePanelFragment = mMoreInputFragment;
        return this;
    }

    public InputPanelManager bindMotionInputFragment(EmotionFragment emotionKeyboardFragment) {
        this.mMotionPanelFragment = emotionKeyboardFragment;
        return this;
    }

    public InputPanelManager bindVoiceBtn(
            View mVoiceBottom,
            OnVoiceClickListenerCallback mOnVoiceClickListenerCallBack
    ) {
        this.mVoiceBottom = mVoiceBottom;
        this.mOnVoiceClickListenerCallBack = mOnVoiceClickListenerCallBack;
        this.mVoiceBottom.setOnClickListener(v -> {
            if (isNotDoubleClick()) {
                if (isScroll()) {
                    mVoiceBottom.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            voiceClick();
                        }
                    }, 200);
                }
            }
        });
        return this;
    }

    private void voiceClick() {
    }

    public InputPanelManager bindMoreBottom(ImageView chatAddBtn) {
        this.mMoreBottom = chatAddBtn;
        this.mMoreBottom.setOnClickListener(v -> {
            if (isNotDoubleClick()) {
                if (isScroll()) {
                    mMoreBottom.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moreBtnClick();
                        }
                    }, 200);
                } else {
                    moreBtnClick();
                }
            }
        });
        return this;
    }

    private void moreBtnClick() {
        mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD;
        mInputEditText.clearFocus();
        onClick(LOAD_SHOW_MORE_INPUT_PANEL);
        onDefault();
    }

    public InputPanelManager bindMotionBtn(ImageView mMotionBtn, final OnMotionClickListenerCallback mOnMotionClickListenerCallBack) {
        this.mMotionBtn = mMotionBtn;
        this.mOnMotionClickListenerCallBack = mOnMotionClickListenerCallBack;
        this.mMotionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotDoubleClick()) {
                    if (isScroll()) {
                        mMotionBtn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                motionClick();
                            }
                        }, 200);
                    } else {
                        motionClick();
                    }
                }
            }
        });
        return this;
    }

    /**
     * 表情按钮点击
     */
    private void motionClick() {
        this.mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD;
        this.mCurrentVoiceClickState = VOICE_INPUT;
        switchVoiceIcon();
        onClick(LOAD_SHOW_MOTION_INPUT_PANEL);
        switchMotionIcon();
        mInputEditText.requestFocus();
    }

    private void switchMotionIcon() {
        switch (mCurrentMotionClickState) {
            case MOTION_SOFT_KEY_BOARD_INPUT: {
                mCurrentMotionClickState = MOTION_INPUT;
                mMotionBtn.setImageResource(R.drawable.icon_keyboard);
                break;
            }
            case MOTION_INPUT: {
                mCurrentMotionClickState = MOTION_SOFT_KEY_BOARD_INPUT;
                mMotionBtn.setImageResource(R.drawable.icon_biaoqing);
                break;
            }
        }
        this.mOnMotionClickListenerCallBack.onMotionClick(mCurrentMotionClickState);
    }

    private void switchVoiceIcon() {
        switch (mCurrentVoiceClickState) {
            case VOICE_SOFT_KEY_BOARD_INPUT: {
                mCurrentVoiceClickState = VOICE_INPUT;
                break;
            }
            case VOICE_INPUT: {
                mCurrentVoiceClickState = VOICE_SOFT_KEY_BOARD_INPUT;
                break;
            }
        }
        this.mOnVoiceClickListenerCallBack.onVoiceClick(mCurrentVoiceClickState);
    }

    private void onClick(int mSwitchType) {
        switch (mSoftKeyBoardState) {
            case SOFT_KEY_BOARD_STATE_SHOW: { // 软键盘显示
                mCurrentClickState = CLICK_UN_HINT_SOFT_KEY_BOARD;
                switchMorePanel(mSwitchType); // 加载更多操作面板
                morePanelVISIBLE();
                hideSoftInput();
                break;
            }
            case SOFT_KEY_BOARD_STATE_HIND: { // 软键盘没有显示
                switch (mMorePanelState) {
                    case MORE_PANEL_STATE_SHOW: {
                        if (mCurrentShowMoreInputPanel != mSwitchType) { // 加载更多操作面板
                            switchMorePanel(mSwitchType);
                        } else {
                            if (mInputEditText != null) {
                                mInputEditText.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showSoftInput();
                                    }
                                }, 50);
                            }
                        }
                        break;
                    }
                    case MORE_PANEL_STATE_HIND: {
                        switchMorePanel(mSwitchType); // 加载更多操作面板
                        morePanelVISIBLE();
                        showMorePanel();
                        break;
                    }
                }
                break;
            }
        }
    }

    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mInputEditText.getWindowToken(), 0);
    }

    private void morePanelVISIBLE() {
        if (mMoreContentView != null) {
            mMoreContentView.setVisibility(View.VISIBLE);
        }
    }

    private void switchMorePanel(int mSwitchType) {
        if (mCurrentShowMoreInputPanel == mSwitchType) {
            return;
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (mSwitchType) {
            case LOAD_SHOW_MORE_INPUT_PANEL: {
                if (mMorePanelFragment != null) {
                    switchFragment(mMorePanelFragment, transaction);
                    mCurrentShowMoreInputPanel = LOAD_SHOW_MORE_INPUT_PANEL;
                }
                break;
            }
            case LOAD_SHOW_MOTION_INPUT_PANEL: {
                if (mMotionPanelFragment != null) {
                    switchFragment(mMotionPanelFragment, transaction);
                    mCurrentShowMoreInputPanel = LOAD_SHOW_MOTION_INPUT_PANEL;
                }
            }
        }
    }

    private void switchFragment(Fragment mFragment, FragmentTransaction mFragmentTransaction) {
        if (mFragment != null && mMoreContentView != null && mMoreContentView.getId() != 0) {
            if (mFragmentTransaction != null) {
                mFragmentTransaction.replace(mMoreContentView.getId(), mFragment);
            }
        }
        if (mFragmentTransaction != null) {
            mFragmentTransaction.commitAllowingStateLoss();
        }
    }


    /**
     * 设置默认状态
     */
    private void onDefault() {
        mCurrentMotionClickState = MOTION_INPUT;
        mCurrentVoiceClickState = VOICE_INPUT;
//        switchVoiceIcon();
//        switchMotionIcon()
        mInputEditText.clearFocus();
    }

    @SuppressLint("ClickableViewAccessibility")
    public InputPanelManager bindListView(RecyclerView recyclerView) {
        this.mListView = recyclerView;
        if (mListView != null) {
            mListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    manualHintMorePanel();
                    return false;
                }
            });
//            mListView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//                @Override
//                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                    Log.d("InputPanelManager", "onInterceptTouchEvent");
//                    return false;
//                }
//
//                @Override
//                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                    Log.d("InputPanelManager", "onTouchEvent");
//                    manualHintMorePanel();
//                }
//
//                @Override
//                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//                    Log.d("InputPanelManager", "onRequestDisallowInterceptTouchEvent");
//                }
//            });
        }
        return this;
    }

    private void manualHintMorePanel() {
        if (isNotDoubleClick()) {
            if (mMorePanelState == MORE_PANEL_STATE_SHOW) {
                hideSoftInput();
                mVoiceBottom.postDelayed(this::hintMorePanel, 50);
            }
        }
    }

    public InputPanelManager bindSendBtn(Button sendButton) {
        mSendButton = sendButton;
        return this;
    }


    public interface OnMotionClickListenerCallback {
        void onMotionClick(int motionClickState);
    }

    public interface OnVoiceClickListenerCallback {
        void onVoiceClick(int voiceClickState);
    }
}
