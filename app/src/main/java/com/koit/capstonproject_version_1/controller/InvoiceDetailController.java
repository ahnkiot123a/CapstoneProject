package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.adapter.ItemProductInvDetailAdapter;
import com.koit.capstonproject_version_1.controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.model.InvoiceDetail;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.view.DebitOfDebtorActivity;
import com.koit.capstonproject_version_1.view.CreateOrderActivity;
import com.koit.capstonproject_version_1.model.dao.OrderHistoryDAO;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailController {

    public static boolean loadSuccess = false;
    private Activity activity;
    private InvoiceDetail invoiceDetail;
    private OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();

    final private List<Product> listProductInOrder = new ArrayList<>();
    final private List<Product> listProductInWarehouse = new ArrayList<>();

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
                if (product != null)
                    listProductInOrder.add(product);
            }

            @Override
            public void getListProductInWarehouse(Product product) {
                if (product != null)
                    listProductInWarehouse.add(product);
            }

            @Override
            public void getListProductInOrderWWarehouse(Product productInOrder, Product productWarehouse) {

            }
        };
        invoiceDetail.getListProductInOrder(iInvoiceDetail, invoiceId);
    }

    public void getListProductInDraftOrder(String invoiceId) {
        IInvoiceDetail iInvoiceDetail = new IInvoiceDetail() {
            @Override
            public void getListProductInOrder(Product product) {
                if (product != null) {
                    listProductInOrder.add(product);
                }
            }

            @Override
            public void getListProductInWarehouse(Product product) {
                if (product != null) {
                    listProductInWarehouse.add(product);

                }
            }

            @Override
            public void getListProductInOrderWWarehouse(Product productInOrder, Product productWarehouse) {

            }
        };
        invoiceDetail.getListProductInDraftOrder(iInvoiceDetail, invoiceId);
    }

    public void sendDraftOrder(String invoiceId) {
//        getListProductInDraftOrder(invoiceId);
        Log.d("listProductInOrderDetai", listProductInOrder.toString());
        Log.d("listProInWarehouseDT", listProductInWarehouse.toString());
        Intent intent2 = new Intent(activity, CreateOrderActivity.class);
        Bundle args2 = new Bundle();
//        args2.putSerializable("listSelectedProductInOrder", (Serializable) listProductInOrder);
        args2.putString("invoiceId", invoiceId);
        intent2.putExtra("BUNDLE", args2);
        activity.startActivity(intent2);
    }


    public void setLayoutDebit(LinearLayout layoutDebit, TextView tvDebtorName, TextView tvDebtorPhone, Invoice invoice) {
        if (invoice.getDebtorId().isEmpty() || invoice.getDebtorId() == null) {
            layoutDebit.setVisibility(View.GONE);
        } else {
            tvDebtorName.setText(invoice.getDebtorName());
            fillDebtorPhone(invoice.getDebtorId(), tvDebtorPhone);
        }

    }

    //get debtor name and fill text view
    public void fillDebtorPhone(String id, final TextView textView) {
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                if (debtor != null) {
                    textView.setText(debtor.getPhoneNumber());
                }
            }
        };
        orderHistoryDAO.getDebtorById(id, iDebtor);
    }

    public void setProductInOrderDetail(RecyclerView rvProduct, String invoiceId, TextView tvProductTotal, final LinearLayout layoutLoading, final LinearLayout layoutProduct) {
        getListProductInOrder(invoiceId);
        new CountDownTimer(500, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                layoutLoading.setVisibility(View.GONE);
                layoutProduct.setVisibility(View.VISIBLE);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        rvProduct.setLayoutManager(layoutManager);
        productInvDetailAdapter = new ItemProductInvDetailAdapter(listProductInOrder, activity, tvProductTotal);
        rvProduct.setAdapter(productInvDetailAdapter);

    }

    public void getDebtorAndSendToDebtPayment(String debtorId) {
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                if(debtor != null){
                    Intent intent = new Intent(activity, DebitOfDebtorActivity.class);
                    intent.putExtra(DebitOfDebtorActivity.ITEM_DEBTOR, debtor);
                    activity.startActivity(intent);
                }
            }
        };
        orderHistoryDAO.getDebtorById(debtorId, iDebtor);
    }
}
