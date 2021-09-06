package com.ginger.filter;

import com.google.gson.annotations.SerializedName;

public class MealItem {

    @SerializedName("id")
    private Long mealItemId;
    private String title;
    private String image;

    public Long getMealItemId() {
        return mealItemId;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
