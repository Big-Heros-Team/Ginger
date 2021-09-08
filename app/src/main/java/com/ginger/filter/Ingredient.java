package com.ginger.filter;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("image")
    private String ingredientImage;
    private String name;
    private String amount;
    private String unit;

    public String getIngredientImage() {
        return ingredientImage;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
