package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class InvoiceDetailActivity extends AppCompatActivity {

    private TextView tvInvoiceTime, tvProductTotal, tvTotalPrice, tvDiscount, tvMustPayMoney, tvPaidMoney, tvDebtMoney;
    private TextView tvDebtorName, tvDebtorPhone;
    private RecyclerView rvProduct;

    private Invoice invoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_invoice_detail);

        Intent intent = getIntent();
        invoice = (Invoice) intent.getSerializableExtra(InvoiceHistoryActivity.INVOICE);

        initView();
        setInvoiceValue();



    }

    private void setInvoiceValue() {
        tvInvoiceTime.setText(invoice.getInvoiceTime() + " " + invoice.getInvoiceDate());
        tvTotalPrice.setText(Money.getInstance().formatVN(invoice.getTotal()));
        tvDiscount.setText(Money.getInstance().formatVN(invoice.getDiscount()));
        long mustPayMoney = invoice.getTotal() - invoice.getDiscount();
        tvMustPayMoney.setText(Money.getInstance().formatVN(mustPayMoney));
        tvPaidMoney.setText(Money.getInstance().formatVN(invoice.getFirstPaid()));
        long debtMoney = mustPayMoney > invoice.getFirstPaid() ? mustPayMoney - invoice.getFirstPaid() : 0;
        tvDebtMoney.setText(Money.getInstance().formatVN(debtMoney));
    }

    private void initView() {

        tvInvoiceTime = findViewById(R.id.tvInvoiceTime);
        tvProductTotal = findViewById(R.id.tvProductTotal);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvMustPayMoney = findViewById(R.id.tvMustPayMoney);
        tvPaidMoney = findViewById(R.id.tvPaidMoney);
        tvDebtMoney = findViewById(R.id.tvDebtMoney);
        tvDebtorName = findViewById(R.id.tvDebtorName);
        tvDebtorPhone = findViewById(R.id.tvDebtorPhone);
        rvProduct = findViewById(R.id.rvProduct);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setTextSize(22);
        tvToolbarTitle.setTypeface(null, Typeface.BOLD);
        tvToolbarTitle.setText(invoice.getInvoiceId());
    }

    public void back(View view) {
        onBackPressed();
    }
}