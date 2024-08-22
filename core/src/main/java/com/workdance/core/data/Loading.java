package com.workdance.core.data;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lombok.Data;

@Data
public class Loading {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOADING_START,
            LOADING_DOING,
            LOADING_END})
    @interface LOADING_STATE {
    }

    public static final int LOADING_START = 0;
    public static final int LOADING_DOING = 1;
    public static final int LOADING_END = 2;

    @LOADING_STATE
    private int loadingState = LOADING_START;
}
