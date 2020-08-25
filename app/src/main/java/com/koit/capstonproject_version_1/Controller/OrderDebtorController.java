package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.OrderHistoryAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.View.InvoiceDetailActivity;
import com.koit.capstonproject_version_1.View.OrderHistoryActivity;
import com.koit.capstonproject_version_1.dao.OrderHistoryDAO;

import java.util.ArrayList;

public class OrderDebtorController {

    private Activity activity;
    private OrderHistoryDAO orderHistoryDAO;
    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<Invoice> invoiceList;

    public OrderDebtorController(Activity activity) {
        this.activity = activity;
        orderHistoryDAO = new OrderHistoryDAO();
    }

    public void orderDebtorList(final Debtor debtor, final RecyclerView recyclerViewListProduct, final TextView tvOrderTotal,
                                final ConstraintLayout layoutNotFound) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(invoiceList, activity, tvOrderTotal);
        recyclerViewListProduct.setAdapter(orderHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null) {
                    if (!invoice.isDrafted()) {
                        orderHistoryAdapter.showShimmer = false;
                        if (invoice.getDebtorId().equals(debtor.getDebtorId()))
                            invoiceList.add(invoice);
                    }
                    tvOrderTotal.setText(invoiceList.size() + " đơn hàng");
                    orderHistoryAdapter.notifyDataSetChanged();
                }

            }

        };
        orderHistoryDAO.getInvoiceList(iInvoice, recyclerViewListProduct, layoutNotFound);
        orderHistoryAdapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                sendInvoiceToDetail(position);
            }
        });
    }

    private void sendInvoiceToDetail(int position) {
        if (!invoiceList.isEmpty()) {
            Invoice invoice = invoiceList.get(position);
            Intent intent = new Intent(activity, InvoiceDetailActivity.class);
            intent.putExtra(OrderHistoryActivity.INVOICE, invoice);
            activity.startActivity(intent);
        }
    }


}
