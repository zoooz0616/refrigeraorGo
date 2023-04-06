package com.example.refrigeratorgo;

import java.util.ArrayList;

//item의 TextView 2개와 ImageView 1개에 들어갈 data를 저장할 class.
public class RecipesData extends ArrayList<RecipesData> {

    private String imgUrl;
    private String foodName;
    private String scrap;
    private String detail_url;

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getScrap() {
        return scrap;
    }

    public void setScrap(String scrap) {
        this.scrap = scrap;
    }

    public RecipesData(String imgUrl, String foodName, String scrap, String detail_url) {
        this.imgUrl = imgUrl;
        this.foodName = foodName;
        this.scrap = scrap;
        this.detail_url = detail_url;
    }
}
