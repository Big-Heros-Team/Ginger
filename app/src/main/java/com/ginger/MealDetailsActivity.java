package com.ginger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ginger.Entities.Meal;
import com.ginger.Entities.MealDetails;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Retrofit.FoodApi;
import com.ginger.adapters.IngredientsAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealDetailsActivity extends AppCompatActivity {
    FoodApi foodApi;
    List<MealDetails> mealDetailsList;
    Handler handler;
    List<String> list;
    IngredientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        foodApi = retrofit.create(FoodApi.class);

        Intent intent = getIntent();
        String id = intent.getExtras().getString(MainActivity.MEAL_ID);

        handler = new Handler();
        // Get meal By ID
        handler.post(() -> getMealById(id));


    }

    public void getMealById(String id) {
        Call<MealDetailsList> mealDetailsCall = foodApi.getMealById(id);

        mealDetailsCall.enqueue(new Callback<MealDetailsList>() {
            @Override
            public void onResponse(Call<MealDetailsList> call, Response<MealDetailsList> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "unSuccessful: " + response.code());
                    return;
                }

                MealDetailsList mealsList = response.body();
                assert mealsList != null;
                Log.i("API", "onSuccessful: " + mealsList.getMeals().get(0).getStrMeal());
                mealDetailsList = mealsList.getMeals();
                handler.post(() -> {


                    ImageView mealImage = findViewById(R.id.imageView);
                    Uri uri = Uri.parse(mealDetailsList.get(0).getStrMealThumb());
                    Picasso.with(MealDetailsActivity.this).load(uri).into(mealImage);

                    list = new ArrayList<>();

                    for (int i = 0; i <= 9; i++) {
                        if (mealDetailsList.get(0).getStrIngredient(i + 1) != null) {
                            if (mealDetailsList.get(0).getStrMeasure(i+1)==null || mealDetailsList.get(0).getStrMeasure(i + 1).equals("")) {
                                list.add(mealDetailsList.get(0).getStrIngredient(i + 1));
                            }
                          else {
                              list.add(mealDetailsList.get(0).getStrIngredient(i + 1) + " : " + mealDetailsList.get(0).getStrMeasure(i + 1));
                            }
                        }
                    }

                    adapter = new IngredientsAdapter(list, new IngredientsAdapter.OnTaskItemClickListener() {
                        @Override
                        public void onItemClicked(int position) {

                        }
                    });
                    RecyclerView recyclerView = findViewById(R.id.rv_ingredients);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            MealDetailsActivity.this,
                            RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);


                });

            }

            @Override
            public void onFailure(Call<MealDetailsList> call, Throwable t) {

            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap image = null;
        try {
            URL url = new URL(src);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        return image;

    }
}