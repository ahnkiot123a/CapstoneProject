package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.koit.capstonproject_version_1.Controller.PaymentController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class DebitConfirmationActivity extends AppCompatActivity {
    private TextView tvDebtorName, tvDebtorPhone, tvOldDebtAmount,
            tvDateTime, tvDebitMoney, tvNewDebitAmount, invoiceName,tvToolbarTitle;
    private Button btnConfirmDebit;
    private Debtor debtor;
    private Invoice invoice;
    private InvoiceDetail invoiceDetail;
    private List<Product> listSelectedProductWarehouse;
    private  List<Product> listSelectedProductInOrder;
    private PaymentController paymentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_confirmation);
        paymentController = new PaymentController(this);
        initView();
        tvToolbarTitle.setText("Xác nhận nợ");

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
                paymentController.updateUnitQuantity(listSelectedProductInOrder,listSelectedProductWarehouse);
                debtor.updateTotalDebit(debtor);
                Intent intent = new Intent(DebitConfirmationActivity.this,SelectProductActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(DebitConfirmationActivity.this, "Cho nợ thành công", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setInformation() {
        tvDebtorName.setText(debtor.getFullName());
        tvDebtorPhone.setText(debtor.getPhoneNumber());
        invoiceName.setText("Hoá đơn " + invoice.getInvoiceId() );
        tvDateTime.setText(invoice.getInvoiceDate() + "\n " + invoice.getInvoiceTime());
        tvDebitMoney.setText(Money.getInstance().formatVN(invoice.getDebitAmount()));
        long oldDebtAmount = debtor.getDebitTotal();
        tvOldDebtAmount.setText(Money.getInstance().formatVN(oldDebtAmount));
        long newDebtAmount = oldDebtAmount + invoice.getDebitAmount();
        tvNewDebitAmount.setText(Money.getInstance().formatVN(newDebtAmount));
        debtor.setDebitTotal(newDebtAmount);
    }

    private void getData() {
        Intent intent =getIntent();
        debtor = (Debtor) intent.getSerializableExtra("debtor");
        invoice = (Invoice) intent.getSerializableExtra("invoice");
        invoiceDetail = (InvoiceDetail) intent.getSerializableExtra("invoiceDetail");
        Bundle args = intent.getBundleExtra("BUNDLE");
        listSelectedProductWarehouse = (ArrayList<Product>) args.getSerializable("listSelectedProductWarehouse");
        listSelectedProductInOrder = invoiceDetail.getProducts();
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
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
    }
    public void back(View view) {
        onBackPressed();
    }

}