package com.myapp.beatify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class MediaPlayerActivity extends AppCompatActivity {
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Log.e("MediaPlayerActivity", "onCreate()");

        mSeekBar = findViewById(R.id.seekBar);

        MediaEventBus.getInstance().getFragmentEventObservable().subscribe(new Observer<MediaEvent>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("Info", "onSubscribe()");
            }

            @Override
            public void onNext(@NonNull MediaEvent mediaEvent) {
                Log.e("Infoo", "onNext()");
                if (mediaEvent.getACTION() == MediaEventBus.ACTION_MUSIC_PLAYED_FROM_FRAGMENT) {
                    Log.e("Info", "onNext()");
                    Toast.makeText(MediaPlayerActivity.this, "Listening!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("MediaPlayerActivity  ", "error", e);

            }

            @Override
            public void onComplete() {

            }
        });

    }
}