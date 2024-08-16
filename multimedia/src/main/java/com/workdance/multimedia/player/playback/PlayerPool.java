package com.workdance.multimedia.player.playback;

import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.workdance.core.AppKit;
import com.workdance.core.util.LogUtils;
import com.workdance.multimedia.player.AVPlayer;
import com.workdance.multimedia.player.Player;
import com.workdance.multimedia.player.source.MediaSource;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerPool {
    private final Map<String, Player> mAcquiredPlayers = Collections.synchronizedMap(new LinkedHashMap<>());
    public static final String TAG = "PlayerPool";
    public Player acquire(@NonNull MediaSource source) {
        Context context = AppKit.getAppKit().getContext();
        Player player = get(source);
        if (player != null) {
            if (player.isError() || player.isReleased()) {
                recycle(player);
                player = null;
            }
        }
        if (player == null) {
            player = new AVPlayer(context, source, Looper.getMainLooper());
        }

        return player;
    }

    public void recycle(@NonNull Player player) {
        LogUtils.d(this, "recycle", player.getDataSource(), player);
        synchronized (mAcquiredPlayers) {
            Map<String, Player> copy = new LinkedHashMap<>(mAcquiredPlayers);
            for (Map.Entry<String, Player> entry : copy.entrySet()) {
                if (entry.getValue() == player) {
                    mAcquiredPlayers.remove(entry.getKey());
                }
            }
        }
        player.release();
    }

    public Player get(@NonNull MediaSource source) {
        return mAcquiredPlayers.get(key(source));
    }

    private String key(@NonNull MediaSource mediaSource) {
        return mediaSource.getUniqueId();
    }

    private void recycle(@NonNull MediaSource source) {
        Player o = mAcquiredPlayers.remove(key(source));
        if (o != null) {
            LogUtils.d(this, "recycle by player ", source, o);
        }
    }
}
