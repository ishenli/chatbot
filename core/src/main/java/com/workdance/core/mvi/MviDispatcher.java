package com.workdance.core.mvi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MviDispatcher<T> extends ViewModel implements DefaultLifecycleObserver {
    private final static int DEFAULT_QUEUE_LENGTH = 10;
    private final HashMap<Integer, LifecycleOwner> mOwner = new HashMap<>();
    private final HashMap<Integer, LifecycleOwner> mFragmentOwner = new HashMap<>();
    private final HashMap<Integer, Observer<T>> mObservers = new HashMap<>();
    private final FixedLengthList<OneTimeMessage<T>> mResults = new FixedLengthList<>();

    protected int initQueueMaxLength() {
        return DEFAULT_QUEUE_LENGTH;
    }

    public final void output(@NonNull AppCompatActivity activity, @NonNull Observer<T> observer) {
        activity.getLifecycle().addObserver(this);
        Integer identityId = System.identityHashCode(activity);
        outputTo(identityId, activity, observer);
    }

    public final void output(@NonNull Fragment fragment, @NonNull Observer<T> observer) {
        fragment.getLifecycle().addObserver(this);
        Integer identityId = System.identityHashCode(fragment);
        this.mFragmentOwner.put(identityId, fragment);
        outputTo(identityId, fragment.getViewLifecycleOwner(), observer);
    }

    private void outputTo(Integer identityId, LifecycleOwner owner, Observer<T> observer) {
        this.mOwner.put(identityId, owner);
        this.mObservers.put(identityId, observer);
        for (OneTimeMessage<T> result : mResults) {
            result.observe(owner, observer);
        }
    }

    protected final void sendResult(@NonNull T intent) {
        mResults.init(initQueueMaxLength(), mutableResult -> {
            for (Map.Entry<Integer, Observer<T>> entry : mObservers.entrySet()) {
                Observer<T> observer = entry.getValue();
                mutableResult.removeObserver(observer);
            }
        });
        boolean eventExist = false;
        for (OneTimeMessage<T> result : mResults) {
            int id1 = System.identityHashCode(result.get());
            int id2 = System.identityHashCode(intent);
            if (id1 == id2) {
                eventExist = true;
                break;
            }
        }
        if (!eventExist) {
            OneTimeMessage<T> result = new OneTimeMessage<>(intent);
            for (Map.Entry<Integer, Observer<T>> entry : mObservers.entrySet()) {
                Integer key = entry.getKey();
                Observer<T> observer = entry.getValue();
                LifecycleOwner owner = mOwner.get(key);
                assert owner != null;
                result.observe(owner, observer);
            }
            mResults.add(result);
        }

        OneTimeMessage<T> result = null;
        for (OneTimeMessage<T> r : mResults) {
            int id1 = System.identityHashCode(r.get());
            int id2 = System.identityHashCode(intent);
            if (id1 == id2) {
                result = r;
                break;
            }
        }
        if (result != null) result.set(intent);
    }

    public final void input(T intent) {
        onHandle(intent);
    }

    protected void onHandle(T intent) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onDestroy(owner);
        boolean isFragment = owner instanceof Fragment;
        for (Map.Entry<Integer, LifecycleOwner> entry : isFragment ? mFragmentOwner.entrySet() : mOwner.entrySet()) {
            LifecycleOwner owner1 = entry.getValue();
            if (owner1.equals(owner)) {
                Integer key = entry.getKey();
                mOwner.remove(key);
                if (isFragment) mFragmentOwner.remove(key);
                for (OneTimeMessage<T> mutableResult : mResults) {
                    mutableResult.removeObserver(Objects.requireNonNull(mObservers.get(key)));
                }
                mObservers.remove(key);
                break;
            }
        }
        if (mObservers.size() == 0) mResults.clear();
    }
}