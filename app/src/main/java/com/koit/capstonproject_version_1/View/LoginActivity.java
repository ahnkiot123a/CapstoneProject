package com.koit.capstonproject_version_1.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.UIModel.ProgressButton;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class LoginActivity extends AppCompatActivity {

    public static final String CURRENT_USER = "CURRENT_USER";

    private UserController userController;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private Button btnFbLogin, btnLogin;
    private LoginButton loginFbButton;
    private TextInputEditText etPhoneNumber, etPassword;
    private TextView tvForgotPassword, tvAccount, tvRegister;

    private View view;


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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressButton progressButton = new ProgressButton(LoginActivity.this, view);
                progressButton.buttonActivated();
                loginWithPhone();
            }
        });



    }

    private void initView() {
        loginFbButton = findViewById(R.id.login_fb_button);
        btnFbLogin = findViewById(R.id.btnFbLogin);
        view = findViewById(R.id.btnLogin);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvAccount = findViewById(R.id.tvAccount);
        tvRegister = findViewById(R.id.tvRegister);
    }


    public void callRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginWithFacebook(View view) {
        userController.loginWithFacebook(view, loginFbButton, callbackManager, firebaseAuth, LoginActivity.this, btnFbLogin);
    }

    public void loginWithPhone() {

        userController.loginWithPhoneAndPassword(etPhoneNumber, etPassword, LoginActivity.this, view, btnFbLogin, tvForgotPassword, tvAccount, tvRegister);
    }

    public void callForgotPasswordActivity(View view){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //init where start login activity
        ProgressButton progressButton = new ProgressButton(this, view);
        progressButton.progressInitiation();
        btnFbLogin.setClickable(true);
        view.setClickable(true);
        etPhoneNumber.setClickable(true);
        etPassword.setClickable(true);
        tvForgotPassword.setClickable(true);
        tvAccount.setClickable(true);
        tvRegister.setClickable(true);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        /*if(user != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }*/

//        if(PreferenceManager.getInstance(this).fetchObject("currentUser", User.class) != null){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

        if (SharedPrefs.getInstance().getCurrentUser(CURRENT_USER) != null) {
            Log.i("currentUser", SharedPrefs.getInstance().getCurrentUser(CURRENT_USER).toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Bạn muốn tắt ứng dụng?").setPositiveButton("Có", dialogClickListener)
                .setNegativeButton("Không", dialogClickListener).show();

    }
}
