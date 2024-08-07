package com.workdance.core.widget.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class RoundButton extends AppCompatButton {

    public RoundButton(Context context) {
        super(context);
        init(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        RoundDrawable bg = RoundDrawable.fromAttributeSet(context, attrs);
        setBackgroundDrawable(bg);
    }
}