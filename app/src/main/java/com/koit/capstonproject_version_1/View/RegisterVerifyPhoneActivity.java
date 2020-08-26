package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.RegisterController;
import com.koit.capstonproject_version_1.R;

public class RegisterVerifyPhoneActivity extends AppCompatActivity {
    //It is the verification id that will be sent to the user

    //firebase auth object
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
        setContentView(R.layout.activity_register_verify_phone);
        innitView();
        phoneNumber = getIntent().getStringExtra("phonenumber");
        Log.d("PhoneNumber",phoneNumber);
        etPhoneNumberRVP.setText(phoneNumber);
        registerController = new RegisterController(RegisterVerifyPhoneActivity.this, phoneNumber);
        //send OTP
        registerController.sendVerificationCode(phoneNumber);
        // sendVerificationCode(phoneNumberRVP);
        //get OTP code in TextInputLayout
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String storeName = etStoreNameRVP.getText().toString().trim();
                //   String number = etPassword.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String confirmPass = etConfirmPassword.getText().toString().trim();
                String otpCode = etOTP.getText().toString().trim();

                registerController.checkRegister(storeName, pass, confirmPass, otpCode, phoneNumber);
                //  registerController.checkStoreName(storeName);
                // if (!validateStoreName(storeName)) return;
                //   if (!checkPassword(number)) return;
                //if (!checkConfirmPass(pass)) return;
                //OTP is not true format
                //  if (!checkOTP(otpCode)) return;
//                verifyCode(otpCode);
//                firebaseDatabase = FirebaseDatabase.getInstance();
//                databaseReference = firebaseDatabase.getReference().child("User");
//                User user = new User("", "", "", storeName, "", pass, "", false, false);
//                databaseReference.child(phoneNumber).setValue(user);
            }
        });
    }

    private void innitView() {
        etPhoneNumberRVP = findViewById(R.id.etPhoneNumberRVP);
        etStoreNameRVP = findViewById(R.id.etStoreNameRVP);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnDone = findViewById(R.id.btnDone);
    }


    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //resend OTP code
        public void resendOTPCode(android.view.View view) {
        registerController.resendOTPCode();
    }

    public void showTextError(String error, TextInputEditText et) {
        et.requestFocus();
        et.setError(error);
    }

    public TextInputEditText getEtStoreNameRVP() {
        return etStoreNameRVP;
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