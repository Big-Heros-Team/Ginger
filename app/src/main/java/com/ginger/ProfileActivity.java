package com.ginger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
//import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
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

    // location
//    private static final String TAG2 = "location" ;
//    FusedLocationProviderClient locationProviderClient;
//    Location currentLocation;
//    String addressString;
    // pop out menue

    private AlertDialog.Builder dialogeBuilder;
    private AlertDialog dialog;
    private EditText newContent_name;
    private EditText newContent_email;
    private EditText newContent_address;

    private Button save;
    private Button cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

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
                        startActivity(new Intent(getApplicationContext(), FavoritesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.item3:
                        return true;
//                    case R.id.item4:
//                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        // location

//        askForPermissionToUseLocation();
//        configureLocationServices();
//        askForLocation();


        // upload profile photo
//        Button uploadButt = findViewById(R.id.addPicture);

        // add listener for the upload file button

//        uploadButt.setOnClickListener(v -> getFileFromDevice());
//        download image and render it
        ImageView image= findViewById(R.id.image);
        downloadFile(Amplify.Auth.getCurrentUser().getUsername(),image);
        if (getIntent().getStringExtra("linkFile") != null){

            downloadFile(getIntent().getStringExtra("linkFile"),image);
        }

    }

    // location
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void askForPermissionToUseLocation() {
//        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
//    }

//
//    public void askForLocation() {
//        // TODO: geocoder
//        LocationRequest locationRequest;
//        LocationCallback locationCallback;
//
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    Log.i(TAG2, "result is null");
//
//                    return;
//                }
//                currentLocation = locationResult.getLastLocation();
//                Log.i(TAG2, currentLocation.toString());
//
//                // TODO: GeoCoding the coordinates
//                Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 10);
//                    Log.i(TAG2, addresses.get(0).toString());
//                    addressString = addresses.get(0).getAddressLine(0);
//                    Log.i(TAG2, addressString);
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        };
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            Toast t = new Toast(this);
//            t.setText("You need to accept the permissions");
//            t.setDuration(Toast.LENGTH_LONG);
//            t.show();
//            return;
//        }
//        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
//    }

//    public void configureLocationServices(){
//        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        // fuses the multiple location requests into one big one, gives you the most accurate that comes back
//    }

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
                    sucess -> Log.i(TAG,"the file saved to s3 successfully"),
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
//            Toast.makeText(this, "Picture Uploaded", Toast.LENGTH_SHORT).show();
        }
        if (id== R.id.item2){

            createNewContantDialoge();
        }
//        switch (item.getItemId()) {
//            case R.id.item1:
//                return true;
//            case R.id.item2:
////                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
//                createNewContantDialoge();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
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
                TextView namePlacHolder= findViewById(R.id.name);
                TextView emailPlacHolder= findViewById(R.id.email);
                TextView addressPlacHolder= findViewById(R.id.address);

                namePlacHolder.setText(newContent_name.getText());
                emailPlacHolder.setText(newContent_email.getText());
                addressPlacHolder.setText(newContent_address.getText());
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

