package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputController {

    public InputController() {

    }

    public String formatPhoneNumber(String phone) {
        if (phone.startsWith("+84")) {
            phone = "0" + phone.subSequence(3, phone.length());

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
            if (!isPhoneNumber(phone)) {
                inputEditText.setError("Hãy nhập đúng số điện thoại của bạn!");
                inputEditText.requestFocus();
                return false;
            }
        }
        return true;
    }

    public static boolean checkValidNumber(String numStr, int maxNumOfDigits) {
        numStr = numStr.trim();
        if (numStr.trim().length() > maxNumOfDigits) return false;
        else if (!numStr.matches("\\d+")) return false;
        return true;
    }

    public boolean isPhoneNumber(String phone) {
        phone = formatPhoneNumber(phone);
        String regex = "((09|03|07|08|05)+([0-9]{8})\\b)";
        if (phone.isEmpty()) {
            return false;
        } else {
            if (!phone.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPassword(TextInputEditText inputEditText, Activity activity) {
        Drawable customErrorDrawable = activity.getResources().getDrawable(R.drawable.ic_baseline_error_24);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        String regex = "^[0-9]{6,}$";
        String password = inputEditText.getText().toString().trim();
        if (password.isEmpty()) {
            inputEditText.requestFocus();
            inputEditText.setError("Mật khẩu không được để trống!", customErrorDrawable);
            return false;
        } else {
            inputEditText.setError(null);
            if (!password.matches(regex)) {
                inputEditText.requestFocus();
                inputEditText.setError("Mật khẩu chỉ chứa số và có ít nhất 6 ký tự!", customErrorDrawable);
                return false;
            }
        }
        return true;
    }

    public boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (email.equals("")) return true;
        return email.matches(regex);
    }

    public boolean isDate(String strDate) {
        /* Check if date is 'null' */
        if (strDate.trim().equals("")) {
            return true;
        }
        /* Date is not 'null' */
        else {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd-MM-yyyy");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try {
                Date javaDate = sdfrmt.parse(strDate);
                System.out.println(strDate + " is valid date format");
            }
            /* Date format is invalid */ catch (ParseException e) {
                System.out.println(strDate + " is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }
}
