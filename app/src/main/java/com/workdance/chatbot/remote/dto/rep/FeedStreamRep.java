package com.workdance.chatbot.remote.dto.rep;

import com.workdance.chatbot.ui.multimedia.model.DramaInfo;

import lombok.Data;
@Data
public class FeedStreamRep {
    private String vid;
    private String caption;
    private double duration;
    private String coverUrl;
    private String playAuthToken;
    private String subtitleAuthToken;
    private EpisodeDetail episodeDetail;

    public static class EpisodeDetail {
        private int episodeNumber;
        private String episodeDesc;
        private DramaInfo dramaInfo;
    }
}