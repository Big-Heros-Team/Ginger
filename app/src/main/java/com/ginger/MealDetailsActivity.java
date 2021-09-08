package com.ginger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amazonaws.mobileconnectors.cognitoauth.handlers.AuthHandler;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Recipe;
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
    List<Recipe> result;
    ImageButton imageButton;


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

        handler = new Handler(Looper.getMainLooper());
        // Get meal By ID
        handler.post(() -> getMealById(id));
imageButton= findViewById(R.id.addToFavorite);

imageButton.setVisibility(View.GONE);

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
                result=getRecipe(mealsList.getMeals().get(0).getStrMeal(),Amplify.Auth.getCurrentUser().getUsername());



                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (result.isEmpty()) {
                            imageButton.setVisibility(View.VISIBLE);
                        }                    }
                }, 1000);

                Log.i("API", "onSuccessful: " + mealsList.getMeals().get(0).getStrMeal());
                mealDetailsList = mealsList.getMeals();
                handler.post(() -> {




                    ImageView mealImage = findViewById(R.id.imageView);
                    Uri uri = Uri.parse(mealDetailsList.get(0).getStrMealThumb());
                    Picasso.with(MealDetailsActivity.this).load(uri).into(mealImage);

                    list = new ArrayList<>();

                    for (int i = 0; i <= 9; i++) {
                        if (mealDetailsList.get(0).getStrIngredient(i + 1) != null && !mealDetailsList.get(0).getStrIngredient(i + 1).equals("")) {
                            if (mealDetailsList.get(0).getStrMeasure(i+1)==null || mealDetailsList.get(0).getStrMeasure(i + 1).equals("")) {
                                list.add(mealDetailsList.get(0).getStrIngredient(i + 1));
                            }
                            else {
                                list.add(mealDetailsList.get(0).getStrIngredient(i + 1) + " : " + mealDetailsList.get(0).getStrMeasure(i + 1));
                            }
                        }
                    }

                    String ingredientsNumbers="<b>" + list.size() + "</b> " + "ingredients    |";;
                    TextView ingredientsNum = findViewById(R.id.ingredients);
                    TextView cuisine = findViewById(R.id.cuisine);
                    TextView category = findViewById(R.id.category);
                    TextView mealName = findViewById(R.id.mealName);
                    TextView instructions = findViewById(R.id.instructions);

                    ingredientsNum.setText(Html.fromHtml(ingredientsNumbers));
                    cuisine.setText(mealDetailsList.get(0).getStrArea()+ "   |");
                    category.setText(mealDetailsList.get(0).getStrCategory());
                    mealName.setText(mealDetailsList.get(0).getStrMeal());
                    instructions.setText(mealDetailsList.get(0).getStrInstructions().replaceAll("\\.\\s?", "\\.\n"));

                    instructions.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

                    imageButton.setOnClickListener(view -> addToFavorite(mealDetailsList.get(0)));


                    adapter = new IngredientsAdapter(list);
                    RecyclerView recyclerView = findViewById(R.id.rv_ingredients);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.HORIZONTAL));
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

    public void addToFavorite(MealDetails meal){
        if (Amplify.Auth.getCurrentUser()==null){
            Log.i("addToFavorite", "addToFavorite: "+Amplify.Auth.getCurrentUser());
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
        }
        else {



            handler.postDelayed(() -> {



                Recipe recipe = Recipe.builder()
                        .recipeId(meal.getIdMeal())
                        .name(meal.getStrMeal())
                        .photo(meal.getStrMealThumb())
                        .owner(Amplify.Auth.getCurrentUser().getUsername())
                        .build();

                Amplify.API.mutate(ModelMutation.create(recipe),
                        response -> {
                    Log.i("MyAmplifyApp", "Todo with id: " + response.getData().getId());
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 Toast.makeText(getApplicationContext(), "Recipe is added to your favorites", Toast.LENGTH_SHORT).show();
                             }
                         });
                        },
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

            },3000);
        }
}

public List<Recipe> getRecipe(String name,String owner){

        List<Recipe> result=new ArrayList<>();
    Amplify.API.query(
            ModelQuery.list(Recipe.class, Recipe.NAME.contains(name).and(Recipe.OWNER.contains(owner))),
            response -> {
                for (Recipe recipe : response.getData()) {
                    Log.i("MyAmplifyApp", recipe.getName());
                    result.add(recipe);
                }
            },
            error -> Log.e("MyAmplifyApp", "Query failure", error)
    );
    return result;
}
}