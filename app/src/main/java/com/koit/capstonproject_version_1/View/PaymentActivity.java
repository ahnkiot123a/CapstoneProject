package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvTotalQuantity, tvTotalPrice, tvCustomerPaid,
            tvMoneyChange, tvCustomerDebit, tvToolbarTitle;
    private TextInputEditText etSaleMoney, etPaidMoney;
    private Button btnSubmitPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        tvToolbarTitle.setText("Thanh toán");
        getInvoice();

        inputCustomerPaid();
        inputSaleMoney();
        actionBtnSubmitPaid();
    }

    private void getInvoice() {

    }

    private void actionBtnSubmitPaid() {
        btnSubmitPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void inputSaleMoney() {
        etSaleMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sale = s.toString();
                long totalSum = Long.parseLong(tvTotalPrice.getText().toString());
                long salePrice = 0;
                try {
                    salePrice  = Long.parseLong(sale);
                } catch (Exception e){
                    salePrice = 0;
                }

                tvCustomerPaid.setText(totalSum-salePrice+"");
                etPaidMoney.setText(totalSum-salePrice+"");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void inputCustomerPaid() {
        etPaidMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String paid = s.toString();
                long customerPaid = 0;
                try {
                    customerPaid  = Long.parseLong(paid);
                } catch (Exception e){
                    customerPaid = 0;
                }
                long changeMoney = customerPaid - Long.parseLong(tvCustomerPaid.getText().toString());
                if(changeMoney >= 0){
                    tvMoneyChange.setText(changeMoney + "");
                    tvCustomerDebit.setText("0");
                } else {
                    tvCustomerDebit.setText(-changeMoney + "");
                    tvMoneyChange.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCustomerPaid = findViewById(R.id.tvCustomerPaid);
        tvMoneyChange = findViewById(R.id.tvMoneyChange);
        tvCustomerDebit = findViewById(R.id.tvCustomerDebit);
        etSaleMoney = findViewById(R.id.etSaleMoney);
        etPaidMoney = findViewById(R.id.etPaidMoney);
        btnSubmitPaid = findViewById(R.id.btnSubmitPaid);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
    }

    public void back(View view) {
        onBackPressed();
    }

}