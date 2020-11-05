package com.example.cardchain;

public class ListCardModel {
    private String cardnumber;

    public String getCardnumber() {
        return cardnumber;
    }

    public String getCardholdername() {
        return cardholdername;
    }

    public int getImageID() {
        return imageID;
    }

    private String cardholdername;
    private int imageID;

    public ListCardModel(String cardnumber, String cardholdername, int imageID){
        this.cardholdername = cardholdername;
        this.cardnumber = cardnumber;
        this.imageID = imageID;
    }
}
