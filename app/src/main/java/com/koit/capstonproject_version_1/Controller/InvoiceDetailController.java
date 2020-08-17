package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.DebtorAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailController {
    private Activity activity;
    private InvoiceDetail invoiceDetail;

    public InvoiceDetailController(Activity activity) {
        this.activity = activity;
        invoiceDetail =new InvoiceDetail();
    }

    public InvoiceDetailController() {
    }
    public void getListProductInOrder(String invoiceId){
        final List<Product> listProductInOrder = new ArrayList<>();
        final List<Product>  listProductInWarehouse = new ArrayList<>();
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//        recyclerViewDebtor.setLayoutManager(layoutManager);
//        debtorAdapter = new DebtorAdapter(debtorList, context, invoice, invoiceDetail, listSelectedProductWarehouse);
//        recyclerViewDebtor.setAdapter(debtorAdapter);
       /* ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                products.add(product);
            }
        };*/
        IInvoiceDetail iInvoiceDetail = new IInvoiceDetail() {
            @Override
            public void getListProductInOrder(Product product) {
                listProductInOrder.add(product);
            }

            @Override
            public void getListProductInWarehouse(Product product) {

            }
        };


        invoiceDetail.getListProductInOrder(iInvoiceDetail,invoiceId);
    }
}
