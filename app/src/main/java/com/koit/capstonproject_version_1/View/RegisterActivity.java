package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.koit.capstonproject_version_1.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText;
    //ma vung Viet Nam la 84
    private final String CODE = "84";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText = findViewById(R.id.etPhoneNumber);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString().trim();

                if (!checkPhonenumber(number)) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }
                String phoneNumber = "+" + CODE + number;
                Intent intent = new Intent(RegisterActivity.this, RegisterVerifyPhone.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);
            }
        });
    }
    //check phone number is valid or not

    private boolean checkPhonenumber(String number) {
        //bat dau = 0 hoac NOT bat dau = 0
        String regexStr = "^[0]{0,1}+[0-9]{9}$";
        if (number.matches(regexStr)) {
            return true;
        } else return false;
    }
}