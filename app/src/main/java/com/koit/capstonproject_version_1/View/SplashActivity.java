package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koit.capstonproject_version_1.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgView_logo;
    private TextInputEditText etPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgView_logo = findViewById(R.id.imgView_logo);
       handlerChangeActivity();


    }

    private void handlerChangeActivity(){
       new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                          Pair[] pairs = new Pair[1];
                                          pairs[0] = new Pair<View, String>(imgView_logo, "logo_image");
                                          ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                                          startActivity(intent, options.toBundle());
                                      }
                                  }
                , 800);
    }
}