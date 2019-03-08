package com.example.omsairam01.goggles.model;

import java.io.Serializable;

public class ReqModel implements Serializable {


    private String id;
    private String name;
    private String description;
    private String price;
    private String date;



    public ReqModel() {
        this.id = id;

        this.name = name;
        this.description = description;
        this.price = price;
        this.date=date;

    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
