package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.ItemProductInvDetailAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailController {
    private Activity activity;
    private InvoiceDetail invoiceDetail;
    private InvoiceHistoryDAO invoiceHistoryDAO = new InvoiceHistoryDAO();

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
                listProductInOrder.add(product);
            }

            @Override
            public void getListProductInWarehouse(Product product) {
                listProductInWarehouse.add(product);
            }
        };
        invoiceDetail.getListProductInOrder(iInvoiceDetail, invoiceId);
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
        invoiceHistoryDAO.getDebtorById(id, iDebtor);
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
}
