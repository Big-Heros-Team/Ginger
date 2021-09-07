package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView textView = findViewById(R.id.textfromSignInToReg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent= new Intent(SigninActivity.this,SignupActivity.class);
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
                    try {
                        Thread.sleep(3000);
                        Log.d("signupActivity", "user string="+userString);
                        Log.d("signupActivity", "password string="+passwordString);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    username.getText().clear();
                    password.getText().clear();
                }

            }
        });
    }

    public void signing(String username, String password){
        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    if (result.isSignInComplete()){
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}