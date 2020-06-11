package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.UserController;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edOldPassword, edNewPassword, edConfirmNewPassword;
    private Button btnChangePassword;
    private DatabaseReference databaseReference;
    private User currentUser;
    private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");
        findViewById();
        userController = new UserController();
        actionBtnChangePassword();
    }

    private void actionBtnChangePassword() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userController.changePassword(edOldPassword,edNewPassword,edConfirmNewPassword,currentUser,ChangePasswordActivity.this,databaseReference);
            }
        });
    }


    private void findViewById(){
        edOldPassword = findViewById(R.id.edOldPassword);
        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmNewPassword = findViewById(R.id.edConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }
    public void back(View v){
        onBackPressed();
    }

}