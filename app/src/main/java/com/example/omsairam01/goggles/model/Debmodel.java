package com.example.omsairam01.goggles.model;

import java.io.Serializable;

public class Debmodel implements Serializable {

    private String id;
    private String name;
    private String img;
    private String price;
    private String payed;
    private String unpayed;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public String getUnpayed() {
        return unpayed;
    }

    public void setUnpayed(String unpayed) {
        this.unpayed = unpayed;
    }
}
