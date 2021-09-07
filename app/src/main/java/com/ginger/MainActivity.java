package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.ginger.Entities.CategoryList;
import com.ginger.Entities.MealDetails;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Entities.MealsList;
import com.ginger.Retrofit.FoodApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);

        // Get Categories list
        Call<CategoryList> resultCall = foodApi.getCategoryList();

        resultCall.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (!response.isSuccessful()){
                    Log.i("API", "unSuccessful: "+ response.code());
                    return;
                }

                CategoryList categoryList = response.body();
                Log.i("API", "onSuccessful: "+ categoryList.getCategoryList().get(0).getStrCategory());
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Log.i("API", "onFailure: "+ t.getMessage());
            }
        });


        // Get Meals list by Category
        Call<MealsList> mealsListCall = foodApi.getMealsList("Chicken");

        mealsListCall.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(Call<MealsList> call, Response<MealsList> response) {
                if (!response.isSuccessful()){
                    Log.i("API", "unSuccessful: "+ response.code());
                    return;
                }

                MealsList mealsList = response.body();
                Log.i("API", "onSuccessful: "+ mealsList.getMeals().get(0).getStrMeal());
            }

            @Override
            public void onFailure(Call<MealsList> call, Throwable t) {
                Log.i("API", "onFailure: "+ t.getMessage());
            }
        });

        // Get meal By ID
        Call<MealDetailsList> mealDetailsCall = foodApi.getMealById("53050");

        mealDetailsCall.enqueue(new Callback<MealDetailsList>() {
            @Override
            public void onResponse(Call<MealDetailsList> call, Response<MealDetailsList> response) {
                if (!response.isSuccessful()){
                    Log.i("API", "unSuccessful: "+ response.code());
                    return;
                }

                MealDetailsList mealsList = response.body();
                Log.i("API", "onSuccessful: "+ mealsList.getMeals().get(0).getStrMeal());
            }

            @Override
            public void onFailure(Call<MealDetailsList> call, Throwable t) {

            }
        });



        Button button = (Button) findViewById(R.id.buttonSignUp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }


}