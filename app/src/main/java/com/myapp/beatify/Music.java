package com.myapp.beatify;

import java.util.List;

public class Music {
    private String title;
    private String url;
    private String category;
    private List<String> artists;
    private String musicUrl;

    public Music() {
    }

    public Music(String title, String url, String category, List<String> artists, String musicUrl) {
        this.title = title;
        this.url = url;
        this.category = category;
        this.artists = artists;
        this.musicUrl = musicUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getMusicUrl() {
        return musicUrl;
    }
}
