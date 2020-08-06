package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.R;

public class DebitConfirmationActivity extends AppCompatActivity {
    private TextView tvDebtorName, tvDebtorPhone, tvDebtorDebitAmount,
            tvDateTime, tvDebitMoney, tvNewDebitAmount;
    private Button btnConfirmDebit;
    private Debtor debtor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_confirmation);
        initView();
        getDebtor();
        setInformation();
    }

    private void setInformation() {
        tvDebtorName.setText(debtor.getFullName());
        tvDebtorPhone.setText(debtor.getPhoneNumber());
    }

    private void getDebtor() {
        Intent intent =getIntent();
        debtor = (Debtor) intent.getSerializableExtra("debtor");
    }


    private void initView() {
        tvDebtorName = findViewById(R.id.tvDebtorName);
        tvDebtorPhone = findViewById(R.id.tvDebtorPhone);
        tvDebtorDebitAmount = findViewById(R.id.tvDebtorDebitAmount);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvDebitMoney = findViewById(R.id.tvDebitMoney);
        tvNewDebitAmount = findViewById(R.id.tvNewDebitAmount);
        btnConfirmDebit = findViewById(R.id.btnConfirmDebit);
    }
}