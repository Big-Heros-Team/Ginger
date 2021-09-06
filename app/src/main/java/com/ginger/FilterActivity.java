package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ginger.Retrofit.FoodApi;
import com.ginger.filter.Results;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilterActivity extends AppCompatActivity {
    private final String apiKey = "11e60bd17b7648619ed45bb0dbf4e838";
    private String query = "pasta";
    private String number = "5";
    private String cuisine;
    private String diet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Cuisines spinner
        Spinner cuisinesList = findViewById(R.id.CuisinesSpinner);
        String[] cuisines = new String[]{"",
                "African", "American", "British", "Cajun", "Caribbean",
                "Chinese", "Eastern European", "European", "French", "German",
                "Greek", "Indian", "Irish", "Italian", "Japanese", "Korean",
                "Latin American", "Mediterranean", "Mexican", "Middle Eastern",
                "Nordic", "Southern", "Spanish", "Thai", "Vietnamese"
        };
        ArrayAdapter<String> cuisinesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cuisines);
        cuisinesList.setAdapter(cuisinesAdapter);


        // Diet spinner
        Spinner dietList = findViewById(R.id.dietSpinner);
        String[] diets = new String[]{"",
                "Gluten Free", "Ketogenic", "Vegetarian",
                "Lacto-Vegetarian", "Ovo-Vegetarian", "Vegan",
                "Pescetarian", "Paleo", "Primal", "Whole30"
        };

        ArrayAdapter<String> dietAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, diets);
        dietList.setAdapter(dietAdapter);

        // start filtering
        findViewById(R.id.filtering).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get cuisines
                Spinner cuisinesSpinner = (Spinner) findViewById(R.id.CuisinesSpinner);
                cuisine = cuisinesSpinner.getSelectedItem().toString();

                // get diet
                Spinner dietSpinner = (Spinner) findViewById(R.id.dietSpinner);
                diet = dietSpinner.getSelectedItem().toString();

                Log.i("spinner", "onClick: " + cuisine + "  -  " + diet);


                // get query
                query = ((EditText) findViewById(R.id.queryName)).getText().toString();
                Log.i("TAG", "onClick: " + query);

                getFilteredMealsFromApi();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

//        getFilteredMealsFromApi();

    }

    public void getFilteredMealsFromApi() {
        // Get Meals
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);

        Call<Results> mealsListCall = foodApi.getResults(apiKey, query, diet, cuisine);

        mealsListCall.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (!response.isSuccessful()) {
                    Log.i("FilterAPI", "unSuccessful: " + response.code());
                    return;
                }
                Results mealsList = response.body();
                if (mealsList.getResults().size() == 0) {
                    Log.i("FilterAPI", "nothing found: ");

                } else {
                    Log.i("FilterAPI", "onSuccessful: " + mealsList.getResults().get(0).getTitle());
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.i("FilterAPI", "onFailure: " + t.getMessage());
            }
        });
    }


}