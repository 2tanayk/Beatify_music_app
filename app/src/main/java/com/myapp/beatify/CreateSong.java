package com.myapp.beatify;

public class CreateSong {
    private int img;
    private String txt;

    public CreateSong(int img, String txt) {
        this.img = img;
        this.txt = txt;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getImg() {
        return img;
    }

    public String getTxt() {
        return txt;
    }
}
