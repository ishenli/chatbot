package com.example.wechat.widget;

import android.content.Context;
import android.view.MotionEvent;

public class ChatEditTextView extends androidx.appcompat.widget.AppCompatEditText {

    private OnChatEditTextTouchListener mOnChatEditTextTouchListener;
    public void setOnChatEditTextTouchListener(OnChatEditTextTouchListener listener) {
        mOnChatEditTextTouchListener = listener;
    }
    public ChatEditTextView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                requestFocus();
                break;
            case MotionEvent.ACTION_UP:
                if (mOnChatEditTextTouchListener != null) {
                    mOnChatEditTextTouchListener.onChatEditTouch();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public interface OnChatEditTextTouchListener {
        public void onChatEditTouch();
    }
}
