package com.myapp.beatify;

public class CreatePreferences {
    private int img;
    private String txt;

    public CreatePreferences(String txt,int img) {
        this.txt = txt;
        this.img=img;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTxt() {
        return txt;
    }

    public int getImg() {
        return img;
    }
}
