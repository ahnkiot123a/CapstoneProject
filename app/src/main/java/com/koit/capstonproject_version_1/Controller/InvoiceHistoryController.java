package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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

    public InvoiceHistoryController(Activity activity) {
        this.activity = activity;
        invoiceHistoryDAO = new InvoiceHistoryDAO();
    }

    public void getInvoiceList(String searchText, RecyclerView recyclerViewListProduct, final TextView textView) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        invoiceHistoryAdapter = new InvoiceHistoryAdapter(invoiceList,activity.getApplicationContext(), textView);
        recyclerViewListProduct.setAdapter(invoiceHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if(invoice != null){
                    invoiceHistoryAdapter.showShimmer = false;
                    invoiceList.add(invoice);
                    invoiceHistoryAdapter.notifyDataSetChanged();
                }

            }

        };
        invoiceHistoryDAO.getInvoiceList(iInvoice);
    }

    public void etSearchEvent(EditText etSearch) {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                invoiceHistoryAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void searchByStatus(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
