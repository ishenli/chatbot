package com.workdance.core.util;

import android.content.Context;
import android.widget.TextView;

import io.noties.markwon.Markwon;


public class StringUtils {

    public static void identifyMarkdownExpression(Context context,
                                              TextView textView, String value) {
        final Markwon markwon = Markwon.create(context);
        markwon.setMarkdown(textView, value);
    }


    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
