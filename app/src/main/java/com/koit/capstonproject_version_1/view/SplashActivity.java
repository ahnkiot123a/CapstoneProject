package com.koit.capstonproject_version_1.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView imgView_logo;
    private TextInputEditText etPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imgView_logo = findViewById(R.id.imgView_logo);

        restoreData();
    }

    private void restoreData() {
        if (restorePrefData()) {
            handlerChangeLoginActivity();
        } else {
            handlerChangeIntroActivity();
        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    private void handlerChangeLoginActivity() {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                          Pair[] pairs = new Pair[1];
                                          pairs[0] = new Pair<View, String>(imgView_logo, "logo_image");
                                          ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                                          startActivity(intent, options.toBundle());
                                          finish();
                                      }
                                  }
                , 200);
    }

    private void handlerChangeIntroActivity() {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent mainActivity = new Intent(getApplicationContext(), IntroActivity.class);
                                          startActivity(mainActivity);
                                          finish();
                                      }
                                  }
                , 500);
    }
}