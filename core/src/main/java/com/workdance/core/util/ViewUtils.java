package com.workdance.core.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ViewUtils {

    public static int dip2Px(Context context, int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }



    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setSystemBarTheme(
            Activity activity,
            int statusBarColor,
            boolean lightStatusBar,
            boolean immersiveStatusBar,
            int navigationBarColor,
            boolean lightNavigationBar,
            boolean immersiveNavigationBar) {

        Window window = activity.getWindow();

        int flags = window.getDecorView().getSystemUiVisibility();

        if (immersiveStatusBar) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }

        if (immersiveNavigationBar) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }

        if (lightNavigationBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags = (flags & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) | (0 & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }

        if (lightStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = (flags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) | (0 & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        window.getDecorView().setSystemUiVisibility(flags);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(navigationBarColor);
    }
}
