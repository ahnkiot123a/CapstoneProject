package com.koit.capstonproject_version_1.Model.UIModel;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.koit.capstonproject_version_1.R;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private TextView textView;
    private LinearLayout linearLayoutAnimation;

    Animation fade_in;

    public ProgressButton(Context context, View view) {

        fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        cardView = view.findViewById(R.id.cardViewProgressBtn);
        constraintLayout = view.findViewById(R.id.constraintLayoutProgressBtn);
        progressBar = view.findViewById(R.id.progressBarBtn);
        textView = view.findViewById(R.id.tvProgressBtn);
        linearLayoutAnimation = view.findViewById(R.id.linearLayoutAnimationDone);
        linearLayoutAnimation.setVisibility(View.GONE);
    }

    public void buttonActivated(){
        progressBar.setAnimation(fade_in);
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutAnimation.setVisibility(View.GONE);
        textView.setAnimation(fade_in);
        textView.setText("Chờ chút...");

    }

    public void progressSuccess(){
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        linearLayoutAnimation.setVisibility(View.VISIBLE);

    }

    public void progressInitiation(){
        linearLayoutAnimation.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Đăng nhập");
    }

}
