package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class UserController {

    private static final String TAG_FB = "FacebookLogin";

    private User user;


    public UserController() {

        user = new User();

    }

    public void loginWithFacebook(View view, LoginButton loginButton, CallbackManager callbackManager, final FirebaseAuth firebaseAuth, final Activity LoginActivity, Button btnFbLogin) {

        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(String.valueOf(R.string.TAG_FB), "facebook:onSuccess:" + loginResult);
                user.handleFacebookAccessToken(loginResult.getAccessToken(), loginResult, firebaseAuth, LoginActivity, TAG_FB);
            }

            @Override
            public void onCancel() {
                Log.d(TAG_FB, "facebook:onCancel");
                Toast.makeText(LoginActivity.getApplicationContext(), "Đã hủy đăng nhập", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG_FB, "facebook:onError", error);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.getApplicationContext());
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
}
