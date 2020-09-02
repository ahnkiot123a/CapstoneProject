package com.koit.capstonproject_version_1.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.controller.ForgotPasswordController;
import com.koit.capstonproject_version_1.helper.MyDialog;
import com.koit.capstonproject_version_1.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etPhoneNumber;
    private LottieAnimationView lottieAnimationView;

    private ForgotPasswordController forgotPasswordController;
    private Disposable internetDisposable;
    private MyDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        forgotPasswordController = new ForgotPasswordController(ForgotPasswordActivity.this);
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
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        lottieAnimationView = findViewById(R.id.animationView);
    }

    public void sendPhoneNumberToResetPasswordActivity(android.view.View view) {
        String number = etPhoneNumber.getText().toString().trim();
        forgotPasswordController.checkPhoneNumber(number);
    }

    public void back(android.view.View view) {
        onBackPressed();
    }


    public void showTextError(String error, TextInputEditText et) {
        et.requestFocus();
        et.setError(error);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lottieAnimationView.setVisibility(View.GONE);
    }

    public TextInputEditText getEtPhoneNumber() {
        return etPhoneNumber;
    }

    public LottieAnimationView getLottieAnimationView() {
        return lottieAnimationView;
    }
}