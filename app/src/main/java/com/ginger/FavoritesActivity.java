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
import android.view.WindowManager;

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
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_favorites);


        loadingDialog = new LoadingDialog(FavoritesActivity.this);

        loadingDialog.startLoadingDialog();

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
                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item4:
                        startActivity(new Intent(getApplicationContext(), BlogActivity.class));
                        overridePendingTransition(0,0);
                        return true;
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

