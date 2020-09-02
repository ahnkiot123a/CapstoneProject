package com.koit.capstonproject_version_1.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.controller.RegisterController;
import com.koit.capstonproject_version_1.R;

public class RegisterInputPhoneActivity extends AppCompatActivity {
    private TextInputEditText etPhoneNumberRA;
    //ma vung Viet Nam la 84
    private Button btnContinue;
    private RegisterController registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        //click button "Tiep tuc"
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etPhoneNumberRA.getText().toString().trim();
                registerController = new RegisterController(RegisterInputPhoneActivity.this);
                registerController.checkPhone(number);
                //registerController.checkExistUser(number);
            }
        });
    }

    private void initView() {
        btnContinue = findViewById(R.id.btnContinue);
        etPhoneNumberRA = findViewById(R.id.etPhoneNumberRA);
    }

    public void showTextError(String error, TextInputEditText et) {
        et.requestFocus();
        et.setError(error);
    }

    public void back(View view) {
        onBackPressed();
    }

    public TextInputEditText getEtPhoneNumberRA() {
        return etPhoneNumberRA;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Button getBtnContinue() {
        return btnContinue;
    }
}