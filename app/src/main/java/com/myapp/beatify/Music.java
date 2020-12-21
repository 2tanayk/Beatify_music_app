package com.myapp.beatify;

import java.util.List;

public class Music {
    private String title;
    private String url;
    private String category;
    private List<String> artists;

    public Music() {
    }

    public Music(String title, String url, String category, List<String> artists) {
        this.title = title;
        this.url = url;
        this.category = category;
        this.artists = artists;
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
}
