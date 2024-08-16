package com.workdance.multimedia.player.source;

import lombok.Data;

@Data
public class Subtitle {
    private int index;
    private int subtitleId;
    private String language;
    private int languageId;
    private String url;
    private long expire;
    private String format;
    private String subtitleDesc;
}
