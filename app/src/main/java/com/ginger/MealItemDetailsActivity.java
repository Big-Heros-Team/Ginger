package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ginger.Retrofit.FoodApi;
import com.ginger.filter.MealItemDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealItemDetailsActivity extends AppCompatActivity {
    private final String apiKey = "11e60bd17b7648619ed45bb0dbf4e838";
    private String mealID = "716429";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_item_details);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        mealID = intent.getExtras().getString("mealID");
        getMealFromAPI();
    }


    public void getMealFromAPI() {
        // Get Meal
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/{mealID}/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);

        Call<MealItemDetails> mealItemDetailsCall = foodApi.getMealItemDetailsById(apiKey, mealID);

        mealItemDetailsCall.enqueue(new Callback<MealItemDetails>() {
            @Override
            public void onResponse(Call<MealItemDetails> call, Response<MealItemDetails> response) {
                if (!response.isSuccessful()) {
                    Log.i("Meal Details", "unSuccessful: " + response.code());
                    return;
                }
                MealItemDetails mealDetails = response.body();
                Log.i("Meal Details", mealDetails.getTitle());
            }

            @Override
            public void onFailure(Call<MealItemDetails> call, Throwable t) {
                Log.i("FilterAPI", "onFailure: " + t.getMessage());

            }
        });
    }
}