package com.example.cardchain;

public class Model {
    private int image;
    private boolean showingCode=false;
    private String cardname;
    private String cardnumber;

    public String getCardHoldName() {
        return cardHoldName;
    }

    public void setCardHoldName(String cardHoldName) {
        this.cardHoldName = cardHoldName;
    }

    private String cardHoldName;
    private String barcode;
    private String barcodeType;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setShowingCode(boolean showingCode) {
        this.showingCode = showingCode;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public boolean isShowingCode(){ return showingCode;}

    public void toggle(){this.showingCode=!this.showingCode;}

    public Model(int image, String cardHoldName, String cardname, String cardnumber, String barcode, String barcodeType){

        this.image = image;
        this.cardname = cardname;
        this.cardnumber = cardnumber;
        this.barcode = barcode;
        this.cardHoldName = cardHoldName;
        this.barcodeType = barcodeType;
    }
}
