package com.workdance.core.dto;

import java.util.Objects;

public class OperateResult<T> {
    T result;
    String code;

    public OperateResult(T result, String code) {
        this.result = result;
        this.code = code;
    }

    public OperateResult(T result) {
        this.result = result;
        this.code = "success";
    }


    public boolean isSuccess() {
        return Objects.equals(code, "success");
    }

    public T getResult() {
        return result;
    }

    public String getErrorCode() {
        return code;
    }
}

