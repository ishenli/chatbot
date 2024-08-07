package com.workdance.chatbot.remote.dto;

import lombok.Data;

@Data
public class StreamEvent {
    private StreamEventEnum status;
    private String data;
}
