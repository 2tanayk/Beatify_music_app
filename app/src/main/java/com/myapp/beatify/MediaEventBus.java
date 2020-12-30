package com.myapp.beatify;

import android.util.Log;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MediaEventBus {
    private static final String TAG = MediaEventBus.class.getSimpleName();
    private static final String TAG2 = MediaEventBus.class.getCanonicalName();

    public static final int ACTION_MUSIC_PLAYED_FROM_FRAGMENT = 1;
    public static final int ACTION_MUSIC_CONTROLLED_FROM_ACTIVITY = 2;

    //public MediaEvent mEvent=new MediaEvent(AC)

    private static MediaEventBus mInstance;

    public static MediaEventBus getInstance() {
        if (mInstance == null) {
            mInstance = new MediaEventBus();
        }
        return mInstance;
    }

    private MediaEventBus() {

    }

    private PublishSubject<MediaEvent> fragmentEventSubject = PublishSubject.create();

    public Observable<MediaEvent> getFragmentEventObservable() {
        return fragmentEventSubject;
    }

    public void postFragmentAction(MediaEvent mediaEvent) {
        Log.e("EventBus", "postFragmentAction()");
        fragmentEventSubject.onNext(mediaEvent);
    }
}
