package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koit.capstonproject_version_1.Controller.OrderHistoryController;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.R;

public class OrderHistoryActivity extends AppCompatActivity {

    public final static String INVOICE = "INVOICE";

    public static boolean isFirstTimeRun = true;
    private RecyclerView rvInvoiceHistory;
    private TextView tvInvoiceCount, tvTime;
    private Spinner invoiceStatusSpinner, timeSpinner;
    private SearchView svInvoice;
    private SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayout refreshLayoutNotFound;
    private LinearLayout layoutOrderHistory;


    private ConstraintLayout layoutNotFound;
    private OrderHistoryController orderHistoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);

        initView();

//        buildRvInvoiceHistory();
        buildSpinner();

        orderHistoryController.invoiceSpinnerEvent(rvInvoiceHistory,tvInvoiceCount, timeSpinner,
                invoiceStatusSpinner, svInvoice, tvTime, layoutNotFound, refreshLayout,layoutOrderHistory, refreshLayoutNotFound);

        orderHistoryController.etSearchEvent(svInvoice);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        rvInvoiceHistory = findViewById(R.id.rvInvoiceHistory);
        tvInvoiceCount = findViewById(R.id.tvInvoiceCount);
        invoiceStatusSpinner = findViewById(R.id.invoiceStatusSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        svInvoice = findViewById(R.id.svInvoice);
        layoutNotFound = findViewById(R.id.layout_not_found_item);
        tvTime = findViewById(R.id.tvTime);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayoutNotFound = findViewById(R.id.refreshLayoutNotFound);
        layoutOrderHistory = findViewById(R.id.layoutOrderHistory);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Lịch sử đơn hàng");

        //set current date
        tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());

        orderHistoryController = new OrderHistoryController(this);
        OrderHistoryActivity.isFirstTimeRun = true;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callCreateOrder(View view) {
        Intent intent = new Intent(this, SelectProductActivity.class);
        startActivity(intent);
    }
}