package com.koit.capstonproject_version_1.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.controller.ChangePasswordController;
import com.koit.capstonproject_version_1.controller.UserController;
import com.koit.capstonproject_version_1.model.User;
import com.koit.capstonproject_version_1.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edOldPassword, edNewPassword, edConfirmNewPassword;
    private Button btnChangePassword;
    private DatabaseReference databaseReference;
    private User currentUser;
    private UserController userController;
    private ChangePasswordController changePasswordController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_change_password);
        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Đổi mật khẩu");
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        Intent intent = getIntent();
        currentUser =(User)intent.getSerializableExtra("currentUser");
        findViewById();
        userController = new UserController();
        changePasswordController = new ChangePasswordController(ChangePasswordActivity.this);
        actionBtnChangePassword();
    }

    private void actionBtnChangePassword() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordController.changePassword(edOldPassword,edNewPassword,edConfirmNewPassword,currentUser);
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


    public void setErrorEditTxt(String mess, EditText textInputEditText){
        textInputEditText.requestFocus();
        Drawable customErrorDrawable = this.getResources().getDrawable(R.drawable.ic_baseline_error_24);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        textInputEditText.setError(mess, customErrorDrawable);
    }

    public EditText getEdOldPassword() {
        return edOldPassword;
    }


    public EditText getEdNewPassword() {
        return edNewPassword;
    }


    public EditText getEdConfirmNewPassword() {
        return edConfirmNewPassword;
    }


}