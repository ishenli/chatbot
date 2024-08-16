package com.workdance.core.util;

import android.content.Context;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;

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

    public static String getMD5(String s) {
        if (s == null) return "";
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            return (new BigInteger(1, md.digest())).toString(16);
        } catch (Exception e) {
            return result;
        }
    }
}
