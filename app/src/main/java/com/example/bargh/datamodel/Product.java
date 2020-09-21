package com.example.bargh.datamodel;

import android.graphics.drawable.Drawable;

public class Product {

    private int id;
    private String name;
    private int price;
    private String info;
    private String productImageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String pic) {
        this.productImageUrl = pic;
    }
}
