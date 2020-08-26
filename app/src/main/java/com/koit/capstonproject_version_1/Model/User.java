package com.koit.capstonproject_version_1.Model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IUser;
import com.koit.capstonproject_version_1.Controller.RegisterController;
import com.koit.capstonproject_version_1.Model.UIModel.Dialog;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.LoginActivity;
import com.koit.capstonproject_version_1.View.MainActivity;
import com.koit.capstonproject_version_1.View.RegisterVerifyPhoneActivity;
import com.koit.capstonproject_version_1.View.ResetPasswordActivity;
import com.koit.capstonproject_version_1.helper.CustomToast;

import java.io.Serializable;

public class User implements Serializable {
    RegisterVerifyPhoneActivity registerVerifyPhoneActivity;
    ResetPasswordActivity resetPasswordActivity;
    RegisterController registerController;

    private String userID, fullName, address, email, storeName;
    private String dateOfBirth;
    private String phoneNumber, password, roleID;
    boolean hasFingerprint, gender;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;

    }

    public User(RegisterVerifyPhoneActivity registerVerifyPhoneActivity) {
        this.registerVerifyPhoneActivity = registerVerifyPhoneActivity;
    }

    public User(ResetPasswordActivity resetPasswordActivity) {
        this.resetPasswordActivity = resetPasswordActivity;
    }

    public User() {

    }

    public User(String storeName, String password) {
        this.storeName = storeName;
        this.password = password;
    }

    public User(String fullName, String address, String email, String storeName, String dateOfBirth, String password, String roleID, boolean hasFingerprint, boolean gender) {
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.storeName = storeName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.roleID = roleID;
        this.hasFingerprint = hasFingerprint;
        this.gender = gender;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isHasFingerprint() {
        return hasFingerprint;
    }

    public void setHasFingerprint(boolean hasFingerprint) {
        this.hasFingerprint = hasFingerprint;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", storeName='" + storeName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", roleID='" + roleID + '\'' +
                ", hasFingerprint=" + hasFingerprint +
                ", gender=" + gender +
                '}';
    }

    public void handleFacebookAccessToken(AccessToken token, LoginResult loginResult, FirebaseAuth firebaseAuth, final Activity LoginActivity, final String TAG_FB) {
        Log.d(TAG_FB, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        final Dialog dialog = new Dialog(LoginActivity);
        dialog.showLoadingDialog(R.raw.triangle_loading);
        // [END_EXCLUDE]
        token.getPermissions();
        String tokenID = loginResult.getAccessToken().getToken();
        AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG_FB, "signInWithCredential:success");
                    Toast.makeText(LoginActivity.getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.getApplicationContext(), MainActivity.class);
                    LoginActivity.startActivity(intent);


                } else {
                    Log.d(TAG_FB, "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginActivity.getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }

                // [START_EXCLUDE]
                dialog.dismissLoadingDialog();
                // [END_EXCLUDE]
            }
        });

    }

    public void getUserWithPhoneAndPasswordInterface(String phoneNumber, final IUser iUser) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(phoneNumber);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                iUser.getCurrentUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    public void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(registerVerifyPhoneActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(registerVerifyPhoneActivity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            registerVerifyPhoneActivity.startActivity(intent);
                            CustomToast.makeText(registerVerifyPhoneActivity, "Đăng kí thành công!", Toast.LENGTH_LONG
                                    , CustomToast.SUCCESS ,true, Gravity.CENTER).show();
                        } else {
                            registerVerifyPhoneActivity.showTextError("Mã OTP không chính xác.", registerVerifyPhoneActivity.getEtOTP());
                            // Toast.makeText(RegisterVerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signInTheUserByCredentialsFromResetPassword(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(resetPasswordActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(resetPasswordActivity, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            resetPasswordActivity.startActivity(intent);
                            Toast.makeText(resetPasswordActivity, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();


                        } else {
                            resetPasswordActivity.showTextError("Mã OTP không chính xác.", resetPasswordActivity.getEtOTP());
                            Toast.makeText(resetPasswordActivity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void updateUserToFirebase( User currentUser,
                                     String fullName, String email, boolean gender, String dob,
                                     String address, String storeName ){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
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
    }
    public void changePasswordToFirebase(User currentUser, String confirmNewPassword){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.child(currentUser.getPhoneNumber()).child("password").setValue(confirmNewPassword);
    }

}
