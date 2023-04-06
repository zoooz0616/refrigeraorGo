package com.example.refrigeratorgo;

import java.io.Serializable;

public class Food implements Serializable {
    private int id;
    private String name;
    private String date;
    private byte[] image;
    private String category;
    private String memo;

    public Food(String name, String date, byte[] image, String category, String memo, int id) {
        this.name = name;
        this.date = date;
        this.image = image;
        this.category = category;
        this.memo = memo;
        this.id = id;
    }
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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.category = memo;
    }

}
