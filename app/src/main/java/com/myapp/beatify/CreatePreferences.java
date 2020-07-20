package com.myapp.beatify;

public class CreatePreferences {
    //Model class for recycler view
    private int img;
    private String txt;

    public CreatePreferences(String txt, int img) {
        this.txt = txt;
        this.img = img;
    }//constructor ends

  //setter methods
    public void setTxt(String txt) {
        this.txt = txt;
    }//setTxt ends

    public void setImg(int img) {
        this.img = img;
    }//setImg ends

    //getter methods
    public String getTxt() {
        return txt;
    }//getTxt ends

    public int getImg() {
        return img;
    }//getImg ends
}//class ends
