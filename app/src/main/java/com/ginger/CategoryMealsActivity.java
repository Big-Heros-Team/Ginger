package com.ginger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ginger.Entities.MealsList;
import com.ginger.Retrofit.FoodApi;
import com.ginger.adapters.MealAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryMealsActivity extends AppCompatActivity {
    private MealAdapter adapter;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_meals);

        Intent intent = getIntent();
        category = intent.getExtras().getString("category");
        setTitle(category + " Category Meals");

        ((TextView)findViewById(R.id.categoryMealsListHeader)).setText(category + " Category Meals");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get Meals list by Category
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);

        Call<MealsList> mealsListCall = foodApi.getMealsList(category);
        mealsListCall.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(Call<MealsList> call, Response<MealsList> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "unSuccessful: " + response.code());
                    return;
                }
                MealsList mealsList = response.body();
                createMealsRecyclerView( mealsList);
                Log.i("API", "onSuccessful: " + mealsList.getMeals().get(0).getStrMeal());
            }

            @Override
            public void onFailure(Call<MealsList> call, Throwable t) {
                Log.i("API", "onFailure: " + t.getMessage());
            }
        });
    }


    public void createMealsRecyclerView(MealsList mealsList) {
        RecyclerView mealsRecyclerView = findViewById(R.id.categoryMealsList);
        adapter = new MealAdapter(mealsList.getMeals(), new MealAdapter.OnTaskItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                // todo: >>>>>>>>>>>>>>>>> change MainActivity to yousef activity
                Log.i("onItemClicked -> ", mealsList.getMeals().get(position).getStrMeal());
                        Intent goToDetailsIntent = new Intent(getApplicationContext(), MainActivity.class);
                        goToDetailsIntent.putExtra("mealName", mealsList.getMeals().get(position).getStrMeal());
                        goToDetailsIntent.putExtra("mealId", mealsList.getMeals().get(position).getIdMeal());
                        goToDetailsIntent.putExtra("mealImage", mealsList.getMeals().get(position).getStrMealThumb());
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