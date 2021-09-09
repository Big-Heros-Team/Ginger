package com.ginger.filter;

import com.google.gson.annotations.SerializedName;

public class MealItem {

    @SerializedName("id")
    private String mealItemId;
    private String title;
    private String image;

    public String getMealItemId() {
        return mealItemId;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
