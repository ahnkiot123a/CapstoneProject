package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.CustomerController;
import com.koit.capstonproject_version_1.R;

public class AddNewCustomerActivity extends AppCompatActivity {
    private TextInputEditText edFullname, edEmail, edPhoneNumber, edDob, edAddress;
    private RadioButton rbMale, rbFemale;
    private CustomerController customerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        initView();
        customerController = new CustomerController(this);
    }

    private void initView() {
        edFullname = findViewById(R.id.edFullname);
        edEmail = findViewById(R.id.edEmail);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edDob = findViewById(R.id.edDob);
        edAddress = findViewById(R.id.edAddress);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
    }

    public void saveNewCustomer(View view) {
        customerController.createCustomer(edFullname,edEmail,edPhoneNumber,edDob,edAddress,rbMale);


    }
}