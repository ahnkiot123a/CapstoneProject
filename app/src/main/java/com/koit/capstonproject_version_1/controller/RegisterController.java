package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.controller.Interface.IUser;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.view.ForgotPasswordActivity;
import com.koit.capstonproject_version_1.view.LoginActivity;
import com.koit.capstonproject_version_1.view.RegisterActivity;
import com.koit.capstonproject_version_1.view.RegisterInputPhoneActivity;
import com.koit.capstonproject_version_1.view.ResetPasswordActivity;
import com.koit.capstonproject_version_1.helper.CustomToast;

import java.util.concurrent.TimeUnit;

public class RegisterController {
    RegisterInputPhoneActivity registerInputPhoneActivity;
    RegisterActivity registerActivity;
    ForgotPasswordActivity forgotPasswordActivity;
    ResetPasswordActivity resetPasswordActivity;
    InputController inputController;
    private String phoneNumber;
    private int otpCounter = 0;
    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken token;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User user;


    public RegisterController(RegisterInputPhoneActivity registerInputPhoneActivity) {
        this.registerInputPhoneActivity = registerInputPhoneActivity;
        user = new User();
    }

    public RegisterController(RegisterActivity registerActivity, String phoneNumber) {
        this.registerActivity = registerActivity;
        this.phoneNumber = phoneNumber;
    }


    //check phone number is valid form or not
    public void checkPhone(final String phone) {
        inputController = new InputController();
        if (!inputController.isPhoneNumber(phone)) {
            registerInputPhoneActivity.showTextError("Số điện thoại không chính xác.", registerInputPhoneActivity.getEtPhoneNumberRA());
        } else {
            final IUser iUser = new IUser() {
                @Override
                public void getCurrentUser(User user) {
                    if (user != null) {
                        registerInputPhoneActivity.showTextError("Số điện thoại đã tồn tại.", registerInputPhoneActivity.getEtPhoneNumberRA());
                    } else
                        //move to new Activity
                        tranIntent(registerInputPhoneActivity, phone);
                }
            };
            user.getUserWithPhoneAndPasswordInterface(phone, iUser);

        }
    }


    //check Store name, password, confirmPassword, OTP
    public void checkRegister(String storeName, String pass, String confirmPass, String otpCode, String phoneNumber) {
        if (!checkStoreName(storeName)) return;
        if (!checkPass(pass)) return;
        if (!checkConfirmPass(pass, confirmPass)) return;
        if (!checkOTPCode(otpCode)) return;
        verifyCode(otpCode, storeName, pass);

    }

    //otp code is 6 number degits or not
    private boolean checkOTPCode(String otpCode) {
        if (otpCode.isEmpty()) {
            registerActivity.showTextError("Vui lòng nhập OTP", registerActivity.getEtOTP());
            return false;
        } else if (otpCode.length() != 6) {
            registerActivity.showTextError("Mã OTP phải bao gồm 6 kí tự", registerActivity.getEtOTP());
            return false;
        }
        return true;
    }

    private boolean checkConfirmPass(String pass, String confirmPass) {
        if (pass.equals(confirmPass))
            return true;
        else {
            registerActivity.showTextErrorNoIcon("Mật khẩu không khớp.", registerActivity.getEtConfirmPassword());
        }
        return false;
    }

    //check password is true format(>=6 number digits) or not
    private boolean checkPass(String pass) {
        inputController = new InputController();
        if (pass.isEmpty()) {
            registerActivity.showTextErrorNoIcon("Vui lòng nhập mật khẩu.", registerActivity.getEtPassword());
        } else {
            //gom it nhat 6 ki tu so
            String regexStr = "^[0-9]{6,}$";
            if (pass.matches(regexStr)) {
                return true;
            } else {
                registerActivity.showTextErrorNoIcon("Vui lòng nhập 6 kí tự số trở lên.", registerActivity.getEtPassword());
            }
        }
        return false;
    }

    //check store name is empty or not
    public boolean checkStoreName(String storeName) {
        if ((storeName.isEmpty())) {
            registerActivity.showTextError("Vui lòng nhập tên cửa hàng.", registerActivity.getEtStoreNameRVP());
            return false;
        }
        return true;
    }

    //resend OTP code
    public void resendVerificationCode(String phoneNumber,
                                       PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        otpCounter = 0;
    }

    public void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + number,
                120,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            token = forceResendingToken;
            CustomToast.makeText(registerActivity, "Mã xác nhận OTP đã được gửi tới máy bạn!",
                    Toast.LENGTH_LONG, CustomToast.SUCCESS, true, Gravity.CENTER).show();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                verifyCode(code);
//            }
        }

        @Override
        //This method is called in response to an invalid verification request,
        // such as a request that specifies an invalid phone number or verification code.
        public void onVerificationFailed(@NonNull FirebaseException e) {
            CustomToast.makeText(registerActivity, "Tạm thời không thể gửi mã OTP",
                    Toast.LENGTH_LONG, CustomToast.CONFUSING, true, Gravity.CENTER).show();
            Intent intent = new Intent(registerActivity, LoginActivity.class);
            registerActivity.startActivity(intent);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
//            CustomToast.makeText(registerActivity, "Mã OTP của bạn đã hết hạn. Vui lòng nhấn vào 'Gửi lại mã'",
//                    Toast.LENGTH_LONG, CustomToast.WARNING, true, Gravity.CENTER).show();
        }
    };

    //resend OTP code
    public void resendOTPCode() {
        resendVerificationCode(phoneNumber, token);
    }

    //check OTP code is valid or not
    private void verifyCode(String code, String storeName, String pass) {
        //OTP code must be check <=3 times
        Log.d("verifyCodeafter", "1");
        otpCounter++;
        PhoneAuthCredential credential = null;
        try {
            credential = PhoneAuthProvider.getCredential(verificationId, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (credential != null) {
            //OPT expired
            if (otpCounter >= 3) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                resendOTPCode();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(registerActivity);
                builder.setMessage("Mã OTP của bạn đã hết hạn, vui lòng gửi lại mã.").setPositiveButton("Gửi lại mã", dialogClickListener)
                        .setNegativeButton("Thoát", dialogClickListener).show();
            } else {
                User user = new User(registerActivity);
                Log.d("beforesignIn", "1");
                if (credential != null)
                    user.signInTheUserByCredentials(credential, storeName, pass, phoneNumber);
            }
        } else {
            CustomToast.makeText(registerActivity, "Tạm thời không thể gửi mã OTP",
                    Toast.LENGTH_LONG, CustomToast.CONFUSING, true, Gravity.BOTTOM).show();
        }
    }

    //Chuyen sang man hinh verify OTP code va mat khau
    public void tranIntent(Activity fromActivity, String number) {
        Intent intent = new Intent(fromActivity, RegisterActivity.class);
        intent.putExtra("phonenumber", number);
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(registerInputPhoneActivity.getEtPhoneNumberRA(), "phoneTran");
        pairs[1] = new Pair<View, String>(registerInputPhoneActivity.getBtnContinue(), "btnDoneTran");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(registerInputPhoneActivity, pairs);
        registerInputPhoneActivity.startActivity(intent, options.toBundle());
    }


}
