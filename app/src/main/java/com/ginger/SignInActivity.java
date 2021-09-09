package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

public class SignInActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);

        loadingDialog = new LoadingDialog(SignInActivity.this);



        TextView textView = findViewById(R.id.textfromSignInToReg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent= new Intent(SignInActivity.this,SignupActivity.class);
              startActivity(intent);

            }
        });


                EditText username = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        username.setHint("Username");
        password.setHint("Password");
        findViewById(R.id.signinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  userString = username.getText().toString();
                String passwordString = password.getText().toString();

                if( userString.trim().equals(""))
                {
                    username.setError( "Username is required!" );


                }else if(passwordString.trim().equals("")){

                    password.setError( "Password is required!" );


                }else{
                    signing(userString,passwordString);

                    username.getText().clear();
                    password.getText().clear();
                }

                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },5000);


            }

        });

    }

    public void signing(String username, String password){
        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    if (result.isSignInComplete()){
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}