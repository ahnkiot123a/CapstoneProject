package com.koit.capstonproject_version_1.Controller;

import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.R;

public class InputController {

    public InputController() {

    }

    public String formatPhoneNumber(String phone){
        if (phone.startsWith("+84")) {
            phone = "0" + phone.subSequence(3,phone.length());

        }
        return phone;
    }

    public boolean isPhoneNumber(TextInputEditText inputEditText) {
        String regex = "((09|03|07|08|05)+([0-9]{8})\\b)";
        String phone = inputEditText.getText().toString().trim();

        phone = formatPhoneNumber(phone);
        Log.d("phone", phone);
        Log.d("phone", phone.matches(regex) + "");

        if (phone.isEmpty()) {
            inputEditText.setError("Số điện thoại không được để trống!");
            inputEditText.requestFocus();
            return false;
        } else {
            inputEditText.setError(null);
            if (!phone.matches(regex)) {
                inputEditText.setError("Số điện thoại không đúng!");
                inputEditText.requestFocus();
                return false;
            }
        }
        return true;
    }

    public boolean isPassword(TextInputEditText inputEditText){
        String regex = "^[0-9]{6,}$";
        String password = inputEditText.getText().toString().trim();
        if (password.isEmpty()) {
            inputEditText.setError("Mật khẩu không được để trống!");
            inputEditText.requestFocus();
            return false;
        } else {
            inputEditText.setError(null);
            if (!password.matches(regex)) {
                inputEditText.setError("Mật khẩu chỉ chứa số và có ít nhất 6 ký tự!");
                inputEditText.requestFocus();
                return false;
            }
        }
        return true;
    }
}
