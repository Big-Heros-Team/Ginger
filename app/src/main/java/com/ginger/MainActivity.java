package com.ginger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.ginger.Entities.CategoryList;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Entities.MealsList;
import com.ginger.Retrofit.FoodApi;
import com.ginger.adapters.MainCategoryAdapter;
import com.ginger.adapters.MealAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String MEAL_ID = "mealId";
    MainCategoryAdapter adapter;
    MealAdapter mealAdapter;
    CategoryList categoryList;
    Handler handler = new Handler();
    FoodApi foodApi;
    HashMap<String, String> map;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        loadingDialog = new LoadingDialog(MainActivity.this);

        loadingDialog.startLoadingDialog();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.item1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item1:
                        return true;
                    case R.id.item2:
                        if (Amplify.Auth.getCurrentUser()!=null) {
                            startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        else  {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                            break;
                        }
                    case R.id.item3:
                        if (Amplify.Auth.getCurrentUser()!=null) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        else  {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            break;
                        }
                    case R.id.item4:
                        if (Amplify.Auth.getCurrentUser()!=null) {
                            startActivity(new Intent(getApplicationContext(), BlogActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        else   {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            break;
                        }
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },2000);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        foodApi = retrofit.create(FoodApi.class);


        // Get Categories list



    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<CategoryList> resultCall = foodApi.getCategoryList();

        resultCall.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "unSuccessful: " + response.code());
                    return;
                }

                categoryList = response.body();
                Log.i("API", "onSuccessful: " + categoryList.getCategoryList().get(0).getStrCategory());

                handler.post(() -> {
                    setRecyclerView();
                });
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Log.i("API", "onFailure: " + t.getMessage());
            }
        });


        // Get meal By ID
        Call<MealDetailsList> mealDetailsCall = foodApi.getMealById("53050");

        mealDetailsCall.enqueue(new Callback<MealDetailsList>() {
            @Override
            public void onResponse(Call<MealDetailsList> call, Response<MealDetailsList> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "unSuccessful: " + response.code());
                    return;
                }

                MealDetailsList mealsList = response.body();
                Log.i("API", "onSuccessful: " + mealsList.getMeals().get(0));
            }

            @Override
            public void onFailure(Call<MealDetailsList> call, Throwable t) {

            }
        });

    }

    public void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_main_category);

        adapter = new MainCategoryAdapter(categoryList.getCategoryList(), new MainCategoryAdapter.OnTaskItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                findViewById(R.id.select_icon).setVisibility(View.GONE);
                findViewById(R.id.select_category).setVisibility(View.GONE);
                getMealsList(categoryList.getCategoryList().get(position).getStrCategory());
            }
        }, MainActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                MainActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    // Get Meals list by Category
    public void getMealsList(String category) {
        Call<MealsList> mealsListCall = foodApi.getMealsList(category);

        mealsListCall.enqueue(new Callback<MealsList>() {
            @Override
            public void onResponse(Call<MealsList> call, Response<MealsList> response) {
                if (!response.isSuccessful()) {
                    Log.i("API", "unSuccessful: " + response.code());
                    return;
                }

                MealsList mealsList = response.body();
                Log.i("API", "onSuccessful: " + mealsList.getMeals().get(0).getStrMeal());
                handler.post(() -> {
                    createMealsRecyclerView(mealsList);
                });
            }

            @Override
            public void onFailure(Call<MealsList> call, Throwable t) {
                Log.i("API", "onFailure: " + t.getMessage());
            }
        });
    }

    public void createMealsRecyclerView(MealsList mealsList) {
        RecyclerView mealsRecyclerView = findViewById(R.id.rv_sub_category);
        mealAdapter = new MealAdapter(mealsList.getMeals(), new MealAdapter.OnTaskItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                // todo: >>>>>>>>>>>>>>>>> change MainActivity to yousef activity
                Log.i("onItemClicked -> ", mealsList.getMeals().get(position).getStrMeal());
                Intent goToDetailsIntent = new Intent(getApplicationContext(), MealDetailsActivity.class);

                goToDetailsIntent.putExtra(MEAL_ID, mealsList.getMeals().get(position).getIdMeal());
                startActivity(goToDetailsIntent);
            }
        },MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mealsRecyclerView.setLayoutManager(linearLayoutManager);
        mealsRecyclerView.setAdapter(mealAdapter);
    }
}