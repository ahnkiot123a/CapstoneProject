package com.koit.capstonproject_version_1.controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.koit.capstonproject_version_1.view.ResetPasswordActivity;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordController {

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

    public ForgotPasswordController(ForgotPasswordActivity forgotPasswordActivity) {
        this.forgotPasswordActivity = forgotPasswordActivity;
        user = new User();
    }


    public ForgotPasswordController(ResetPasswordActivity resetPasswordActivity, String phoneNumber) {
        this.resetPasswordActivity = resetPasswordActivity;
        this.phoneNumber = phoneNumber;
    }

    //check phone number is valid form or not
    public void checkPhoneNumber(final String phone) {
        inputController = new InputController();
        if (!inputController.isPhoneNumber(phone)) {
            forgotPasswordActivity.showTextError("Số điện thoại không chính xác.", forgotPasswordActivity.getEtPhoneNumber());
        } else {
            forgotPasswordActivity.getLottieAnimationView().setVisibility(View.VISIBLE);
            final IUser iUser = new IUser() {
                @Override
                public void getCurrentUser(User user) {
                    if (user == null) {
                        forgotPasswordActivity.showTextError("Số điện thoại chưa được đăng ký!", forgotPasswordActivity.getEtPhoneNumber());
                        forgotPasswordActivity.getLottieAnimationView().setVisibility(View.GONE);

                    } else {
                        Intent intent = new Intent(forgotPasswordActivity, ResetPasswordActivity.class);
                        intent.putExtra("phonenumber", phone);
                        forgotPasswordActivity.startActivity(intent);
                    }
                }
            };
            user.getUserWithPhoneAndPasswordInterface(phone, iUser);

        }
    }


    //check Store name, password, confirmPassword, OTP
    public void checkInputFromResetPasswordActivity(String password, String confirmPassword, String otpCode) {
        if (!checkPasswordFromForgot(password)) return;
        if (!checkConfirmPasswordFromForgot(password, confirmPassword)) return;
        if (!checkOTPCodeForgotPassword(otpCode)) return;
        verifyCodeFromResetPassword(otpCode, phoneNumber, password);

    }


    //otp code is 6 number degits or not
    private boolean checkOTPCodeForgotPassword(String otpCode) {
        if (otpCode.isEmpty()) {
            resetPasswordActivity.showTextError("Vui lòng nhập OTP", resetPasswordActivity.getEtOTP());
            return false;
        } else if (otpCode.length() != 6) {
            resetPasswordActivity.showTextError("Mã OTP phải bao gồm 6 kí tự", resetPasswordActivity.getEtOTP());
            return false;
        }
        return true;
    }

    private boolean checkConfirmPasswordFromForgot(String pass, String confirmPass) {
        if (pass.equals(confirmPass))
            return true;
        else {
            resetPasswordActivity.showTextErrorNoIcon("Mật khẩu không khớp.", resetPasswordActivity.getEtConfirmPassword());
        }
        return false;
    }

    //check password is true format(>=6 number digits) or not
    private boolean checkPasswordFromForgot(String pass) {
        inputController = new InputController();
        if (pass.isEmpty()) {
            resetPasswordActivity.showTextErrorNoIcon("Vui lòng nhập mật khẩu!", resetPasswordActivity.getEtPassword());
        } else {
            //gom it nhat 6 ki tu so
            String regexStr = "^[0-9]{6,}$";
            if (pass.matches(regexStr)) {
                return true;
            } else {
                resetPasswordActivity.showTextErrorNoIcon("Mật khẩu phải có ít nhất 6 ký tự số!", resetPasswordActivity.getEtPassword());
            }
        }
        return false;
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
            Toast.makeText(resetPasswordActivity, "Mã xác nhận OTP đã được gửi tới máy bạn!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(resetPasswordActivity, "Tạm thời không thể gửi mã OTP!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            //  Toast.makeText(RegisterVerifyPhone.this, "Mã OTP của bạn đã hết hạn. Vui lòng nhấn vào 'Gửi lại mã'", Toast.LENGTH_LONG).show();
        }
    };

    //resend OTP code
    public void resendOTPCode() {
        resendVerificationCode(phoneNumber, token);
    }

    //check OTP code is valid or not
    private void verifyCodeFromResetPassword(String code, String phoneNumber, String password) {
        //OTP code must be check <=3 times
        Log.d("verifyCodeafter", "1");
        otpCounter++;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(resetPasswordActivity);
            builder.setMessage("Mã OTP của bạn đã hết hạn, vui lòng gửi lại mã.").setPositiveButton("Gửi lại mã", dialogClickListener)
                    .setNegativeButton("Thoát", dialogClickListener).show();
        } else {
            User user = new User(resetPasswordActivity);
            user.signInTheUserByCredentialsFromResetPassword(credential, phoneNumber, password);
        }
    }


}




