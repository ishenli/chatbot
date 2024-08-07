package com.workdance.chatbot.remote.dto;

public enum StreamEventEnum {
    Doing(0),
    Done(1);

    private int value;

    StreamEventEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
