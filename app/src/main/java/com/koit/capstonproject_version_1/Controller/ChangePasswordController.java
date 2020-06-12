package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.View.ChangePasswordActivity;
import com.koit.capstonproject_version_1.View.ForgotPasswordActivity;

public class ChangePasswordController {
    ChangePasswordActivity changePasswordActivity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User user;
    private ValidateController validateController;

    public ChangePasswordController() {
        validateController = new ValidateController();
        user = new User();
    }

    public ChangePasswordController(ChangePasswordActivity changePasswordActivity, User user) {
        this.changePasswordActivity = changePasswordActivity;
        this.user = user;
    }
    public void changePassword(EditText edOldPassword, EditText edNewPassword,
                               EditText edConfirmNewPassword,  User currentUser,
                               Activity activity, DatabaseReference databaseReference) {

        String oldPassword = edOldPassword.getText().toString();
        String newPassword = edNewPassword.getText().toString();
        String confirmNewPassword = edConfirmNewPassword.getText().toString();
        if(oldPassword.length()<6 || newPassword.length() <6 || confirmNewPassword.length() <6){
            Toast.makeText(activity,"Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
        } else if (!validateController.getMd5(oldPassword).equals(currentUser.getPassword())) {
            Toast.makeText(activity,"Mật khẩu cũ không đúng",Toast.LENGTH_SHORT).show();
        } else if (!newPassword.equals(confirmNewPassword)){
            Toast.makeText(activity,"Mật khẩu xác nhận không đúng, vui lòng nhập lại",Toast.LENGTH_SHORT).show();

        } else if (oldPassword.equals(confirmNewPassword)){
            Toast.makeText(activity,"Mật khẩu cũ và mới trùng nhau, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
        }else {
            user.changePasswordToFirebase(databaseReference,currentUser,validateController.getMd5(confirmNewPassword));
            Toast.makeText(activity,"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
            currentUser.setPassword(validateController.getMd5(confirmNewPassword));
        }
    }
}
