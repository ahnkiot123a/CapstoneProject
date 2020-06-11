package com.koit.capstonproject_version_1.Controller;

import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputController {

    public InputController() {

    }

    //Format phone number to 10 digit
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
        if (phone.isEmpty()) {
            inputEditText.setError("Số điện thoại không được để trống!");
            inputEditText.requestFocus();
            return false;
        } else {
            inputEditText.setError(null);
            if(!isPhoneNumber(phone)){
                inputEditText.setError("Hãy nhập đúng số điện thoại của bạn!");
                inputEditText.requestFocus();
                return false;
            }
        }
        return true;
    }
    //Check a string phone number satisfy phone number format
    public boolean isPhoneNumber(String phone){
        String regex = "((09|03|07|08|05)+([0-9]{8})\\b)";
        phone = formatPhoneNumber(phone);
        if (phone.isEmpty()) {
            return false;
        } else {
            if (!phone.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPassword(TextInputEditText inputEditText){
        String password = inputEditText.getText().toString().trim();
        if (password.isEmpty()) {
            inputEditText.setError("Mật khẩu không được để trống!");
            inputEditText.requestFocus();
            return false;
        } else {
            inputEditText.setError(null);
            if (!isPassword(password)) {
                inputEditText.setError("Mật khẩu chỉ chứa số và có ít nhất 6 ký tự!");
                inputEditText.requestFocus();
                return false;
            }
        }
        return true;
    }
    // check a String satisfy password format
    public boolean isPassword(String password){
        String regex = "^[0-9]{6,}$";
        if (password.isEmpty()) {
            return false;
        } else {
            if (!password.matches(regex)) {
                return false;
            }
        }
        return true;
    }
    public boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public boolean isDate(String strDate)
    {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))
        {
            return true;
        }
        /* Date is not 'null' */
        else
        {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try
            {
                Date javaDate = sdfrmt.parse(strDate);
                System.out.println(strDate+" is valid date format");
            }
            /* Date format is invalid */
            catch (ParseException e)
            {
                System.out.println(strDate+" is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }
}
