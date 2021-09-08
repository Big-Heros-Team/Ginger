package com.ginger.Retrofit;

import com.ginger.Entities.CategoryList;
import com.ginger.Entities.MealItemDetails;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Entities.MealsList;
import com.ginger.filter.MealFilterItemDetails;
import com.ginger.filter.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("categories.php")
    Call<CategoryList> getCategoryList();

    @GET("filter.php")
    Call<MealsList> getMealsList(@Query("c") String category);

    @GET("lookup.php")
    Call<MealDetailsList> getMealById(@Query("i") String mealId);


    @GET("complexSearch")
    Call<Results> getResults(@Query("apiKey") String apiKey,
                             @Query("includeIngredients") String includeIngredients,
                             @Query("type") String type,
                             @Query("diet") String diet,
                             @Query("cuisine") String cuisine,
                             @Query("maxReadyTime") String maxReadyTime,
                             @Query("minCalories") String minCalories,
                             @Query("maxCalories") String maxCalories
    );

    @GET("{id}/information")
    Call<MealFilterItemDetails> getMealItemDetailsById(@Path("id") String id, @Query("apiKey") String apiKey);

}
