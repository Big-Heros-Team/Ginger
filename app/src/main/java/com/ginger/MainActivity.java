package com.ginger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.ginger.Entities.CategoryList;
import com.ginger.Entities.Meal;
import com.ginger.Entities.MealDetails;
import com.ginger.Entities.MealDetailsList;
import com.ginger.Entities.MealsList;
import com.ginger.Retrofit.FoodApi;
import com.ginger.adapters.MainCategoryAdapter;
import com.ginger.adapters.MealAdapter;

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
    private Button blogEvent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amplifyConfig();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        foodApi = retrofit.create(FoodApi.class);


        // Get Categories list
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
                    getCategoryData();
                });
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Log.i("API", "onFailure: " + t.getMessage());
            }
        });

        // blog event listener
        blogEvent= findViewById(R.id.blogBut);
        blogEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, BlogActivity.class);
                startActivity(intent);

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
                Log.i("API", "onSuccessful: " + mealsList.getMeals().get(0).getStrMeal());
            }

            @Override
            public void onFailure(Call<MealDetailsList> call, Throwable t) {

            }
        });


    }

    public void amplifyConfig() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Tutorial", "Could not initialize Amplify", e);
        }
    }

    public void getCategoryData() {
        RecyclerView recyclerView = findViewById(R.id.rv_main_category);

        adapter = new MainCategoryAdapter(categoryList.getCategoryList(), new MainCategoryAdapter.OnTaskItemClickListener() {
            @Override
            public void onItemClicked(int position) {
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
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mealsRecyclerView.setLayoutManager(linearLayoutManager);
        mealsRecyclerView.setAdapter(mealAdapter);
    }

    // ADD MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fav:
                Toast.makeText(this, "fav selected", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.blog:
                Toast.makeText(this, "blog selected", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(MainActivity.this, BlogActivity.class);
                startActivity(intent);

            case R.id.profile:
                Toast.makeText(this, "profile selected", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}