package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.InvoiceHistoryAdapter;
import com.koit.capstonproject_version_1.Controller.InvoiceHistoryController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class InvoiceHistoryActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView rvInvoiceHistory;
    private TextView tvInvoiceCount;

    private InvoiceHistoryController invoiceHistoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);

        initView();

        buildRvInvoiceHistory();


        searchView = findViewById(R.id.svOrder);
        searchView.clearFocus();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private void initView() {
        rvInvoiceHistory = findViewById(R.id.rvInvoiceHistory);
        tvInvoiceCount = findViewById(R.id.tvInvoiceCount);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Lịch sử đơn hàng");
    }

    private void buildRvInvoiceHistory() {
        invoiceHistoryController = new InvoiceHistoryController(this);
        invoiceHistoryController.getInvoiceList(null, rvInvoiceHistory, tvInvoiceCount);
    }

    public void back(View view){
        onBackPressed();
    }


}