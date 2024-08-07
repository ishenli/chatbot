package com.workdance.chatbot.remote.dto;


import lombok.Data;

@Data
public class BaseResult<T> {
    private boolean success;
    private T data;
}
