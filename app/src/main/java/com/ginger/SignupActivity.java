package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignupActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loadingDialog = new LoadingDialog(SignupActivity.this);


        TextView textView = findViewById(R.id.textView10);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);

            }
        });


        EditText email = findViewById(R.id.signUpEmail);
        EditText username = findViewById(R.id.signUpUser);
        EditText password = findViewById(R.id.signUpPass);


        findViewById(R.id.signUpBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if( email.getText().toString().trim().equals(""))
                {
                    email.setError( "Email is required!" );

                    email.setHint("please enter email");
                }else if(username.getText().toString().trim().equals("")){
                    username.setError( "Username is required!" );

                    username.setHint("please enter username");
                }else if(password.getText().toString().trim().equals("")){
                    password.setError( "Password is required!" );

                    password.setHint("please enter password");
                }else{
                    signingUp(email.getText().toString(),
                            username.getText().toString(),
                            password.getText().toString());


                    Intent intent = new Intent(SignupActivity.this, ConfirmActivity.class);
                    intent.putExtra("username",username.getText().toString());
                    startActivity(intent);


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

        findViewById(R.id.textView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signingUp(String email, String username, String password){
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();


        Amplify.Auth.signUp(username,
                password,
                options,
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }
}