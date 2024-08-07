package com.workdance.core.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class InputViewUtils {

    public static void setSoftInputAdjustResize(Context context, boolean isAdjustResize) {
        if (isAdjustResize) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    public static void showSoftInput(EditText editText, Context context) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT, null);
        } catch (Exception e) {  //特殊机型异常
        }
        InputViewUtils.setSoftInputAdjustResize(context, true);
    }

    public static void hideKeyBoard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception var3) {
            Log.d("cm", "hideKeyBoard: " + var3);
        }
    }

}
