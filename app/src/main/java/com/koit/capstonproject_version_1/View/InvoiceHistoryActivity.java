package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.InvoiceHistoryController;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.R;

public class InvoiceHistoryActivity extends AppCompatActivity {

    public static boolean isFirstTimeRun = true;
    private RecyclerView rvInvoiceHistory;
    private TextView tvInvoiceCount, tvTime;
    private Spinner invoiceStatusSpinner, timeSpinner;
    private EditText etSearch;



    private InvoiceHistoryController invoiceHistoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);

        initView();

//        buildRvInvoiceHistory();
        buildSpinner();

        invoiceHistoryController.spinnerEvent(rvInvoiceHistory,tvInvoiceCount, timeSpinner, invoiceStatusSpinner, etSearch);

        invoiceHistoryController.etSearchEvent(etSearch);


    }


    private void initView() {
        rvInvoiceHistory = findViewById(R.id.rvInvoiceHistory);
        tvInvoiceCount = findViewById(R.id.tvInvoiceCount);
        invoiceStatusSpinner = findViewById(R.id.invoiceStatusSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        etSearch = findViewById(R.id.etSearchField);
        tvTime = findViewById(R.id.tvTime);


        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Lịch sử đơn hàng");

        //set current date
        tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());

        invoiceHistoryController = new InvoiceHistoryController(this);

        InvoiceHistoryActivity.isFirstTimeRun = true;
    }


    private void buildRvInvoiceHistory() {
        invoiceHistoryController = new InvoiceHistoryController(this);
        invoiceHistoryController.invoiceList(rvInvoiceHistory, tvInvoiceCount);
    }

    private void buildSpinner() {
        String[] statusList = {"Tất cả đơn hàng", "Hoá đơn còn nợ", "Hoá đơn trả hết"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        invoiceStatusSpinner.setAdapter(statusAdapter);

        String[] timeList = {"Thời gian", "Hôm nay", "7 ngày trước", "30 ngày trước", "Tuỳ chỉnh"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpinner.setAdapter(timeAdapter);
    }



    public void back(View view) {
        onBackPressed();
    }


}