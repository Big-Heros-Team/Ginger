package com.ginger.filter;

import java.util.List;

public class MealItemDetails {

    private String title;
    private String image;
    List<Ingredient> extendedIngredients;
    private String readyInMinutes;
    private String sourceUrl;
    private String pricePerServing;
    private String servings;

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public List<Ingredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    public String getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getPricePerServing() {
        return pricePerServing;
    }

    public String getServings() {
        return servings;
    }
}
