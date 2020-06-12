package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.ForgotPasswordController;
import com.koit.capstonproject_version_1.Controller.RegisterController;
import com.koit.capstonproject_version_1.R;

public class ResetPasswordActivity extends AppCompatActivity {


    private TextInputEditText etPhoneNumberRVP;
    private Button btnDone;
    private TextInputEditText etOTP;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private ForgotPasswordController forgotPasswordController;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

        initView();

        phoneNumber = getIntent().getStringExtra("phonenumber");
        Log.d("PhoneNumber",phoneNumber);
        etPhoneNumberRVP.setText(phoneNumber);

        forgotPasswordController = new ForgotPasswordController(ResetPasswordActivity.this, phoneNumber);
        //send OTP
        forgotPasswordController.sendVerificationCode(phoneNumber);
    }

    private void initView() {
        etPhoneNumberRVP = findViewById(R.id.etPhoneNumberRVP);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnDone = findViewById(R.id.btnDone);
    }

    public void sendNewPassword(android.view.View view){
        //   String number = etPassword.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String otpCode = etOTP.getText().toString().trim();

        forgotPasswordController.checkInputFromResetPasswordActivity(password,confirmPassword,otpCode);
    }

    //resend OTP code
    public void resendOTPCode(android.view.View view) {
        forgotPasswordController.resendOTPCode();
    }

    public void back(android.view.View view) {
        onBackPressed();
    }

    public void showTextError(String error, TextInputEditText et) {
        et.requestFocus();
        et.setError(error);
    }



    public TextInputEditText getEtPhoneNumberRVP() {
        return etPhoneNumberRVP;
    }

    public TextInputEditText getEtOTP() {
        return etOTP;
    }

    public TextInputEditText getEtPassword() {
        return etPassword;
    }

    public TextInputEditText getEtConfirmPassword() {
        return etConfirmPassword;
    }

}