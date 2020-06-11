package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.koit.capstonproject_version_1.Controller.Interface.IUser;
import com.koit.capstonproject_version_1.Model.UIModel.ProgressButton;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.ChangePasswordActivity;
import com.koit.capstonproject_version_1.View.FeedbackActivity;
import com.koit.capstonproject_version_1.View.MainActivity;
import com.koit.capstonproject_version_1.View.UserInformationActivity;

public class UserController {

    private static final String TAG_FB = "FacebookLogin";

    private String phoneNumber;

    private User user;
    User currentUser;
    private InputController inputController;

    public UserController() {
        user = new User();
        inputController = new InputController();

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

    public void loginWithPhoneAndPassword(final TextInputEditText etPhoneNumber, final TextInputEditText etPassword, final Activity activity, View view) {

        final ProgressButton progressButton = new ProgressButton(activity, view);
        final IUser iUser = new IUser() {
            @Override
            public void getCurrentUser(User user) {
                currentUser = user;
                if (currentUser != null) {
                    currentUser.setPhoneNumber(phoneNumber);
                    Log.d("user", currentUser.toString());
                    String password = etPassword.getText().toString();
                    if (password.equals(currentUser.getPassword())) {
                        Toast.makeText(activity.getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                        progressButton.progressSuccess();
                        Intent intent = new Intent(activity.getApplicationContext(), FeedbackActivity.class);
                        intent.putExtra("currentUser", currentUser);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "Mật khẩu không đúng", Toast.LENGTH_LONG).show();
                        progressButton.progressError();
                    }
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Tài khoản không tồn tại", Toast.LENGTH_LONG).show();
                    progressButton.progressError();
                }

            }
        };


        if (inputController.isPhoneNumber(etPhoneNumber)) {
            if (inputController.isPassword(etPassword)) {
                phoneNumber = etPhoneNumber.getText().toString().trim();
                phoneNumber = inputController.formatPhoneNumber(phoneNumber);
                user.getUserWithPhoneAndPasswordInterface(phoneNumber, iUser);
            }else {
                progressButton.progressError();
            }
        }else{
            progressButton.progressError();
        }
    }


    public void changePassword(EditText edOldPassword, EditText edNewPassword, EditText edConfirmNewPassword, User currentUser, ChangePasswordActivity changePasswordActivity, DatabaseReference databaseReference) {
    }

    public void updateUserInformation(EditText edFullname, EditText edEmail, EditText edPhoneNumber, EditText edDob, RadioButton rbMale, EditText edAddress, EditText edStoreName, User currentUser, UserInformationActivity userInformationActivity, DatabaseReference databaseReference) {
    }
}

