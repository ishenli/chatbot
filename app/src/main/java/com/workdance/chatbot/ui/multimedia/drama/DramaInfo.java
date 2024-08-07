package com.workdance.chatbot.ui.multimedia.drama;

import lombok.Data;

@Data
public class DramaInfo {
    public String dramaId; // 剧 id
    public String dramaTitle; // 剧名
    public String description; // 剧描述
    public String coverUrl; // 剧封面
    public int totalEpisodeNumber; // 集数
    public int latestEpisodeNumber; // 更新集数
    public String authorId;
}
