package com.koit.capstonproject_version_1.Model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
import com.koit.capstonproject_version_1.View.MainActivity;

public class User {

    public User() {
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


}
