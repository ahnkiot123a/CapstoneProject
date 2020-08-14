package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.util.Calendar;

public class AddNewDebtorActivity extends AppCompatActivity {
    private TextInputEditText edFullname, edEmail, edPhoneNumber, edDob, edAddress;
    private RadioButton rbMale, rbFemale;
    private DebtorController debtorController;
    private Debtor debtor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
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
       boolean success =  debtorController.createDebtor(edFullname, edEmail, edPhoneNumber, edDob, edAddress, rbMale);
       if(success){
           Intent intent = new Intent();
           setResult(Activity.RESULT_OK, intent);
           finish();
       }


    }

    public void getNewDate(View view) {

        DatePickerDialog.OnDateSetListener mListener  = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                String date = i1 < 10 ? i + "-0" + i1 + "-" + i2 : i + "-" + i1 + "-" + i2;
                String updateDate = i1 + "/" + i2 + "/" + i;
                edDob.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, mListener, year, month, day);
        dialog.show();
    }

    public void back(View view) {
        onBackPressed();
    }

}