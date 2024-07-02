package com.example.wechat.util;

import android.content.Context;

import java.io.IOException;

public class AssetLoader implements Loader {
    public Context context;

    public AssetLoader(Context context) {
        this.context = context;
    }

    public int load(String folderName){
        int size = 0;
        try {
            size = context.getAssets().list(folderName).length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

}
