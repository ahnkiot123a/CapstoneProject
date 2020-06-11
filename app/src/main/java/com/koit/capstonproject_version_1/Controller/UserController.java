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
import com.koit.capstonproject_version_1.Model.Feedback;
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
                    ValidateController validateController = new ValidateController();
                    String password = etPassword.getText().toString();
                    password = validateController.getMd5(password);
                    if (password.equals(currentUser.getPassword())) {
                        Toast.makeText(activity.getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                        progressButton.progressSuccess();
                        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
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


    public void updateUserInformation(EditText edFullname, EditText edEmail,
                                      EditText edPhoneNumber, EditText edDob,
                                      RadioButton rbMale, EditText edAddress,
                                      EditText edStorename, User currentUser,
                                      Activity activity, DatabaseReference databaseReference) {

        String fullName = edFullname.getText().toString();
        String email = edEmail.getText().toString();
        String phoneNumber = edPhoneNumber.getText().toString();
        String address = edAddress.getText().toString();
        String storeName = edStorename.getText().toString();
        String dob = edDob.getText().toString();
        boolean gender = (rbMale.isChecked()) ? true : false;

        if (fullName.trim().equals("")){
            Toast.makeText(activity.getApplicationContext(),"Vui lòng nhập họ và tên",Toast.LENGTH_SHORT).show();
        } else if (email.trim().equals("")){
            Toast.makeText(activity.getApplicationContext(),"Vui lòng nhập Email",Toast.LENGTH_SHORT).show();
        } else if (!inputController.isEmail(email)){
            Toast.makeText(activity.getApplicationContext(),"Email không hợp lệ, vui lòng nhập lại Email",Toast.LENGTH_SHORT).show();
        } else if (dob.trim().equals("")){
            Toast.makeText(activity.getApplicationContext(),"Vui lòng nhập ngày sinh",Toast.LENGTH_SHORT).show();
        }else if (!inputController.isDate(dob)){
            Toast.makeText(activity.getApplicationContext(),"Ngày sinh không hợp lệ, vui lòng nhập lại ngày sinh",Toast.LENGTH_SHORT).show();
        } else if (address.trim().equals("")){
            Toast.makeText(activity.getApplicationContext(),"Vui lòng nhập địa chỉ",Toast.LENGTH_SHORT).show();
        } else if (storeName.trim().equals("")){
            Toast.makeText(activity.getApplicationContext(),"Vui lòng nhập tên cửa hàng",Toast.LENGTH_SHORT).show();
        }   else {
            databaseReference.child(currentUser.getPhoneNumber()).child("fullName").setValue(fullName);
            databaseReference.child(currentUser.getPhoneNumber()).child("email").setValue(email);
            databaseReference.child(currentUser.getPhoneNumber()).child("gender").setValue(gender);
            databaseReference.child(currentUser.getPhoneNumber()).child("dateOfBirth").setValue(dob);
            databaseReference.child(currentUser.getPhoneNumber()).child("address").setValue(address);
            databaseReference.child(currentUser.getPhoneNumber()).child("storeName").setValue(storeName);
            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setGender(gender);
            currentUser.setDateOfBirth(dob);
            currentUser.setAddress(address);
            currentUser.setStoreName(storeName);
            Toast.makeText(activity.getApplicationContext(),"Cập nhật thông tin thành công",Toast.LENGTH_SHORT).show();

        }
    }
    public void changePassword(EditText edOldPassword, EditText edNewPassword,
                               EditText edConfirmNewPassword,  User currentUser,
                               Activity activity, DatabaseReference databaseReference) {

        String oldPassword = edOldPassword.getText().toString();
        String newPassword = edNewPassword.getText().toString();
        String confirmNewPassword = edConfirmNewPassword.getText().toString();
        if(oldPassword.length()<6 || newPassword.length() <6 || confirmNewPassword.length() <6){
            Toast.makeText(activity,"Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
        } else if (!oldPassword.equals(currentUser.getPassword())) {
            Toast.makeText(activity,"Mật khẩu cũ không đúng",Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmNewPassword)){
            Toast.makeText(activity,"Mật khẩu xác nhận không đúng, vui lòng nhập lại",Toast.LENGTH_SHORT).show();

        } else if (oldPassword.equals(confirmNewPassword)){
            Toast.makeText(activity,"Mật khẩu cũ và mới trùng nhau, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
        }else {
            databaseReference.child(currentUser.getPhoneNumber()).child("password").setValue(confirmNewPassword);
            Toast.makeText(activity,"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
            currentUser.setPassword(confirmNewPassword);
        }
    }
}

