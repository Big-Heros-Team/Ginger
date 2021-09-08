package com.ginger.Entities;

public class MealDetails {

    private String idMeal;

    private String strMeal;

    private String strCategory;

    private String strArea;

    private String strInstructions;

    private String strMealThumb;

    private String strYoutube;

    private String strSource;

    private String strIngredient1;

    private String strIngredient2;

    private String strIngredient3;

    private String strIngredient4;

    private String strIngredient5;

    private String strIngredient6;

    private String strIngredient7;

    private String strIngredient8;

    private String strIngredient9;

    private String strIngredient10;

    private String strMeasure1;

    private String strMeasure2;

    private String strMeasure3;

    private String strMeasure4;

    private String strMeasure5;

    private String strMeasure6;

    private String strMeasure7;

    private String strMeasure8;

    private String strMeasure9;

    private String strMeasure10;


    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public String getStrSource() {
        return strSource;
    }

    public String getStrIngredient(int i) {
        switch (i) {
            case 1:
                return strIngredient1;

            case 2:
                return strIngredient2;
            case 3:
                return strIngredient3;
            case 4:
                return strIngredient4;
            case 5:
                return strIngredient5;
            case 6:
                return strIngredient6;
            case 7:
                return strIngredient7;
            case 8:
                return strIngredient8;
            case 9:
                return strIngredient9;
            case 10:
                return strIngredient10;

        }
        return null;
    }


    public String getStrMeasure (int i) {
        switch (i){

            case 1:
                return strMeasure1;
            case 2:
                return strMeasure2;
            case 3:
                return strMeasure3;
            case 4:
                return strMeasure4;
            case 5:
                return strMeasure5;
            case 6:
                return strMeasure6;
            case 7:
                return strMeasure7;
            case 8:
                return strMeasure8;
            case 9:
                return strMeasure9;
            case 10:
                return strMeasure10;

        }
        return null;
    }

}
