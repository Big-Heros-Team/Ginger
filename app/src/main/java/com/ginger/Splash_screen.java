package com.ginger;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class Splash_screen extends AppCompatActivity {

    ImageView logoImage;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        amplifyConfig();

        logoImage=findViewById(R.id.splashImage);
        textView=findViewById(R.id.textForSplash);


        //Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initialize object animator
        ObjectAnimator objectAnimator= ObjectAnimator.ofPropertyValuesHolder(
                logoImage,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );
        //Set Duration
        objectAnimator.setDuration(500);
        //Set repeat count
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //Set repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start Animator
        objectAnimator.start();

        //Set animate text
        animatText("Ginger");

        //initialize handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //redirect to main activity
                startActivity(new Intent(Splash_screen.this,WelcomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                //Finish activity
                finish();
            }
        },3000);
    }

    Runnable runnable= new Runnable() {
        @Override
        public void run() {
            //When runnable run
            //set text
            textView.setText(charSequence.subSequence(0,index++));
            // Check condition
            if(index <= charSequence.length()){
                //When index is equal to text length
                //run Handler
                handler.postDelayed(runnable,delay);
            }
        }
    };

    //Created animated Text method
    public void animatText(CharSequence cs){
        //Set Text
        charSequence = cs;
        //Clear index
        index = 0;
        //Clear Text
        textView.setText("");
        //Remove Call back
        handler.removeCallbacks(runnable);
        //Run handler
        handler.postDelayed(runnable,delay);

    }

    public void amplifyConfig(){
        try {
            Amplify.addPlugin(new AWSS3StoragePlugin());

            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e("Tutorial", "Could not initialize Amplify", e);
        }
    }
}