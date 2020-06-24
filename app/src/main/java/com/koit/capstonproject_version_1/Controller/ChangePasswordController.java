package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.View.ChangePasswordActivity;
import com.koit.capstonproject_version_1.View.ForgotPasswordActivity;
import com.koit.capstonproject_version_1.View.MainActivity;

public class ChangePasswordController {


    private User user;
    private ValidateController validateController;
    private ChangePasswordActivity changePasswordActivity;

    public ChangePasswordController() {
        validateController = new ValidateController();
        user = new User();
    }

    public ChangePasswordController(ChangePasswordActivity changePasswordActivity) {
        this.changePasswordActivity = changePasswordActivity;
        user = new User();
        validateController = new ValidateController();

    }
    public void changePassword(EditText edOldPassword, EditText edNewPassword,
                               EditText edConfirmNewPassword,  User currentUser
                                  ) {
        String oldPassword = edOldPassword.getText().toString();
        String newPassword = edNewPassword.getText().toString();
        String confirmNewPassword = edConfirmNewPassword.getText().toString();
        if(oldPassword.length()<6 ){
          //  Toast.makeText(changePasswordActivity.getApplicationContext(),"Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
            changePasswordActivity.setErrorEditTxt("Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",changePasswordActivity.getEdOldPassword());
        } else if (!validateController.getMd5(oldPassword).equals(currentUser.getPassword())) {
            changePasswordActivity.setErrorEditTxt("Mật khẩu cũ không đúng, vui lòng nhập lại",changePasswordActivity.getEdOldPassword());
           // Toast.makeText(changePasswordActivity.getApplicationContext(),"Mật khẩu cũ không đúng",Toast.LENGTH_SHORT).show();
        }
        else  if(newPassword.length() <6){
            //  Toast.makeText(changePasswordActivity.getApplicationContext(),"Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
            changePasswordActivity.setErrorEditTxt("Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",changePasswordActivity.getEdNewPassword());
        } else  if(confirmNewPassword.length() <6){
            //  Toast.makeText(changePasswordActivity.getApplicationContext(),"Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
            changePasswordActivity.setErrorEditTxt("Mật khẩu có chứa 6 ký tự số trở lên, vui lòng nhập lại",changePasswordActivity.getEdConfirmNewPassword());
        }
        else if (!newPassword.equals(confirmNewPassword)){
           // Toast.makeText(changePasswordActivity.getApplicationContext(),"Mật khẩu xác nhận không đúng, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
            changePasswordActivity.setErrorEditTxt("Mật khẩu xác nhận không đúng, vui lòng nhập lại",changePasswordActivity.getEdConfirmNewPassword());

        } else if (oldPassword.equals(confirmNewPassword)){
            changePasswordActivity.setErrorEditTxt("Mật khẩu cũ và mới trùng nhau, vui lòng nhập lại",changePasswordActivity.getEdNewPassword());
           // Toast.makeText(changePasswordActivity.getApplicationContext(),"Mật khẩu cũ và mới trùng nhau, vui lòng nhập lại",Toast.LENGTH_SHORT).show();
        }else {
            user.changePasswordToFirebase(currentUser,validateController.getMd5(confirmNewPassword));
            Toast.makeText(changePasswordActivity.getApplicationContext(),"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();

            currentUser.setPassword(validateController.getMd5(confirmNewPassword));
            Intent intent = new Intent(changePasswordActivity.getApplicationContext().getApplicationContext(), MainActivity.class);
            intent.putExtra("currentUser", currentUser);
            changePasswordActivity.startActivity(intent);

        }
    }
}
