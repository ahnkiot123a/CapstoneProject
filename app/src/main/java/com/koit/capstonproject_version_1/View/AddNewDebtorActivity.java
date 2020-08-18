package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.util.Calendar;
import java.util.Date;

public class AddNewDebtorActivity extends AppCompatActivity {
    private TextInputEditText edFullname, edEmail, edPhoneNumber, edAddress;
    private RadioButton rbMale, rbFemale;
    private TextView tvDob;
    private DebtorController debtorController;
    private Debtor debtor;

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
        tvDob = findViewById(R.id.tvDob);
        edAddress = findViewById(R.id.edAddress);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
    }

    public void saveNewCustomer(View view) {
        boolean success = debtorController.createDebtor(edFullname, edEmail, edPhoneNumber, tvDob, edAddress, rbMale);
        if (success) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }


    }

    public void getNewDate(View view) {
        Date dob = new Date();
        if (!tvDob.getText().toString().trim().equals(""))
            dob = TimeController.getInstance().getDateAndMonthFromText(tvDob.getText().toString(), dob);
        TimeController.getInstance().chooseDayDialog(tvDob, dob, this);
    }

    public void back(View view) {
        onBackPressed();
    }

}