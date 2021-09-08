package com.ginger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ginger.Retrofit.FoodApi;
import com.ginger.filter.Results;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowFilteredMealsActivity extends AppCompatActivity {
    private MealItemAdapter adapter;

    private String ingredients;
    private String maxReadyTime;
    private String cuisine;
    private String diet;
    private String minCalories;
    private String maxCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_filtered_meals);


        Intent intent = getIntent();
        ingredients = intent.getExtras().getString("ingredients");
        maxReadyTime = intent.getExtras().getString("maxReadyTime");
        cuisine = intent.getExtras().getString("cuisine");
        diet = intent.getExtras().getString("diet");
        minCalories = intent.getExtras().getString("minCalories");
        maxCalories = intent.getExtras().getString("maxCalories");

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFilteredMealsFromApi();
    }

    public void getFilteredMealsFromApi() {
        // Get Meals
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);

        String apiKey = "11e60bd17b7648619ed45bb0dbf4e838";
        Call<Results> mealsListCall = foodApi.getResults(
                apiKey,
                ingredients,
                diet,
                cuisine,
                maxReadyTime,
                minCalories,
                maxCalories);

        mealsListCall.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (!response.isSuccessful()) {
                    Log.i("Show Filter API", "unSuccessful: " + response.code());
                    return;
                }
                Results mealsList = response.body();
                if (mealsList.getResults().size() == 0) {
                    Log.i("Show Filter API", "nothing found: ");
                } else {
                    //todo: create recycle view here
                    createMealsItemRecyclerView( mealsList);
                    Log.i("Show Filter API", "onSuccessful: " + mealsList.getResults().get(0).getTitle());
                    Log.i("Show Filter API", "onSuccessful: " + mealsList.getResults().size());
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.i("Show Filter API", "onFailure: " + t.getMessage());
            }
        });
    }


    public void createMealsItemRecyclerView(Results mealsList) {
        Log.i("RecyclerView", "createMealsItemRecyclerView: " + mealsList.getResults().get(0).getTitle());

        RecyclerView mealsRecyclerView = findViewById(R.id.ShowFilteredMealsList);
        adapter = new MealItemAdapter(mealsList.getResults(), new MealItemAdapter.OnMealItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.i("onItemClicked -> ", mealsList.getResults().get(position).getTitle());
                Intent goToDetailsIntent = new Intent(getApplicationContext(), MainActivity.class);
                goToDetailsIntent.putExtra("mealName", mealsList.getResults().get(position).getTitle());
                goToDetailsIntent.putExtra("mealId", mealsList.getResults().get(position).getMealItemId());
                goToDetailsIntent.putExtra("mealImage", mealsList.getResults().get(position).getImage());
                startActivity(goToDetailsIntent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        mealsRecyclerView.setLayoutManager(linearLayoutManager);
        mealsRecyclerView.setAdapter(adapter);
    }

}