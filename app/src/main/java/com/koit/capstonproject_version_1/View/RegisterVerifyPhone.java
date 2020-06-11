package com.koit.capstonproject_version_1.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.koit.capstonproject_version_1.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterVerifyPhone extends AppCompatActivity {
    //It is the verification id that will be sent to the user

    //firebase auth object
    private FirebaseAuth mAuth;
    private TextInputLayout etPhoneNumberRVP;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;

    private String phoneNumber;
    private Button btnDone;
    private TextInputLayout etStoreNameRVP;
    private TextInputLayout etOTP;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;
    private String otpCode;
    private int otpCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify_phone);
        etPhoneNumberRVP = findViewById(R.id.etPhoneNumberRVP);
        phoneNumber = getIntent().getStringExtra("phonenumber");
        etPhoneNumberRVP.getEditText().setText(phoneNumber);
        etStoreNameRVP = findViewById(R.id.etStoreNameRVP);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnDone = findViewById(R.id.btnDone);
        //send OTP
        // sendVerificationCode(phoneNumberRVP);
        sendVerificationCode(phoneNumber);
        //get OTP code in TextInputLayout
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                otpCode = etOTP.getEditText().getText().toString().trim();
                if (!validateStoreName()) return;
                if (!checkPassword()) return;
                if (!checkConfirmPass()) return;
                //OTP is not true format
                if (!checkOTP()) return;
                verifyCode(otpCode);
            }
        });
    }


    //check OTP code is valid format or not
    private boolean checkOTP() {
        String optCode = etOTP.getEditText().getText().toString().trim();
        if (optCode.isEmpty()) {
            etOTP.getEditText().requestFocus();
            etOTP.getEditText().setError("Vui lòng nhập OTP");
            return false;
        } else if (optCode.length() != 6) {
            etOTP.getEditText().requestFocus();
            etOTP.getEditText().setError("Mã OTP phải bao gồm 6 kí tự");
            return false;
        }
        return true;
    }

    //validate User Store Name
    private boolean validateStoreName() {
        String storeName = etStoreNameRVP.getEditText().getText().toString().trim();
        if (storeName.isEmpty()) {
            etStoreNameRVP.getEditText().requestFocus();
            etStoreNameRVP.getEditText().setError("Vui lòng nhập tên cửa hàng.");
            return false;
        } else {
            return true;
        }
    }

    //resend OTP code
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phoneNumber,        // Phone number to verify
                90,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + number,
                90,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            token = forceResendingToken;
            Toast.makeText(RegisterVerifyPhone.this, "Mã xác nhận OTP đã được gửi tới máy bạn!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                verifyCode(code);
//            }
        }

        @Override
        //This method is called in response to an invalid verification request,
        // such as a request that specifies an invalid phone number or verification code.
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(RegisterVerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            //  Toast.makeText(RegisterVerifyPhone.this, "Mã OTP của bạn đã hết hạn. Vui lòng nhấn vào 'Gửi lại mã'", Toast.LENGTH_LONG).show();
        }
    };


    //check OTP code is valid or not
    private void verifyCode(String code) {
        //OTP code must be check <=3 times
        otpCounter++;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(RegisterVerifyPhone.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            etOTP.getEditText().requestFocus();
                            etOTP.getEditText().setError("Mã OTP không chính xác");
                            //OPT expired
                            if (otpCounter >= 3) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                resendVerificationCode(phoneNumber, token);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterVerifyPhone.this);
                                builder.setMessage("Mã OTP của bạn đã hết hạn, vui lòng gửi lại mã.").setPositiveButton("Gửi lại mã", dialogClickListener)
                                        .setNegativeButton("Thoát", dialogClickListener).show();
                            }
                            // Toast.makeText(RegisterVerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //check password is valid or not
    private boolean checkPassword() {
        String number = etPassword.getEditText().getText().toString().trim();
        if (number.isEmpty()) {
            etPassword.getEditText().requestFocus();
            etPassword.getEditText().setError("Vui lòng nhập mật khẩu");
        } else {
            //gom it nhat 6 ki tu so
            String regexStr = "^[0-9]{6,}$";
            if (number.matches(regexStr)) {
                return true;
            } else {
                etPassword.getEditText().requestFocus();
                etPassword.getEditText().setError("Vui lòng nhập 6 kí tự số trở lên.");
            }
        }
        return false;
    }

    //resend OTP code
    public void resendOTPCode(View view) {
        otpCounter = 0;
        resendVerificationCode(phoneNumber, token);
    }

    //check confirm password
    private boolean checkConfirmPass() {
        String pass = etPassword.getEditText().getText().toString().trim();
        String confirmPass = etConfirmPassword.getEditText().getText().toString().trim();
        if (pass.equals(confirmPass))
            return true;
        else {
            etConfirmPassword.getEditText().requestFocus();
            etConfirmPassword.getEditText().setError("Mật khẩu không khớp.");
        }
        return false;
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}