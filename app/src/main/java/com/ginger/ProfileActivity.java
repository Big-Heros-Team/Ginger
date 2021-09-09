package com.ginger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
//import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Blog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    // storage
    private static final int CODE_REQUEST =55 ;
    private static final String TAG = "upload";
    private String key;

    private AlertDialog.Builder dialogeBuilder;
    private AlertDialog dialog;
    private EditText newContent_name;
    private EditText newContent_email;
    private EditText newContent_address;

    private Button save;
    private Button cancle;
    ImageView image;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
    TextView namePlaceHolder;
    TextView emailPlaceHolder;
    TextView addressPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        preferenceEditor= sharedPreferences.edit();

         namePlaceHolder= findViewById(R.id.name);
         emailPlaceHolder= findViewById(R.id.email);
         addressPlaceHolder= findViewById(R.id.address);

        if (sharedPreferences.contains("name")){
            namePlaceHolder.setText(sharedPreferences.getString("name", "username"));
        }
        if (sharedPreferences.contains("email")){
            emailPlaceHolder.setText(sharedPreferences.getString("email", "username"));
        }
        if (sharedPreferences.contains("address")){
            addressPlaceHolder.setText(sharedPreferences.getString("address", "username"));
        }

        // user Name:
        TextView namePlaceHolder= findViewById(R.id.tv_name);
        namePlaceHolder.setText(Amplify.Auth.getCurrentUser().getUsername());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.item3);
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

        findViewById(R.id.logout).setOnClickListener(view -> {
            Amplify.Auth.signOut(
                    () -> startActivity(new Intent(getApplicationContext(), SignInActivity.class)),
                    error -> Log.e("AuthQuickstart", error.toString())
            );

        });


         image= findViewById(R.id.image);
        downloadFile(Amplify.Auth.getCurrentUser().getUsername(),image);
        if (getIntent().getStringExtra("linkFile") != null){

            downloadFile(getIntent().getStringExtra("linkFile"),image);
        }


        findViewById(R.id.uploadPhotoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFileFromDevice();
            }
        });

        findViewById(R.id.updateDetailsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewContantDialoge();
            }
        });
    }


    // storage
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CODE_REQUEST && resultCode== RESULT_OK ){
            File uploadedFile = new File(getApplicationContext().getFilesDir(),"file");
            Log.i(TAG,"create stream");
//            key = Amplify.Auth.getCurrentUser().getUsername();
            try {
                assert data != null;
                InputStream stream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(stream, new FileOutputStream(uploadedFile));


            }catch (Exception e){
                Log.e(TAG,"error in upolad the File "+ e.toString());

            }
            Amplify.Storage.uploadFile(
                    key = Amplify.Auth.getCurrentUser().getUsername(),
//                    key= new Date().toString()+".jpg",
                    uploadedFile,
                    success -> {
                        Log.i(TAG,"the file saved to s3 successfully");
                        downloadFile(key, image);
                    },
                    error -> Log.e(TAG," error in store data at S3 "+ error)
            );

        }

    }

    private void getFileFromDevice(){

               // Intent
        Intent selectFile= new Intent(Intent.ACTION_GET_CONTENT);
        selectFile.setType("*/*");
        selectFile= Intent.createChooser(selectFile,"select File");

        startActivityForResult(selectFile,CODE_REQUEST);


    }

    private void downloadFile(String Key, ImageView image){
        File imageFile;
        Amplify.Storage.downloadFile(
                Key,
                imageFile= new File(getApplicationContext().getFilesDir() + "/download.png"),
                result ->{
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                    image.setImageBitmap(myBitmap);

                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                } ,
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id== R.id.item1){
            getFileFromDevice();
        }
        if (id== R.id.item2){

            createNewContantDialoge();
        }
        return super.onOptionsItemSelected(item);

    }
    public void createNewContantDialoge(){
        dialogeBuilder= new AlertDialog.Builder(this);
        final View contantpopupView = getLayoutInflater().inflate(R.layout.popup,null);

        newContent_name =  contantpopupView.findViewById(R.id.tv_name);
        newContent_email = contantpopupView.findViewById(R.id.tv_email);
        newContent_address = contantpopupView.findViewById(R.id.tv_address);
        dialogeBuilder.setView(contantpopupView);
        dialog= dialogeBuilder.create();
        dialog.show();
        save= contantpopupView.findViewById(R.id.save_button);
        cancle= (Button) contantpopupView.findViewById(R.id.cancel_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                namePlaceHolder.setText(newContent_name.getText());
                emailPlaceHolder.setText(newContent_email.getText());
                addressPlaceHolder.setText(newContent_address.getText());

                preferenceEditor.putString("name", newContent_name.getText().toString());
                preferenceEditor.putString("email", newContent_email.getText().toString());
                preferenceEditor.putString("address", newContent_address.getText().toString());

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
}

