package com.koit.capstonproject_version_1.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.controller.ForgotPasswordController;
import com.koit.capstonproject_version_1.helper.MyDialog;
import com.koit.capstonproject_version_1.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResetPasswordActivity extends AppCompatActivity {


    private TextInputEditText etPhoneNumberRVP;
    private Button btnDone;
    private TextInputEditText etOTP;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private ForgotPasswordController forgotPasswordController;
    private String phoneNumber;
    private Disposable internetDisposable;
    private MyDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();

        phoneNumber = getIntent().getStringExtra("phonenumber");
        Log.d("PhoneNumber", phoneNumber);
        etPhoneNumberRVP.setText(phoneNumber);

        forgotPasswordController = new ForgotPasswordController(ResetPasswordActivity.this, phoneNumber);
        //send OTP
        forgotPasswordController.sendVerificationCode(phoneNumber);
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
        etPhoneNumberRVP = findViewById(R.id.etPhoneNumberRVP);
        etOTP = findViewById(R.id.etOTP);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnDone = findViewById(R.id.btnDone);
    }

    public void sendNewPassword(android.view.View view) {
        dialog.showDefaultLoadingDialog(R.raw.material_loading);
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String otpCode = etOTP.getText().toString().trim();
        forgotPasswordController.checkInputFromResetPasswordActivity(password, confirmPassword, otpCode, dialog);
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

    public void showTextErrorNoIcon(String error, TextInputEditText et) {
        et.requestFocus();
        Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_baseline_error_24);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        et.setError(error, customErrorDrawable);
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