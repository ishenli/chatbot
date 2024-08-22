package com.workdance.chatbot.ui.explore.viewModel;

import lombok.Data;

@Data
public class CircleCover {
    private String name;
    private String coverUrl;
    private String avatarUrl;

    public CircleCover() {
    }

    public CircleCover(String name, String coverUrl, String avatarUrl) {
        this.name = name;
        this.coverUrl = coverUrl;
        this.avatarUrl = avatarUrl;
    }
}
