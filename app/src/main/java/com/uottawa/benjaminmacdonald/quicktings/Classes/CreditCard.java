package com.uottawa.benjaminmacdonald.quicktings.Classes;

/**
 * Created by BenjaminMacDonald on 2017-07-14.
 */

public class CreditCard {

    private String cardNumber;
    private String expire;
    private String name;
    private String cvv;
    private String type;


    public CreditCard() {
        this.cardNumber = "";
        this.expire = "";
        this.name = "";
        this.cvv = "";
        this.type = "";
    }

    public CreditCard(String name, String cardNumber, String expire, String cvv, String type) {
        this.cardNumber = cardNumber;
        this.expire = expire;
        this.name = name;
        this.cvv = cvv;
        this.type = type;
    }



    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }
}
