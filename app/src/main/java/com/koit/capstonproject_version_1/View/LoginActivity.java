package com.koit.capstonproject_version_1.View;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.R;

public class LoginActivity extends AppCompatActivity {

    private UserController userController;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private Button btnFbLogin, btnLogin;
    private LoginButton loginFbButton;
    private EditText etPhoneNumber, etOTPorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        // [START initialize_auth]
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        userController = new UserController();
    }

    private void initView() {
        loginFbButton = findViewById(R.id.login_fb_button);
        btnFbLogin = findViewById(R.id.btnFbLogin);
        btnLogin = findViewById(R.id.btnLogin);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etOTPorPassword = findViewById(R.id.etOTPorPassword);
    }


    public void callRegisterActivity(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginWithFacebook(View view) {
        userController.loginWithFacebook(view, loginFbButton, callbackManager, firebaseAuth, LoginActivity.this, btnFbLogin);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
