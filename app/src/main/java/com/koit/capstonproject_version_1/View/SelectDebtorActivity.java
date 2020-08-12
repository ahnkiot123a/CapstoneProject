package com.koit.capstonproject_version_1.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.Controller.PaymentController;
import com.koit.capstonproject_version_1.Controller.RandomStringController;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class SelectDebtorActivity extends AppCompatActivity {
    private RecyclerView recyclerViewDebitors;
    private DebtorController debtorController;
    private Invoice invoice;
    private InvoiceDetail invoiceDetail;
    private PaymentController paymentController;
    private List<Product> listSelectedProductWarehouse;
    public static final int REQUEST_ADD_DEBTOR_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debitor);
        paymentController = new PaymentController(this);
        initView();
        getData();
        buildRecyclerviewDebtors();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("invoice") != null
                && intent.getSerializableExtra("invoiceDetail") != null) {
            invoice = (Invoice) intent.getSerializableExtra("invoice");
            invoiceDetail = (InvoiceDetail) intent.getSerializableExtra("invoiceDetail");
            Bundle args = intent.getBundleExtra("BUNDLE");
            listSelectedProductWarehouse = (List<Product>) args.getSerializable("listSelectedProductWarehouse");
        } else {
            Bundle args = intent.getBundleExtra("BUNDLE");
            listSelectedProductWarehouse = (ArrayList<Product>) args.getSerializable("listSelectedProductWarehouse");
            List<Product> listSelectedProductInOrder = (ArrayList<Product>) args.getSerializable("listSelectedProductInOrder");

            long totalPrice = paymentController.calTotalPrice(listSelectedProductInOrder);
            listSelectedProductInOrder = paymentController.formatListProductInOrder(listSelectedProductInOrder);
            listSelectedProductWarehouse = paymentController.formatListProductWarehouse(listSelectedProductWarehouse);
            String invoiceId = RandomStringController.getInstance().randomInvoiceId();
            long debitAmount = totalPrice;
            long discount = 0;
            long firstPaid = 0;
            String invoiceDate = TimeController.getInstance().getCurrentDate();
            String invoiceTime = TimeController.getInstance().getCurrentTime();
            invoice = new Invoice(invoiceId, "", invoiceDate, invoiceTime,
                    "", debitAmount, discount, firstPaid, totalPrice, true);
            invoiceDetail = new InvoiceDetail(invoiceId, listSelectedProductInOrder);
            // paymentController.updateUnitQuantity(listSelectedProductInOrder,listSelectedProductWarehouse);

        }

    }

    private void initView() {
        recyclerViewDebitors = findViewById(R.id.recyclerViewDebitors);
    }

    private void buildRecyclerviewDebtors() {
        debtorController = new DebtorController(this.getApplicationContext(), invoice, invoiceDetail,listSelectedProductWarehouse);
        debtorController.getListDebtor(recyclerViewDebitors, invoice, invoiceDetail);
    }

    public void addNewCustomer(View view) {
        Intent intent = new Intent(SelectDebtorActivity.this, AddNewDebtorActivity.class);
        startActivityForResult(intent,REQUEST_ADD_DEBTOR_CODE);
    }

    public void searchCustomer(View view) {

    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_DEBTOR_CODE && resultCode == Activity.RESULT_OK) {
            buildRecyclerviewDebtors();
        }
    }
}