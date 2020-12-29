package com.myapp.beatify;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MediaEventBus {
    private static final String TAG = MediaEventBus.class.getSimpleName();
    private static final String TAG2 = MediaEventBus.class.getCanonicalName();

    public static final int ACTION_MUSIC_PLAYED_FROM_FRAGMENT = 1;
    public static final int ACTION_MUSIC_OTHER = 2;

    private static MediaEventBus mInstance;

    public static MediaEventBus getInstance() {
        if (mInstance == null) {
            mInstance = new MediaEventBus();
        }
        return mInstance;
    }

    private MediaEventBus() {
    }

    private PublishSubject<Integer> fragmentEventSubject = PublishSubject.create();

    public Observable<Integer> getFragmentEventObservable() {
        return fragmentEventSubject;
    }

    public void postFragmentAction(Integer actionId) {
        fragmentEventSubject.onNext(actionId);
    }
}
