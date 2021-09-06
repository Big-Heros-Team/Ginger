package com.ginger;

import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("id")
    private int  apiId;

    private String title;

    private String image;

    public int getApiId() {
        return apiId;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

}
