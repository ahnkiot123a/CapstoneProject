package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.RegisterController;
import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etPhoneNumberRA;
    //ma vung Viet Nam la 84
    private Button btnContinue;
    private RegisterController registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnContinue = findViewById(R.id.btnContinue);
        //phone
        etPhoneNumberRA = findViewById(R.id.etPhoneNumberRA);

        //click button "Tiep tuc"
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etPhoneNumberRA.getText().toString().trim();
                registerController = new RegisterController(RegisterActivity.this);
                registerController.checkPhone(number);
                //registerController.checkExistUser(number);
            }
        });
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

    public Button getBtnContinue() {
        return btnContinue;
    }
}