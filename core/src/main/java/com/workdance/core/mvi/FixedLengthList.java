package com.workdance.core.mvi;

import java.util.LinkedList;

public class FixedLengthList<T> extends LinkedList<T> {
    private int maxLength;
    private boolean hasBeenInit;
    private QueueCallback<T> queueCallback;

    public final void init(int maxLength, QueueCallback<T> queueCallback) {
        if (!hasBeenInit) {
            this.maxLength = maxLength;
            this.queueCallback = queueCallback;
            hasBeenInit = true;
        }
    }

    @Override
    public boolean add(T t) {
        if (size() + 1 > maxLength) {
            T t1 = super.removeFirst();
            if (queueCallback != null) queueCallback.onRemoveFirst(t1);
        }
        return super.add(t);
    }

    public interface QueueCallback<T> {
        void onRemoveFirst(T t);
    }
}