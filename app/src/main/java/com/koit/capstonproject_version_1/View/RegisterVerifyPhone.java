package com.koit.capstonproject_version_1.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.koit.capstonproject_version_1.R;

import java.util.concurrent.TimeUnit;

public class RegisterVerifyPhone extends AppCompatActivity {
    //It is the verification id that will be sent to the user

    //firebase auth object
    private FirebaseAuth mAuth;
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify_phone);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        mAuth = FirebaseAuth.getInstance();
        String phoneNumber = getIntent().getStringExtra("phonenumber");
        etPhoneNumber.setText(phoneNumber);
    }


}