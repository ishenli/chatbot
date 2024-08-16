package com.workdance.multimedia.player.source;

import com.workdance.core.util.LogUtils;

import java.io.Serializable;

import lombok.Data;

@Data
public class SubtitleText implements Serializable {
    private long pts;
    private long duration;
    private String text;

    @Override
    public String toString() {
        return "SubtitleText{" +
                "pts=" + pts +
                ", duration=" + duration +
                ", text='" + text + '\'' +
                '}';
    }

    public static String dump(SubtitleText text) {
        if (!LogUtils.ENABLE_LOG) return null;
        if (text == null) return null;

        return text.pts + " " + text.duration + " " + text.text;
    }
}
