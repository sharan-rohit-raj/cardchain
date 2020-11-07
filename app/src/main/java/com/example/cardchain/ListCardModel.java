package com.example.cardchain;

public class ListCardModel {
    private String cardname;

    public String getCardname() {
        return cardname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public int getImageID() {
        return imageID;
    }

    private String cardnumber;
    private int imageID;

    public ListCardModel(String cardnumber, String cardname, int imageID){
        this.cardnumber = cardnumber;
        this.cardname = cardname;
        this.imageID = imageID;
    }
}
