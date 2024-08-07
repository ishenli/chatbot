package com.workdance.chatbot.remote.dto;


import lombok.Data;

@Data
public class MediaResult<T> {
    private ResponseMetadata responseMetadata;
    private T result;

    public static class ResponseMetadata {
        private String requestId;
        private String action;
        private String service;
    }
}
