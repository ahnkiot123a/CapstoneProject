package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.Interface.IUser;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.View.RegisterActivity;
import com.koit.capstonproject_version_1.View.RegisterVerifyPhone;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class RegisterController {
    RegisterActivity registerActivity;
    RegisterVerifyPhone registerVerifyPhone;
    InputController inputController;
    private String phoneNumber;
    private int otpCounter = 0;
    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken token;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    boolean checkExistUser = false;
    private User user;

    public RegisterController() {
    }

    public RegisterController(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
        user = new User();
    }

    public RegisterController(RegisterVerifyPhone registerVerifyPhone, String phoneNumber) {
        this.registerVerifyPhone = registerVerifyPhone;
        this.phoneNumber = phoneNumber;
    }

    //check phone number is valid form or not
    public void checkPhone(String phone) {
        inputController = new InputController();
        if (!inputController.isPhoneNumber(phone)) {
            registerActivity.showTextError("Số điện thoại không chính xác.", registerActivity.getEtPhoneNumberRA());
        } else {
            if (checkExistUser(phone)) {
                registerActivity.showTextError("Số điện thoại đã tồn tại.", registerActivity.getEtPhoneNumberRA());
            } else
                //move to new Activity
                tranIntent(registerActivity, phone);
        }
    }

    //check Store name, password, confirmPassword, OTP
    public void checkRegister(String storeName, String pass, String confirmPass, String otpCode) {
        if (!checkStoreName(storeName)) return;
        if (!checkPass(pass)) return;
        if (!checkConfirmPass(pass, confirmPass)) return;
        if (!checkOTPCode(otpCode)) return;
        verifyCode(otpCode);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("User");
        ValidateController validateController = new ValidateController();
        //ma hoa mat khau
        pass = validateController.getMd5(pass);
        User user = new User("", "", "", storeName, "", pass, "", false, false);

        databaseReference.child(phoneNumber).setValue(user);
    }

    //otp code is 6 number degits or not
    private boolean checkOTPCode(String otpCode) {
        if (otpCode.isEmpty()) {
            registerVerifyPhone.showTextError("Vui lòng nhập OTP", registerVerifyPhone.getEtOTP());
            return false;
        } else if (otpCode.length() != 6) {
            registerVerifyPhone.showTextError("Mã OTP phải bao gồm 6 kí tự", registerVerifyPhone.getEtOTP());
            return false;
        }
        return true;
    }

    private boolean checkConfirmPass(String pass, String confirmPass) {
        if (pass.equals(confirmPass))
            return true;
        else {
            registerVerifyPhone.showTextError("Mật khẩu không khớp.", registerVerifyPhone.getEtConfirmPassword());
        }
        return false;
    }

    //check password is true format(>=6 number digits) or not
    private boolean checkPass(String pass) {
        inputController = new InputController();
        if (pass.isEmpty()) {
            registerVerifyPhone.showTextError("Vui lòng nhập mật khẩu.", registerVerifyPhone.getEtPassword());
        } else {
            //gom it nhat 6 ki tu so
            String regexStr = "^[0-9]{6,}$";
            if (pass.matches(regexStr)) {
                return true;
            } else {
                registerVerifyPhone.showTextError("Vui lòng nhập 6 kí tự số trở lên.", registerVerifyPhone.getEtPassword());
            }
        }
        return false;
    }


    //check store name is empty or not
    public boolean checkStoreName(String storeName) {
        if ((storeName.isEmpty())) {
            registerVerifyPhone.showTextError("Vui lòng nhập tên cửa hàng.", registerVerifyPhone.getEtStoreNameRVP());
            return false;
        }
        return true;
    }

    //resend OTP code
    public void resendVerificationCode(String phoneNumber,
                                       PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + phoneNumber,        // Phone number to verify
                90,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        otpCounter = 0;
    }

    public void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + number,
                90,
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
            Toast.makeText(registerVerifyPhone, "Mã xác nhận OTP đã được gửi tới máy bạn!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(registerVerifyPhone, e.getMessage(), Toast.LENGTH_LONG).show();
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
    private void verifyCode(String code) {
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

            AlertDialog.Builder builder = new AlertDialog.Builder(registerVerifyPhone);
            builder.setMessage("Mã OTP của bạn đã hết hạn, vui lòng gửi lại mã.").setPositiveButton("Gửi lại mã", dialogClickListener)
                    .setNegativeButton("Thoát", dialogClickListener).show();
        }
        User user = new User(registerVerifyPhone);
        Log.d("beforesignIn", "1");
        user.signInTheUserByCredentials(credential);
    }


    //Chuyen sang man hinh verify OTP code va mat khau
    public void tranIntent(Activity fromActivity, String number) {
        Intent intent = new Intent(fromActivity, RegisterVerifyPhone.class);
        intent.putExtra("phonenumber", number);
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(registerActivity.getEtPhoneNumberRA(), "phoneTran");
        pairs[1] = new Pair<View, String>(registerActivity.getBtnContinue(), "btnDoneTran");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(registerActivity, pairs);
        registerActivity.startActivity(intent, options.toBundle());
    }

    //check phone number
    //return false if user doest not exist
    public boolean checkExistUser(String phoneNumber) {
        final IUser iUser = new IUser() {
            @Override
            public void getCurrentUser(User user) {
                if (user != null) checkExistUser = true;
            }
        };
        user.getUserWithPhoneAndPasswordInterface(phoneNumber, iUser);
        return checkExistUser;
    }

}
