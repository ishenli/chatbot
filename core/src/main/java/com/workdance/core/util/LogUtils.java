package com.workdance.core.util;

import android.util.Log;

public class LogUtils {
    static final String TAG = "LogUtils";
    public static boolean ENABLE_LOG = false;
    public static void d(Object o, String method, Object... messages) {
        if (ENABLE_LOG) {
            Log.d(TAG, createLog(o, method, messages));
        }
    }

    public static void w(Object o, String method, Object... messages) {
        if (ENABLE_LOG) {
            Log.w(TAG, createLog(o, method, messages));
        }
    }

    private static String createLog(Object o, String method, Object... messages) {
        StringBuilder msg = new StringBuilder("[" + obj2String(o) + "]").append(" -> ").append(method);
        if (messages != null) {
            for (Object message : messages) {
                msg.append(" -> ").append(obj2String(message));
            }
        }
        return msg.toString();
    }

    public static String string(Object o) {
        if (o == null) return "null";
        if (ENABLE_LOG) {
            return o.toString();
        } else {
            return "";
        }
    }

    public static String obj2String(Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Boolean) {
            return String.valueOf(o);
        } else if (o instanceof Number) {
            return String.valueOf(o);
        } else if (o.getClass().isAnonymousClass()) {
            String s = o.toString();
            return s.substring(s.lastIndexOf('.'));
        } else if (o instanceof Class<?>) {
            return ((Class<?>) o).getSimpleName();
        } else {
            return o.getClass().getSimpleName() + '@' + Integer.toHexString(o.hashCode());
        }
    }
}
