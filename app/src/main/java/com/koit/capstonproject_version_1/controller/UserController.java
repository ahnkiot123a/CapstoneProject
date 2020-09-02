package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.koit.capstonproject_version_1.controller.Interface.IUser;
import com.koit.capstonproject_version_1.controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.helper.ProgressButton;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.view.LoginActivity;
import com.koit.capstonproject_version_1.view.MainActivity;
import com.koit.capstonproject_version_1.view.UserInformationActivity;
import com.koit.capstonproject_version_1.helper.CustomDialog;

public class UserController {

    private static final String TAG_FB = "FacebookLogin";

    private String phoneNumber;
    private UserInformationActivity userInformationActivity;
    private User user;
    User currentUser;
    private InputController inputController;

    public UserController() {
        user = new User();
        inputController = new InputController();
        //  userInformationActivity = new UserInformationActivity();
    }

    public UserController(UserInformationActivity userInformationActivity) {
        this.userInformationActivity = userInformationActivity;
        inputController = new InputController();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity);
                builder.setMessage("Không thể đăng nhập. Kiểm tra lại kết nối mạng ");
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

    public void loginWithPhoneAndPassword(final TextInputEditText etPhoneNumber, final TextInputEditText etPassword, final Activity activity, View view, final Button btnFbLogin, final TextView tvForgotPassword, final TextView tvAccount, final TextView tvRegister) {
        final CustomDialog dialog = new CustomDialog(activity);
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
                        SharedPrefs.getInstance().putCurrentUser(LoginActivity.CURRENT_USER, currentUser);
                        Toast.makeText(activity.getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                        progressButton.progressSuccess();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                                intent.putExtra("currentUser", currentUser);
                                activity.startActivity(intent);
                            }
                        }, 500); // afterDelay will be executed after 500 milliseconds.

                    } else {
//                        Toast.makeText(activity.getApplicationContext(), "Mật khẩu không đúng", Toast.LENGTH_LONG).show();
//                        dialog.showErrorDialog("Mật khẩu không đúng. Quý khách vui lòng nhập lại.");
                        etPassword.requestFocus();
                        Drawable customErrorDrawable = activity.getResources().getDrawable(R.drawable.ic_baseline_error_24);
                        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
                        etPassword.setError("Mật khẩu không đúng!", customErrorDrawable);
                        setClickableView(etPhoneNumber, etPassword, btnFbLogin, tvForgotPassword, tvAccount, tvRegister, true);
                        progressButton.progressInitiation();

                    }
                } else {
                    etPhoneNumber.requestFocus();
                    etPhoneNumber.setError("Tài khoản không tồn tại!");
                    setClickableView(etPhoneNumber, etPassword, btnFbLogin, tvForgotPassword, tvAccount, tvRegister, true);
                    progressButton.progressInitiation();
                }

            }
        };


        if (inputController.isPhoneNumber(etPhoneNumber)) {
            if (inputController.isPassword(etPassword, activity)) {
                phoneNumber = etPhoneNumber.getText().toString().trim();
                phoneNumber = inputController.formatPhoneNumber(phoneNumber);
                user.getUserWithPhoneAndPasswordInterface(phoneNumber, iUser);
            } else {
                setClickableView(etPhoneNumber, etPassword, btnFbLogin, tvForgotPassword, tvAccount, tvRegister, true);
                progressButton.progressInitiation();
            }
        } else {
            setClickableView(etPhoneNumber, etPassword, btnFbLogin, tvForgotPassword, tvAccount, tvRegister, true);
            progressButton.progressInitiation();
        }
    }

    public void setClickableView(TextInputEditText etPhoneNumber, TextInputEditText etPassword, Button btnFbLogin,
                                 TextView tvForgotPassword, TextView tvAccount, TextView tvRegister, boolean status) {
        etPhoneNumber.setClickable(status);
        etPassword.setClickable(status);
        btnFbLogin.setClickable(status);
        tvForgotPassword.setClickable(status);
        tvRegister.setClickable(status);
        tvAccount.setClickable(status);
    }


    public void updateUserInformation(EditText edFullname, EditText edEmail,
                                      EditText edPhoneNumber, TextView tvDob,
                                      RadioButton rbMale, EditText edAddress,
                                      EditText edStorename, User currentUser
    ) {

        String fullName = edFullname.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        String storeName = edStorename.getText().toString().trim();
        String dob = tvDob.getText().toString().trim();
        boolean gender = rbMale.isChecked();

        if (fullName.isEmpty()) {
            //  Toast.makeText(userInformationActivity.getApplicationContext(),"Vui lòng nhập họ và tên",Toast.LENGTH_SHORT).show();
            userInformationActivity.setErrorEditTxt("Vui lòng nhập họ và tên", userInformationActivity.getEdFullname());
        } else if (!inputController.isEmail(email)) {
            userInformationActivity.setErrorEditTxt("Email không hợp lệ, vui lòng nhập lại Email", userInformationActivity.getEdEmail());
            //  Toast.makeText(userInformationActivity.getApplicationContext(),"Email không hợp lệ, vui lòng nhập lại Email",Toast.LENGTH_SHORT).show();
        } else if (storeName.trim().equals("")) {
            userInformationActivity.setErrorEditTxt("Vui lòng nhập tên cửa hàng", userInformationActivity.getEdStoreName());
            //Toast.makeText(userInformationActivity.getApplicationContext(),"Vui lòng nhập tên cửa hàng",Toast.LENGTH_SHORT).show();
        } else {
            user.updateUserToFirebase(currentUser, fullName, email, gender, dob, address, storeName);
            Toast.makeText(userInformationActivity.getApplicationContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setGender(gender);
            currentUser.setDateOfBirth(dob);
            currentUser.setAddress(address);
            currentUser.setStoreName(storeName);
            SharedPrefs.getInstance().putCurrentUser(LoginActivity.CURRENT_USER, currentUser);

            Intent intent = new Intent(userInformationActivity.getApplicationContext(), MainActivity.class);
            intent.putExtra("currentUser", currentUser);
            userInformationActivity.startActivity(intent);
            //userInformationActivity.onBackPressed();
        }
    }

    public void logout(final Activity activity) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
//        View view = LayoutInflater.from(activity).inflate(R.layout.layout_warning_dialog,
//                (ConstraintLayout) activity.findViewById(R.id.layoutDialog));
//        builder.setView(view);
//        TextView tvMessage = view.findViewById(R.id.tvMessage);
//        TextView tvTitle = view.findViewById(R.id.tvTitle);
//        Button btnCancel = view.findViewById(R.id.btnCancel);
//        Button btnConfirm = view.findViewById(R.id.btnConfirm);
//        tvTitle.setText("Đăng xuất");
//        tvMessage.setText("Bạn có muốn đăng xuất không?");
//        btnConfirm.setText("Đăng xuất");
//
//        final AlertDialog dialog = builder.create();
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPrefs.getInstance().clear();
//                LoginManager.getInstance().logOut();
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(activity, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(activity, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                activity.startActivity(intent);
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        dialog.show();

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setMessage("Bạn có muốn đăng xuất không?")
                .setCancelable(true)
                .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPrefs.getInstance().clear();
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(activity, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.theme));
                alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.red_chrome));
            }
        });
        alert.show();
    }


}

