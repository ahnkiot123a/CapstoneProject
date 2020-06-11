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
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edOldPassword, edNewPassword, edConfirmNewPassword;
    private Button btnChangePassword;
    private DatabaseReference databaseReference;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");
        findViewById();
        actionBtnChangePassword();
    }

    private void actionBtnChangePassword() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edOldPassword.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                String confirmNewPassword = edConfirmNewPassword.getText().toString();
                if(oldPassword.length()<6 || newPassword.length() <6 || confirmNewPassword.length() <6){
                    Toast.makeText(ChangePasswordActivity.this,"Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
                } else if (!oldPassword.equals(currentUser.getPassword())) {
                    Toast.makeText(ChangePasswordActivity.this,"Mật khẩu cũ không đúng",Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmNewPassword)){
                    Toast.makeText(ChangePasswordActivity.this,"Mật khẩu xác nhận không đúng, vui lòng nhập lại",Toast.LENGTH_SHORT).show();

                } else if (oldPassword.equals(confirmNewPassword)){
                    Toast.makeText(ChangePasswordActivity.this,"Mật khẩu cũ và mới trùng nhau, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child(currentUser.getPhoneNumber()).child("password").setValue(confirmNewPassword);
                    Toast.makeText(ChangePasswordActivity.this,"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
                    currentUser.setPassword(confirmNewPassword);
                }
            }
        });
    }


    private void findViewById(){
        edOldPassword = findViewById(R.id.edOldPassword);
        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmNewPassword = findViewById(R.id.edConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }

}