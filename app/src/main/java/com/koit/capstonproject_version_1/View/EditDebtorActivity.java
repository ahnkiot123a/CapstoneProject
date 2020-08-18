package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.util.Date;

public class EditDebtorActivity extends AppCompatActivity {
    private TextInputEditText edFullname,edEmail,edPhoneNumber,edAddress;
    private TextView tvFirstName,tvDob;
    private RadioButton rbMale,rbFemale;
    private Debtor debtor;
    private DebtorController debtorController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_debtor);
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Thông tin khách hàng");
        initView();
        debtorController = new DebtorController(this);
        debtor = new Debtor("KH20200804100001","hanoi","","luatnd@gmail.com","Nguyen Duc Luat","034413666",true,0);
        setDebtorInfo();
    }

    private void setDebtorInfo() {
        edFullname.setText(debtor.getFullName().trim());
        edEmail.setText(debtor.getEmail().trim());
        edPhoneNumber.setText(debtor.getPhoneNumber().trim());
        edAddress.setText(debtor.getAddress().trim());
        tvFirstName.setText(debtor.getFullName().charAt(0)+"");
        tvDob.setText(debtor.getDateOfBirth());
        if (debtor.isGender()) rbMale.setChecked(true); else rbFemale.setChecked(true);

    }

    private void initView() {
        edFullname = findViewById(R.id.edFullname);
        edEmail = findViewById(R.id.edEmail);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edAddress = findViewById(R.id.edAddress);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvDob = findViewById(R.id.tvDob);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
    }
    public void getNewDate(View view) {
        Date dob = new Date();
        dob = TimeController.getInstance().getDateAndMonthFromText(tvDob.getText().toString(), dob);
        TimeController.getInstance().chooseDayDialog(tvDob, dob, this);
    }
    public void back(View view){
        onBackPressed();
    }
    public void updateDebtor(View view){
      boolean success =  debtorController.updateDebtor(edFullname,edEmail,edPhoneNumber,tvDob,edAddress,rbMale,debtor);
      if (success){
//          debtorController.tranIntent(EditDebtorActivity.this,MainActivity.class);
      }
    }
}