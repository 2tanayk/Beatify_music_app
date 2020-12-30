package com.myapp.beatify;

import android.media.MediaPlayer;

public class MediaEvent {
    private int ACTION;
    private MediaPlayer rMediaPlayer;

    public MediaEvent(int ACTION, MediaPlayer rMediaPlayer) {
        this.ACTION = ACTION;
        this.rMediaPlayer = rMediaPlayer;
    }

    public MediaPlayer getrMediaPlayer() {
        return rMediaPlayer;
    }

    public int getACTION() {
        return ACTION;
    }
}
