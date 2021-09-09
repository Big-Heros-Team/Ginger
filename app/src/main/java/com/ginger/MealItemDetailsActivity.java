package com.ginger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ginger.Retrofit.FoodApi;
import com.ginger.adapters.IngredientsAdapter;
import com.ginger.filter.MealFilterItemDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealItemDetailsActivity extends AppCompatActivity {
    Bitmap bitmap;
    List<String> list;
    IngredientsAdapter adapter;

    private final String apiKey = "fb01023c94494c8dae6a1c8cd02b9e1c";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_item_details);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        Log.i("mealID from intent", "id -> " + id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMealFromAPI();
    }


    public void getMealFromAPI() {
        // Get Meal
        //https://api.spoonacular.com/recipes/716429/information?apiKey=11e60bd17b7648619ed45bb0dbf4e838

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi foodApi = retrofit.create(FoodApi.class);

        Call<MealFilterItemDetails> mealItemDetailsCall = foodApi.getMealItemDetailsById(id, apiKey);

        mealItemDetailsCall.enqueue(new Callback<MealFilterItemDetails>() {
            @Override
            public void onResponse(Call<MealFilterItemDetails> call, Response<MealFilterItemDetails> response) {
                if (!response.isSuccessful()) {
                    Log.i("Meal Details", "unSuccessful: " + response.code());
                    return;
                }
                MealFilterItemDetails mealDetails = response.body();
                Log.i("Meal Details", mealDetails.getTitle());

                //todo: render yousef details activity
                Log.i("test", "onResponse: " + mealDetails.getExtendedIngredients().get(0).getName());
                Log.i("test", "onResponse: " + mealDetails.getExtendedIngredients().get(0).getAmount());
                Log.i("test", "onResponse: " + mealDetails.getExtendedIngredients().get(0).getUnit());
                Log.i("test", "onResponse: " + mealDetails.getExtendedIngredients().size());

                // set name
                TextView mealTitle = findViewById(R.id.mealName);
                mealTitle.setText(mealDetails.getTitle());

                // set image
                ImageView mealImage = findViewById(R.id.imageView);
                if (mealDetails.getImage() != null && !mealDetails.getImage().equals("")) {
                    new GetImageFromUrl(mealImage).execute(mealDetails.getImage());
                }

                //todo: set area
                TextView areaView = findViewById(R.id.cuisine);
                areaView.setText("Cuisine NA" + "   |");

                // set category
                TextView categoryView = findViewById(R.id.category);
                categoryView.setText(mealDetails.getDishTypes().get(0));

                // set instructions
                Log.i("instructions", "onResponse: " + mealDetails.getInstructions());
                TextView instructions = findViewById(R.id.instructions);
                if (mealDetails.getInstructions() != null) {
                    instructions.setText(Html.fromHtml(mealDetails.getInstructions()));
                } else {
                    instructions.setText("No Instructions Available");
                }


                // set ingredients
                list = new ArrayList<>();
                if (mealDetails.getExtendedIngredients().size() != 0) {
                    for (int i = 0; i < mealDetails.getExtendedIngredients().size(); i++) {
                        list.add(mealDetails.getExtendedIngredients().get(i).getName() + ": " +
                                mealDetails.getExtendedIngredients().get(i).getAmount() + " " +
                                mealDetails.getExtendedIngredients().get(i).getUnit());
                    }
                }


                adapter = new IngredientsAdapter(list);
                RecyclerView recyclerView = findViewById(R.id.rv_ingredients);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        MealItemDetailsActivity.this,
                        RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<MealFilterItemDetails> call, Throwable t) {
                Log.i("FilterAPI", "onFailure: " + t.getMessage());

            }
        });
    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public GetImageFromUrl(ImageView img) {
            this.imageView = img;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}