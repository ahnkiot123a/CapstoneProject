package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.ForgotPasswordController;
import com.koit.capstonproject_version_1.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etPhoneNumber;
    private LottieAnimationView lottieAnimationView;

    private ForgotPasswordController forgotPasswordController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initView();

        forgotPasswordController = new ForgotPasswordController(ForgotPasswordActivity.this);
    }

    private void initView() {
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        lottieAnimationView = findViewById(R.id.animationView);
    }

    public void sendPhoneNumberToResetPasswordActivity(android.view.View view){
        String number = etPhoneNumber.getText().toString().trim();
        forgotPasswordController.checkPhoneNumber(number);
    }

    public void back(android.view.View view) {
        onBackPressed();
    }


    public void showTextError(String error, TextInputEditText et) {
        et.requestFocus();
        et.setError(error);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lottieAnimationView.setVisibility(View.GONE);
    }

    public TextInputEditText getEtPhoneNumber() {
        return etPhoneNumber;
    }

    public LottieAnimationView getLottieAnimationView() {
        return lottieAnimationView;
    }
}