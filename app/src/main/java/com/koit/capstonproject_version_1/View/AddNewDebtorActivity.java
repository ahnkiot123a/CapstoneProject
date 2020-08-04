package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.R;

public class AddNewDebtorActivity extends AppCompatActivity {
    private TextInputEditText edFullname, edEmail, edPhoneNumber, edDob, edAddress;
    private RadioButton rbMale, rbFemale;
    private DebtorController debtorController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_debtor);
        initView();
        debtorController = new DebtorController(this);
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
        debtorController.createDebtor(edFullname,edEmail,edPhoneNumber,edDob,edAddress,rbMale);
    }
}