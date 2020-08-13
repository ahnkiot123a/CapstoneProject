package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.InvoiceHistoryController;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;

public class DraftOrderActivity extends AppCompatActivity {

    public static boolean isFirstTimeRun = true;

    private RecyclerView rvDraftOrder;
    private TextView tvDraftOrderCount, tvTime;
    private Spinner timeSpinner;
    private SearchView svDraftOrder;

    private InvoiceHistoryController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_draft_order);

        initView();

        buildRvDraftOrder();
        buildSpinner();

        controller.draftSpinnerEvent(rvDraftOrder, tvDraftOrderCount, timeSpinner, svDraftOrder, tvTime);
        controller.etSearchEvent(svDraftOrder);

        InvoiceHistoryDAO dao = new InvoiceHistoryDAO();
        dao.deleteDraftOrder("HD20200812154810");
    }

    private void initView() {
        rvDraftOrder = findViewById(R.id.rvDraftOrder);
        tvDraftOrderCount = findViewById(R.id.tvDraftOrderCount);
        timeSpinner = findViewById(R.id.timeSpinner);
        tvTime = findViewById(R.id.tvTime);
        svDraftOrder = findViewById(R.id.svDraftOrder);

        controller = new InvoiceHistoryController(this);

        tvTime.setText("");

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Hoá đơn tạm");
    }

        private void buildRvDraftOrder() {
        controller.draftOrderList(rvDraftOrder, tvDraftOrderCount, tvTime);
    }

    private void buildSpinner() {
        String[] timeList = {"Tất cả", "Hôm nay", "Hôm qua", "Hôm kia", "Tuỳ chỉnh"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpinner.setAdapter(timeAdapter);
    }

    public void back(View view) {
        onBackPressed();
    }

}