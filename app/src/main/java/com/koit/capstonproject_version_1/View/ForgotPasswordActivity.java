package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.core.view.View;
import com.koit.capstonproject_version_1.Controller.ForgotPasswordController;
import com.koit.capstonproject_version_1.Controller.RegisterController;
import com.koit.capstonproject_version_1.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etPhoneNumber;

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



    public TextInputEditText getEtPhoneNumber() {
        return etPhoneNumber;
    }
}