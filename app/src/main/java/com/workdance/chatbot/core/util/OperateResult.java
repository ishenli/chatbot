package com.workdance.chatbot.core.util;

import java.util.Objects;

public class OperateResult<T> {
    T result;
    String errorCode;

    public OperateResult(T result, String errorCode) {
        this.result = result;
        this.errorCode = errorCode;
    }

    public OperateResult(T result) {
        this.result = result;
        this.errorCode = "success";
    }

    public OperateResult(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return Objects.equals(errorCode, "success");
    }

    public T getResult() {
        return result;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

