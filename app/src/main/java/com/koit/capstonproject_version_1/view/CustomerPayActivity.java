package com.koit.capstonproject_version_1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.koit.capstonproject_version_1.controller.CreateOrderController;
import com.koit.capstonproject_version_1.controller.RandomStringController;
import com.koit.capstonproject_version_1.controller.TimeController;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.model.InvoiceDetail;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.MoneyEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerPayActivity extends AppCompatActivity {
    private TextView tvTotalQuantity, tvTotalPrice, tvCustomerPaid,
            tvMoneyChange, tvCustomerDebit, tvToolbarTitle;
    private List<Product> listSelectedProductWarehouse, listSelectedProductInOrder;
    private MoneyEditText etSaleMoney, etPaidMoney;
    private Button btnSubmitPaid;
    private InvoiceDetail invoiceDetail;
    private long totalProductQuantity = 0;
    private long totalPrice = 0;
    private CreateOrderController createOrderController;
    private long debitAmount, customerPaid;
    private long firstPaid, discount;
    private String invoiceDate, invoiceTime;
    private boolean isDrafted;
    private String invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_payment);
        initView();
        tvToolbarTitle.setText("Thanh toán");
        createOrderController = new CreateOrderController(this);
        getInvoiceDetail();
        setData();
        inputCustomerPaid();
        inputSaleMoney();
        actionBtnSubmitPaid();
//        etPaidMoney.selectAll();
    }

    private void setData() {
        totalProductQuantity = createOrderController.calTotalProductQuantity(listSelectedProductInOrder);
        tvTotalQuantity.setText(totalProductQuantity + "");
        totalPrice = createOrderController.calTotalPrice(listSelectedProductInOrder);
        tvTotalPrice.setText(Money.getInstance().formatVN(totalPrice));
        tvCustomerPaid.setText(Money.getInstance().formatVN(totalPrice));
        etPaidMoney.setText(Money.getInstance().formatVN(totalPrice));
        tvCustomerDebit.setText("0");
    }

    private void getInvoiceDetail() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        listSelectedProductWarehouse = (ArrayList<Product>) args.getSerializable("listSelectedProductWarehouse");
        listSelectedProductInOrder = (ArrayList<Product>) args.getSerializable("listSelectedProductInOrder");
        for (int i = 0; i < listSelectedProductWarehouse.size(); i++) {
            Log.d("paymentWarehouse", listSelectedProductWarehouse.get(i).toString());
        }
        for (int i = 0; i < listSelectedProductInOrder.size(); i++) {
            Log.d("paymentInorder", listSelectedProductInOrder.get(i).toString());
        }
        listSelectedProductInOrder = createOrderController.formatListProductInOrder(listSelectedProductInOrder);
        listSelectedProductWarehouse = createOrderController.formatListProductWarehouse(listSelectedProductWarehouse);
        for (int i = 0; i < listSelectedProductWarehouse.size(); i++) {
            Log.d("paymentWarehouseAfter", listSelectedProductWarehouse.get(i).toString());
        }
        for (int i = 0; i < listSelectedProductInOrder.size(); i++) {
            Log.d("paymentInorderAfter", listSelectedProductInOrder.get(i).toString());
        }
    }

    private void actionBtnSubmitPaid() {
        btnSubmitPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceId = RandomStringController.getInstance().randomInvoiceId();
                debitAmount = Money.getInstance().reFormatVN(tvCustomerDebit.getText().toString());
                discount = Money.getInstance().reFormatVN(etSaleMoney.getText().toString());
                firstPaid = Money.getInstance().reFormatVN(etPaidMoney.getText().toString());
                customerPaid = Money.getInstance().reFormatVN(tvCustomerPaid.getText().toString());
                invoiceDate = TimeController.getInstance().getCurrentDate();
                invoiceTime = TimeController.getInstance().getCurrentTime();
                Invoice invoice = new Invoice(invoiceId, "", invoiceDate, invoiceTime, "", debitAmount, discount,
                        firstPaid, totalPrice, isDrafted);
                InvoiceDetail invoiceDetail = new InvoiceDetail(invoiceId, listSelectedProductInOrder);
                if (debitAmount == 0) {
                //    invoice.setFirstPaid(customerPaid);
                    createOrderController.addInvoiceToFirebase(invoice);
                    createOrderController.addInvoiceDetailToFirebase(invoiceDetail);
                    createOrderController.updateUnitQuantity(listSelectedProductInOrder, listSelectedProductWarehouse);
                    for (int i = 0; i < listSelectedProductWarehouse.size(); i++) {
                        Log.d("warehouseAfter", listSelectedProductWarehouse.get(i).toString());
                    }
                    Intent intent = new Intent(CustomerPayActivity.this, SelectProductActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(CustomerPayActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                } else {
                    invoice.setDrafted(true);
                  /*  Intent intent2 = new Intent(CustomerPayActivity.this, SelectDebtorActivity.class);
                    Bundle args2 = new Bundle();
                    args2.putSerializable("invoice", invoice);
                    args2.putSerializable("invoiceDetail", invoiceDetail);
                    args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
                    intent2.putExtra("BUNDLE", args2);
                    startActivity(intent2);*/
                    Intent intent = new Intent(CustomerPayActivity.this, SelectDebtorActivity.class);
                    intent.putExtra("invoice", invoice);
                    intent.putExtra("invoiceDetail", invoiceDetail);
                    Bundle args2 = new Bundle();
                    args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
                    intent.putExtra("BUNDLE", args2);
                    startActivity(intent);

                }

            }
        });
    }

    private void inputSaleMoney() {
        createOrderController.inputSaleMoney(etSaleMoney, tvCustomerPaid, totalPrice, etPaidMoney);
    }

    private void inputCustomerPaid() {
        createOrderController.inputPaidMoney(etPaidMoney, tvCustomerPaid, tvMoneyChange, tvCustomerDebit, btnSubmitPaid);

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