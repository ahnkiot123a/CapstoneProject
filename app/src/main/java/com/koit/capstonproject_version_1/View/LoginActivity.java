package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.koit.capstonproject_version_1.R;

public class LoginActivity extends AppCompatActivity {

    private final int timeoutSplash = 1500;//2000 is timeout for the splash

    private RelativeLayout relativeLayout1, relativeLayoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        animatedChangeSplash();
    }

    private void initView() {
        relativeLayout1 = findViewById(R.id.relativeLayout1);
        relativeLayoutRoot = findViewById(R.id.relativeLayoutRoot);
    }

    private void animatedChangeSplash() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                relativeLayout1.setVisibility(View.VISIBLE);
            }
        };
        relativeLayoutRoot.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        handler.postDelayed(runnable, timeoutSplash);
    }
}
