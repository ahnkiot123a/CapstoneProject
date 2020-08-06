package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Controller.PaymentController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

public class DebitConfirmationActivity extends AppCompatActivity {
    private TextView tvDebtorName, tvDebtorPhone, tvOldDebtAmount,
            tvDateTime, tvDebitMoney, tvNewDebitAmount, invoiceName;
    private Button btnConfirmDebit;
    private Debtor debtor;
    private Invoice invoice;
    private InvoiceDetail invoiceDetail;
    private PaymentController paymentController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_confirmation);
        paymentController = new PaymentController(this);
        initView();
        getData();
        setInformation();
        actionBtnConfirmDebit();
    }

    private void actionBtnConfirmDebit() {
        btnConfirmDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice.setDrafted(false);
                paymentController.addInvoiceToFirebase(invoice);
                paymentController.addInvoiceDetailToFirebase(invoiceDetail);
            }
        });
    }

    private void setInformation() {
        tvDebtorName.setText(debtor.getFullName());
        tvDebtorPhone.setText(debtor.getPhoneNumber());
        invoiceName.setText("Hoá đơn " + invoice.getInvoiceId() );
        tvDateTime.setText(invoice.getInvoiceDate() + " " + invoice.getInvoiceTime());
        tvDebitMoney.setText(Money.getInstance().formatVN(invoice.getDebitAmount()));
        long oldDebtAmount = Money.getInstance().reFormatVN(tvOldDebtAmount.getText().toString());
        long newDebtAmount = oldDebtAmount + invoice.getDebitAmount();
        tvNewDebitAmount.setText(Money.getInstance().formatVN(newDebtAmount));
    }

    private void getData() {
        Intent intent =getIntent();
        debtor = (Debtor) intent.getSerializableExtra("debtor");
        invoice = (Invoice) intent.getSerializableExtra("invoice");
        invoiceDetail = (InvoiceDetail) intent.getSerializableExtra("invoiceDetail");
        invoice.setDebtorId(debtor.getDebtorId());
    }


    private void initView() {
        tvDebtorName = findViewById(R.id.tvDebtorName);
        tvDebtorPhone = findViewById(R.id.tvDebtorPhone);
        tvOldDebtAmount = findViewById(R.id.tvOldDebtAmount);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvDebitMoney = findViewById(R.id.tvDebitMoney);
        tvNewDebitAmount = findViewById(R.id.tvNewDebitAmount);
        btnConfirmDebit = findViewById(R.id.btnConfirmDebit);
        invoiceName = findViewById(R.id.invoiceName);
    }
}