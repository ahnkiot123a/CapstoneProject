package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.ItemProductInvDetailAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailController {
    private Activity activity;
    private InvoiceDetail invoiceDetail;
    final List<Product> listProductInOrder = new ArrayList<>();
    final List<Product> listProductInWarehouse = new ArrayList<>();

    private ItemProductInvDetailAdapter productInvDetailAdapter;

    public InvoiceDetailController(Activity activity) {
        this.activity = activity;
        invoiceDetail = new InvoiceDetail();
    }

    public InvoiceDetailController() {
    }

    public void getListProductInOrder(String invoiceId) {
        IInvoiceDetail iInvoiceDetail = new IInvoiceDetail() {
            @Override
            public void getListProductInOrder(Product product) {
                listProductInOrder.add(product);
            }

            @Override
            public void getListProductInWarehouse(Product product) {
                listProductInWarehouse.add(product);
            }
        };
        invoiceDetail.getListProductInOrder(iInvoiceDetail, invoiceId);
    }

    public void setProductInOrderDetail(RecyclerView recyclerView, String invoiceId, TextView tvProductTotal){
        getListProductInOrder(invoiceId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        productInvDetailAdapter = new ItemProductInvDetailAdapter(listProductInOrder, activity, tvProductTotal);
        recyclerView.setAdapter(productInvDetailAdapter);

    }
}
