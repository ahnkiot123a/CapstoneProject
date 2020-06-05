package com.koit.capstonproject_version_1.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.koit.capstonproject_version_1.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText;
    private String verificationId;
    //ma vung Viet Nam la 84
    private final String CODE = "84";
    private Button btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnContinue = findViewById(R.id.btnContinue);
        editText = findViewById(R.id.etPhoneNumber);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString().trim();
                if (!checkPhonenumber(number)) {
                    editText.setError("Số điện thoại không chính xác");
                    editText.requestFocus();
                    return;
                }
                String phoneNumber = "+" + CODE + number;
                Intent intent = new Intent(RegisterActivity.this, RegisterVerifyPhone.class);
                intent.putExtra("phonenumber", phoneNumber);
                sendVerificationCode(phoneNumber);
                startActivity(intent);
            }
        });
    }
    //check phone number is valid or not

    private boolean checkPhonenumber(String number) {
        //bat dau = 0 hoac NOT bat dau = 0
        String regexStr = "^[0]{0,1}+[0-9]{9}$";
        if (number.matches(regexStr)) {
            return true;
        } else return false;
    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required
//                            Intent intent = new Intent(RegisterActivity.this, RegisterVerifyPhone.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
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
            //  mResendToken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
           String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}