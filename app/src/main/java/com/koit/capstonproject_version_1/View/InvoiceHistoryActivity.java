package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.InvoiceHistoryAdapter;
import com.koit.capstonproject_version_1.Controller.InvoiceHistoryController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class InvoiceHistoryActivity extends AppCompatActivity {

    private RecyclerView rvInvoiceHistory;
    private TextView tvInvoiceCount;
    private Spinner invoiceStatusSpinner, timeSpinner;
    private EditText etSearch;

    private InvoiceHistoryController invoiceHistoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);

        initView();

        buildRvInvoiceHistory();
        buildSpinner();
        invoiceHistoryController.etSearchEvent(etSearch);

    }



    private void initView() {
        rvInvoiceHistory = findViewById(R.id.rvInvoiceHistory);
        tvInvoiceCount = findViewById(R.id.tvInvoiceCount);
        invoiceStatusSpinner = findViewById(R.id.invoiceStatusSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        etSearch = findViewById(R.id.etSearchField);


        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Lịch sử đơn hàng");
    }



    private void buildRvInvoiceHistory() {
        invoiceHistoryController = new InvoiceHistoryController(this);
        invoiceHistoryController.getInvoiceList(null, rvInvoiceHistory, tvInvoiceCount);
    }

    private void buildSpinner(){
        String[] statusList = {"Tất cả đơn hàng", "Hoá đơn còn nợ", "Hoá đơn trả hết"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusList);
        invoiceStatusSpinner.setAdapter(statusAdapter);

        String[] timeList = {"Thời gian", "Hôm nay", "Tuần này", "Tháng này", "Tuỳ chỉnh"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timeList);
        timeSpinner.setAdapter(timeAdapter);
    }

    public void back(View view){
        onBackPressed();
    }


}