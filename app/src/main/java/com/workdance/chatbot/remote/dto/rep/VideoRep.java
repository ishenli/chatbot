package com.workdance.chatbot.remote.dto.rep;

import lombok.Data;

@Data
public class VideoRep {
    private String videoId;
    private String title;
    private String coverUrl;
    private String videoUrl;
    private long duration;
}
