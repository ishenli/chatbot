package com.workdance.chatbot.remote.dto.rep;

import lombok.Data;

@Data
public class DramaRep {
    private String dramaId;
    private String dramaTitle;
    private String description;
    private String coverUrl;
    private int totalEpisodeNumber;
    private int latestEpisodeNumber;
    private String authorId;
}
