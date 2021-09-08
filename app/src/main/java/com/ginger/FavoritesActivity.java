package com.ginger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Recipe;
import com.ginger.adapters.FavoritesAdapter;
import com.ginger.adapters.MainCategoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private static final String MEAL_ID ="meal_id" ;
    Handler handler;
    FavoritesAdapter adapter;
    List<Recipe> favoritesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Tutorial", "Could not initialize Amplify", e);
        }
        setContentView(R.layout.activity_favorites);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.item2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item2:
                        return true;
                        // TODO insert third and fourth page for profile and blogs
                }
                return false;
            }
        });

        favoritesList=getAllRecipes();

        handler=new Handler(Looper.getMainLooper());

        handler.postDelayed(this::setRecyclerView,2000);
    }

    public void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_favorite_category);

        adapter = new FavoritesAdapter(favoritesList, new FavoritesAdapter.OnTaskItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent goToDetailsIntent = new Intent(getApplicationContext(), MealDetailsActivity.class);

                goToDetailsIntent.putExtra(MainActivity.MEAL_ID,favoritesList.get(position).getRecipeId());
                startActivity(goToDetailsIntent);
            }
        }, FavoritesActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                FavoritesActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }



    public List<Recipe> getAllRecipes(){

        List<Recipe> result=new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Recipe.class),
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

