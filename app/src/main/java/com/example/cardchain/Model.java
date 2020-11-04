package com.example.cardchain;

public class Model {
    private int image;
    private boolean showingCode=false;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isShowingCode(){ return showingCode;}

    public void toggle(){this.showingCode=!this.showingCode;}

    public Model(int image){
        this.image = image;
    }
}
