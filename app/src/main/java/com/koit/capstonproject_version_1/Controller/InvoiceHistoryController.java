package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.koit.capstonproject_version_1.Adapter.InvoiceHistoryAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;

import java.util.ArrayList;

public class InvoiceHistoryController {
    private Activity activity;
    private InvoiceHistoryDAO invoiceHistoryDAO;
    private InvoiceHistoryAdapter invoiceHistoryAdapter;
    private ArrayList<Invoice> invoiceList;
    private RecyclerView recyclerView;

    public InvoiceHistoryController(Activity activity) {
        this.activity = activity;
        invoiceHistoryDAO = new InvoiceHistoryDAO();
    }

    public void getInvoiceList(String searchText, RecyclerView recyclerViewListProduct, final TextView textView) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        invoiceHistoryAdapter = new InvoiceHistoryAdapter(invoiceList,activity.getApplicationContext());
        recyclerViewListProduct.setAdapter(invoiceHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if(invoice != null){
                    invoiceHistoryAdapter.showShimmer = false;
                    invoiceList.add(invoice);
                    textView.setText(invoiceList.size() + " đơn hàng");
                    invoiceHistoryAdapter.notifyDataSetChanged();
                }

            }

        };
        invoiceHistoryDAO.getInvoiceList(iInvoice);
    }


}
