package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.RegisterController;
import com.koit.capstonproject_version_1.R;

public class ResetPasswordActivity extends AppCompatActivity {


    private TextInputEditText etPhoneNumberRVP;
    private Button btnDone;
    private TextInputEditText etStoreNameRVP;
    private TextInputEditText etOTP;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private RegisterController registerController;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }


    //resend OTP code
    public void resendOTPCode(android.view.View view) {
        registerController.resendOTPCode();
    }

    public void back(View view) {
        onBackPressed();
    }

}