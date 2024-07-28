package com.workdance.chatbot.api.dto;

import lombok.Data;

@Data
public class StreamEvent {
    private StreamEventEnum status;
    private String data;
}
