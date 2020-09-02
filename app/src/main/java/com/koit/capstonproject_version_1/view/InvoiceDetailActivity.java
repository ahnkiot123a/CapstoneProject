package com.koit.capstonproject_version_1.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.controller.InvoiceDetailController;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.R;

public class InvoiceDetailActivity extends AppCompatActivity {

    private TextView tvInvoiceTime, tvProductTotal, tvTotalPrice, tvDiscount, tvMustPayMoney, tvPaidMoney, tvFirstDebt, tvRemainingDebt;
    private TextView tvDebtorName, tvDebtorPhone;
    private RecyclerView rvProduct;
    private LinearLayout layoutDebit;
    private LinearLayout layoutLoading, layoutProduct;

    private Invoice invoice;
    private InvoiceDetailController invoiceDetailController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_invoice_detail);
        Intent intent = getIntent();
        invoice = (Invoice) intent.getSerializableExtra(OrderHistoryActivity.INVOICE);
        initView();


        invoiceDetailController = new InvoiceDetailController(this);
        invoiceDetailController.setProductInOrderDetail(rvProduct, invoice.getInvoiceId(), tvProductTotal, layoutLoading, layoutProduct);
        invoiceDetailController.setLayoutDebit(layoutDebit, tvDebtorName, tvDebtorPhone, invoice);


        setInvoiceValue();
    }

    private void initView() {

        tvInvoiceTime = findViewById(R.id.tvInvoiceTime);
        tvProductTotal = findViewById(R.id.tvProductTotal);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvMustPayMoney = findViewById(R.id.tvMustPayMoney);
        tvPaidMoney = findViewById(R.id.tvPaidMoney);
        tvFirstDebt = findViewById(R.id.tvFirstDebt);
        tvDebtorName = findViewById(R.id.tvDebtorName);
        tvDebtorPhone = findViewById(R.id.tvDebtorPhone);
        tvRemainingDebt = findViewById(R.id.tvRemainingDebt);
        rvProduct = findViewById(R.id.rvProduct);
        layoutDebit = findViewById(R.id.layoutDebit);
        layoutLoading = findViewById(R.id.layoutLoading);
        layoutProduct = findViewById(R.id.layoutProduct);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setTextSize(22);
        tvToolbarTitle.setTypeface(null, Typeface.BOLD);
        tvToolbarTitle.setText(invoice.getInvoiceId());
    }


    private void setInvoiceValue() {
        tvInvoiceTime.setText(String.format("%s   %s", invoice.getInvoiceTime(), invoice.getInvoiceDate()));
        tvTotalPrice.setText(Money.getInstance().formatVN(invoice.getTotal()) + " đ");
        tvDiscount.setText(Money.getInstance().formatVN(invoice.getDiscount()) + " đ");
        long mustPayMoney = invoice.getTotal() - invoice.getDiscount();
        tvMustPayMoney.setText(Money.getInstance().formatVN(mustPayMoney) + " đ");
        tvPaidMoney.setText(Money.getInstance().formatVN(invoice.getFirstPaid()) + " đ");
        long firstDebt = mustPayMoney > invoice.getFirstPaid() ? mustPayMoney - invoice.getFirstPaid() : 0;
        tvFirstDebt.setText(Money.getInstance().formatVN(firstDebt) + " đ");
        tvRemainingDebt.setText(Money.getInstance().formatVN(invoice.getDebitAmount()) + " đ");

    }


    public void back(View view) {
        onBackPressed();
    }

    public void callDebtor(View view) {
        invoiceDetailController.getDebtorAndSendToDebtPayment(invoice.getDebtorId());
    }
}