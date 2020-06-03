package com.koit.capstonproject_version_1.View;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.koit.capstonproject_version_1.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private final int TIMEOUT_SPLASH = 1000;//1500 is timeout for the splash
    private static final String TAG_FB = "FacebookLogin";
    private static final String TAG_PHONE = "PhoneLogin";
    private static final String VN_COUNTRY_CODE = "+84";


    private RelativeLayout relativeLayout1, relativeLayoutRoot;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private Button btnFbLogin, btnLogin;
    private LoginButton loginButton;
    private EditText etPhoneNumber, etOTPorPassword;
    private String phoneNumber, verificationCodeBySystem, codeByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        animatedChangeSplash();
        // [START initialize_auth]
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeByUser = etOTPorPassword.getText().toString().trim();
                verifyCode(codeByUser);
            }
        });


    }

    public void sendVerificationOTPToUser(View view) {
        phoneNumber = etPhoneNumber.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                VN_COUNTRY_CODE + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if(code != null){
//                verifyCode(code);
//            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d(TAG_PHONE, e.getMessage());
            Toast.makeText(LoginActivity.this, "Số điện thoại sai!Mời nhập lại", Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredential(credential);
        ;
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.d(TAG_PHONE, task.getException().getMessage());
                    Toast.makeText(LoginActivity.this, "Mã OTP sai!Vui lòng nhập lại.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initView() {
        relativeLayout1 = findViewById(R.id.relativeLayout1);
        relativeLayoutRoot = findViewById(R.id.relativeLayoutRoot);
        loginButton = findViewById(R.id.login_button);
        btnFbLogin = findViewById(R.id.btnFbLogin);
        btnLogin = findViewById(R.id.btnLogin);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etOTPorPassword = findViewById(R.id.etOTPorPassword);
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
        handler.postDelayed(runnable, TIMEOUT_SPLASH);
    }

    public void callRegisterActivity(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginWithFacebook(View view) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG_FB, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken(), loginResult);
            }
            @Override
            public void onCancel() {
                Log.d(TAG_FB, "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "Đã hủy đăng nhập", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG_FB, "facebook:onError", error);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Không thể đăng nhập. Kiểm tra lại kết nối mạng");
                builder.setCancelable(false);
                builder.setPositiveButton("Đồng ý", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        if (view == btnFbLogin) {
            loginButton.performClick();
        }
    }

    private void handleFacebookAccessToken(AccessToken token, LoginResult loginResult) {
        Log.d(TAG_FB, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressBar();
        // [END_EXCLUDE]
        String tokenID = loginResult.getAccessToken().getToken();
        AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG_FB, "signInWithCredential:success");
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.d(TAG_FB, "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showProgressBar() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
