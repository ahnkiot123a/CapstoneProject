package com.koit.capstonproject_version_1.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.UIModel.MyDialog;
import com.koit.capstonproject_version_1.Model.UIModel.ProgressButton;
import com.koit.capstonproject_version_1.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    public static final String CURRENT_USER = "CURRENT_USER";

    private UserController userController;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private Button btnFbLogin;
    private LoginButton loginFbButton;
    private TextInputEditText etPhoneNumber, etPassword;
    private TextView tvForgotPassword, tvAccount, tvRegister;

    private View view;
    private Disposable internetDisposable;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        // [START initialize_auth]f
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
                etPhoneNumber.setClickable(false);
                etPassword.setClickable(false);
                btnFbLogin.setClickable(false);
                tvForgotPassword.setClickable(false);
                tvRegister.setClickable(false);
                tvAccount.setClickable(false);
                loginWithPhone();
            }
        });

        dialog = new MyDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                            if (isConnected) {
                                if (dialog != null)
                                    dialog.cancelConnectionDialog();
                            } else {
                                dialog.showInternetError();
                            }
                        }

                );
    }

    @Override
    protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
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
        Intent intent = new Intent(LoginActivity.this, RegisterInputPhoneActivity.class);
        startActivity(intent);
    }

    public void loginWithFacebook(View view) {
        userController.loginWithFacebook(view, loginFbButton, callbackManager, firebaseAuth, LoginActivity.this, btnFbLogin);
    }

    public void loginWithPhone() {
        userController.loginWithPhoneAndPassword(etPhoneNumber, etPassword, LoginActivity.this, view, btnFbLogin, tvForgotPassword, tvAccount, tvRegister);
    }

    public void callForgotPasswordActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //init where start login activity
        initStartLoginActivity();

        if (SharedPrefs.getInstance().getCurrentUser(CURRENT_USER) != null) {
            Log.i("currentUser", SharedPrefs.getInstance().getCurrentUser(CURRENT_USER).toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("currentUser", user.toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }

    private void initStartLoginActivity() {
        ProgressButton progressButton = new ProgressButton(this, view);
        progressButton.progressInitiation();
        btnFbLogin.setClickable(true);
        view.setClickable(true);
        etPhoneNumber.setClickable(true);
        etPassword.setClickable(true);
        tvForgotPassword.setClickable(true);
        tvAccount.setClickable(true);
        tvRegister.setClickable(true);

//        checkConnectedNetwork();
    }

    private void checkConnectedNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alert_dialog_network_checking);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
//            Button btnOk = dialog.findViewById(R.id.btnOk);
//            btnOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    recreate();
//                    InputMethodManager imm = (InputMethodManager)
//                            LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                    if (imm != null) {
//                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                    }
//                }
//            });
            dialog.show();
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
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //No button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Yes button clicked
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Bạn có muốn tắt ứng dụng?").setPositiveButton("Không", dialogClickListener)
                .setNegativeButton("Có", dialogClickListener).show();

    }
}
