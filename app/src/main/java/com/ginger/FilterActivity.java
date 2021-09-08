package com.ginger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FilterActivity extends AppCompatActivity {
    private String ingredients;
    private String SelectedMealType;
    private String maxReadyTime;
    private String cuisine;
    private String diet;
    private String minCalories;
    private String maxCalories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter);

        // Meal Types spinner
        Spinner mealTypes = findViewById(R.id.mealTypesSpinner);
        String[] mealType = new String[]{"",
                "main course", "side dish", "dessert",
                "appetizer", "salad", "bread",
                "breakfast", "soup", "beverage", "sauce",
                "marinade", "fingerfood", "snack", "drink"
        };

        ArrayAdapter<String> mealTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mealType);
        mealTypes.setAdapter(mealTypesAdapter);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.item5);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item2:
                        startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item4:
                        startActivity(new Intent(getApplicationContext(), BlogActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item5:
                        return true;
                }
                return false;
            }
        });

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
                // Meal Types spinner
                Spinner cuisinesSpinner = (Spinner) findViewById(R.id.CuisinesSpinner);
                cuisine = cuisinesSpinner.getSelectedItem().toString();

                // get cuisines
                Spinner mealTypesSpinner = (Spinner) findViewById(R.id.mealTypesSpinner);
                SelectedMealType = mealTypesSpinner.getSelectedItem().toString();

                // get diet
                Spinner dietSpinner = (Spinner) findViewById(R.id.dietSpinner);
                diet = dietSpinner.getSelectedItem().toString();

                Log.i("spinner", "cuisine: " + cuisine + "  -  diet: " + diet);

                // get ingredients
                ingredients = ((EditText) findViewById(R.id.ingredients)).getText().toString();
                Log.i("TAG", "query: " + ingredients);


                // get cooking time
                maxReadyTime = ((EditText) findViewById(R.id.maxReadyTime)).getText().toString();
                if (maxReadyTime.equals("")) {
                    maxReadyTime = "9999";
                }
                Log.i("TAG", "maxReadyTime: " + maxReadyTime);


                // get Calories Range
                minCalories = ((EditText) findViewById(R.id.minCalories)).getText().toString();
                maxCalories = ((EditText) findViewById(R.id.maxCalories)).getText().toString();
                Log.i("TAG", minCalories + "--" + maxCalories);

                if (minCalories.equals("")) {
                    minCalories = "0";
                }
                if (maxCalories.equals("")) {
                    maxCalories = "9999999999999";
                }

                //go to show meal details
                Intent goToShowFilteredMeals = new Intent(FilterActivity.this, ShowFilteredMealsActivity.class);
                goToShowFilteredMeals.putExtra("ingredients", ingredients);
                goToShowFilteredMeals.putExtra("mealType", SelectedMealType);
                goToShowFilteredMeals.putExtra("maxReadyTime", maxReadyTime);
                goToShowFilteredMeals.putExtra("cuisine", cuisine);
                goToShowFilteredMeals.putExtra("diet", diet);
                goToShowFilteredMeals.putExtra("minCalories", minCalories);
                goToShowFilteredMeals.putExtra("maxCalories", maxCalories);
                startActivity(goToShowFilteredMeals);

            }
        });

    }


}