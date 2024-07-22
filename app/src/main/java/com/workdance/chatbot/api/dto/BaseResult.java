package com.workdance.chatbot.api.dto;


import lombok.Data;

@Data
public class BaseResult<T> {
    private boolean success;
    private T data;
}
