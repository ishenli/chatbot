package com.workdance.multimedia.player.cache;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.workdance.core.util.StringUtils;
import com.workdance.multimedia.player.source.MediaSource;
import com.workdance.multimedia.player.source.Track;

public interface CacheKeyFactory {

    CacheKeyFactory DEFAULT = new CacheKeyFactory() {
        @Override
        public String generateCacheKey(@NonNull MediaSource source, @NonNull Track track) {
            if (!TextUtils.isEmpty(track.getFileHash())) {
                return track.getFileHash();
            }
            if (!TextUtils.isEmpty(track.getFileId())) {
                return track.getFileId();
            }
            String fileHash = generateCacheKey(track.getUrl());
            track.setFileHash(fileHash);
            return fileHash;
        }

        @Override
        public String generateCacheKey(@NonNull String url) {
            return StringUtils.getMD5(url);
        }
    };

    String generateCacheKey(@NonNull MediaSource source, @NonNull Track track);

    String generateCacheKey(@NonNull String url);
}
