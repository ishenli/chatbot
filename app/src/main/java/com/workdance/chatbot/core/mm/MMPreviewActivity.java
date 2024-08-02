package com.workdance.chatbot.core.mm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.workdance.chatbot.R;

import java.util.ArrayList;
import java.util.List;

public class MMPreviewActivity extends AppCompatActivity {
    private static List<MediaEntry> entries;
    private static int currentPosition;
    private SparseArray<View> views;
    private ViewPager viewPager;
    // private MMPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mm_preview);
        views = new SparseArray<>(3);
    }

    public static void previewImage(Context context, String imageUrl) {
        List<MediaEntry> entries = new ArrayList<>();
        MediaEntry entry = new MediaEntry();
        entry.setType(MediaEntry.TYPE_IMAGE);
        entry.setMediaUrl(imageUrl);
        entries.add(entry);
        previewMedia(context, entries, 0, false);
    }

    private static void previewMedia(Context context, List<MediaEntry> entries, int current, boolean secret) {
        if (entries == null || entries.isEmpty()) {
            return;
        }
        MMPreviewActivity.entries = entries;
        MMPreviewActivity.currentPosition = current;
        Intent intent = new Intent(context, MMPreviewActivity.class);
        context.startActivity(intent);
    }

}
