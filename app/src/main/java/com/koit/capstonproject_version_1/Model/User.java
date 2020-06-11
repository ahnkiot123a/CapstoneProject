package com.koit.capstonproject_version_1.Model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
import com.koit.capstonproject_version_1.View.MainActivity;
import com.koit.capstonproject_version_1.View.RegisterVerifyPhone;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class User implements Serializable {
    RegisterVerifyPhone registerVerifyPhone;
    RegisterController registerController;
    private String fullName, address, email, storeName;
    private String dateOfBirth;
    private String phoneNumber, password, roleID;
    boolean hasFingerprint, gender;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child("FrwiGmQyScgZ6RZRRE25BHO56Ws2");
//        }
    }

    public User(RegisterVerifyPhone registerVerifyPhone) {
        this.registerVerifyPhone = registerVerifyPhone;
    }

    public User() {
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child("FrwiGmQyScgZ6RZRRE25BHO56Ws2");
//        }
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
    public void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(registerVerifyPhone, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(registerVerifyPhone, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            registerVerifyPhone.startActivity(intent);

                        } else {
                            registerVerifyPhone.showTextError("Mã OTP không chính xác.", registerVerifyPhone.getEtOTP());

                            // Toast.makeText(RegisterVerifyPhone.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        dialog.showLoadingDialog();
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


}
