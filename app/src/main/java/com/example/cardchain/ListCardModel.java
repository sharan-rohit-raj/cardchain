package com.example.cardchain;

public class ListCardModel {
    private String cardname;
    private String cardnumber;
    private int imageID;
    private String barcode;
    private String barcodeType;
    private boolean showingCode=false;



    public String getCardname() {
        return cardname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public int getImageID() {
        return imageID;
    }

    public String getBarcode(){return barcode;}

    public String getBarcodeType(){return barcodeType; }
    public boolean isShowingCode(){
        return showingCode;
    }
    public void toggleCard(){
        showingCode=!showingCode;
    }

    public ListCardModel(String cardnumber, String cardname, int imageID, String barcode, String barcodeType){
        this.cardnumber = cardnumber;
        this.cardname = cardname;
        this.imageID = imageID;
        this.barcode=barcode;
        this.barcodeType=barcodeType;

    }
}
