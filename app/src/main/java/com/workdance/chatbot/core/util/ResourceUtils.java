package com.workdance.chatbot.core.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.appcompat.content.res.AppCompatResources;

public class ResourceUtils {
    public static ColorStateList getColorStateListAttrRes(Context context, TypedArray typedArray, @StyleableRes int styleableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return typedArray.getColorStateList(styleableResId);
        } else {
            int resourceId = typedArray.getResourceId(styleableResId, -1);
            if (resourceId != -1) {
                return AppCompatResources.getColorStateList(context, resourceId);
            }
        }
        return null;
    }

    public static int getDimensionPixelSize(@NonNull Context context, @DimenRes int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }
}
