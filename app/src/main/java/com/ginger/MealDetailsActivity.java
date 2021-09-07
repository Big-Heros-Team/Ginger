package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ginger.Entities.Meal;
import com.ginger.Entities.MealDetails;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Retrofit.FoodApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealDetailsActivity extends AppCompatActivity {
    FoodApi foodApi;
    List<MealDetails> mealDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        foodApi = retrofit.create(FoodApi.class);

        // Get meal By ID
        getMealById("53050");

        TextView mealTitle = findViewById(R.id.mealTitle);
        mealTitle.setText(mealDetailsList.get(0).getStrMeal());

        TextView categoryView = findViewById(R.id.category);
        categoryView.setText(mealDetailsList.get(0).getStrCategory());

        TextView areaView = findViewById(R.id.area);
        areaView.setText(mealDetailsList.get(0).getStrArea());

        ImageView mealImage = findViewById(R.id.imageView);
        mealImage.setImageBitmap(getBitmapFromURL(mealDetailsList.get(0).getStrMealThumb()));

    }

    public void getMealById(String id){
        Call<MealDetailsList> mealDetailsCall = foodApi.getMealById(id);

        mealDetailsCall.enqueue(new Callback<MealDetailsList>() {
            @Override
            public void onResponse(Call<MealDetailsList> call, Response<MealDetailsList> response) {
                if (!response.isSuccessful()){
                    Log.i("API", "unSuccessful: "+ response.code());
                    return;
                }

                MealDetailsList mealsList = response.body();
                Log.i("API", "onSuccessful: "+ mealsList.getMeals().get(0).getStrMeal());
                mealDetailsList = mealsList.getMeals();

            }

            @Override
            public void onFailure(Call<MealDetailsList> call, Throwable t) {

            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}