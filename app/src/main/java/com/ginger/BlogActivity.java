package com.ginger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Blog;
import com.amplifyframework.datastore.generated.model.Todo;
import com.ginger.adapters.BlogsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class BlogActivity extends AppCompatActivity {
    private Button addBlogButton;
    private Handler handler;
    private BlogsAdapter adapter;


    // pop out menue

    private AlertDialog.Builder dialogeBuilder;
    private AlertDialog dialog;
    private EditText body_content;
    private EditText title_content;
    private Button save;
    private Button cancle;

    // recycle view
    private ArrayList<Blog> blogs= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_blog);

        // fetch all blogs datat from dynamoDB
        getDataFromApi();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.item4);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item2:
                        if (Amplify.Auth.getCurrentUser()!=null) {
                            startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    case R.id.item3:
                        if (Amplify.Auth.getCurrentUser()!=null) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    case R.id.item4:
                        if (Amplify.Auth.getCurrentUser()!=null) {
                            startActivity(new Intent(getApplicationContext(), BlogActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        addBlogButton = findViewById(R.id.blogBtn);
        addBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewContantDialoge();
            }
        });
        // recycle view
        RecyclerView recyclerView = findViewById(R.id.blog_list);


        // handler
        handler= new Handler(Looper.getMainLooper(),

                msg -> {
                    taskListSetChanged();
                    return false;
                }
        );
        adapter= new BlogsAdapter(blogs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getApplicationContext()

        );

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    // create popout window

    public void createNewContantDialoge() {
        dialogeBuilder = new AlertDialog.Builder(this);
        final View contantpopupView = getLayoutInflater().inflate(R.layout.popout_blog, null);

        body_content = contantpopupView.findViewById(R.id.body_place);
        title_content = contantpopupView.findViewById(R.id.title_place);

        dialogeBuilder.setView(contantpopupView);
        dialog = dialogeBuilder.create();
        dialog.show();
        save = contantpopupView.findViewById(R.id.save_button);
        cancle = (Button) contantpopupView.findViewById(R.id.cancel_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // --- create blog object
                Blog newBlog = Blog.builder().body(body_content.getText().toString()).title(title_content.getText().toString()).userName(Amplify.Auth.getCurrentUser().getUsername()).build();

                // -- save in the dynamoDB
                Amplify.API.mutate(ModelMutation.create(newBlog)

                        ,success -> Log.i("submit", "saved item sucessfully")
                        , error -> Log.e("submit", "error in the saving to server",error)
                );
                dialog.dismiss();

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void getDataFromApi(){
        Amplify.API.query(ModelQuery.list(Blog.class),
                response -> {
                    int i=0;

                    for (Blog item: response.getData()){
                        blogs.add(item);
                        Log.i("bring","item:"+item.getBody());
                        i++;
                    }
                    handler.sendEmptyMessage(1);



//                    taskListSetChanged();
                },
                error -> Log.e("bring", "onCreate: Failed to get expenses => " + error.toString())


        );

    }
    private void taskListSetChanged(){
        adapter.notifyDataSetChanged();
    }


}