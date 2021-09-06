package com.ginger.Retrofit;

import com.ginger.Entities.CategoryList;
import com.ginger.Entities.Meal;
import com.ginger.Entities.MealDetails;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Entities.MealsList;
import com.ginger.filter.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("categories.php")
    Call<CategoryList> getCategoryList();

    @GET("filter.php")
    Call<MealsList> getMealsList(@Query("c") String category);

    @GET("lookup.php")
    Call<MealDetailsList> getMealById(@Query("i") String mealId);

    @GET("complexSearch")
    Call<Results> getResults(@Query("apiKey") String apiKey, @Query("query") String query,@Query("diet") String diet, @Query("cuisine") String cuisine);


//    @GET("complexSearch")
//    Call<Results> getResults(@Query("apiKey") String apiKey, @Query("query") String query, @Query("number") String number);
//
//
//    @GET("complexSearch")
//    Call<Result> getResult(@Query("apiKey") String apiKey, @Query("query") String query, @Query("number") String number);

}
