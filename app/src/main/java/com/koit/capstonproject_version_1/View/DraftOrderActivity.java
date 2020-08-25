package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.OrderHistoryController;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class DraftOrderActivity extends AppCompatActivity {


    private RecyclerView rvDraftOrder;
    private TextView tvDraftOrderCount, tvTime;
    private Spinner timeSpinner;
    private ConstraintLayout layoutNotFound;

    private OrderHistoryController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_draft_order);
        initView();

        buildSpinner();
        controller.setupRecyclerView(rvDraftOrder, tvDraftOrderCount);
        controller.draftSpinnerEvent(rvDraftOrder, tvDraftOrderCount, timeSpinner, tvTime, layoutNotFound);
    }

    private void initView() {
        rvDraftOrder = findViewById(R.id.rvDraftOrder);
        tvDraftOrderCount = findViewById(R.id.tvDraftOrderCount);
        timeSpinner = findViewById(R.id.timeSpinner);
        tvTime = findViewById(R.id.tvTime);
        layoutNotFound = findViewById(R.id.layout_not_found_item);

        controller = new OrderHistoryController(this);

        tvTime.setText("");

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Hoá đơn tạm");
    }

    private void buildRvDraftOrder() {
//        controller.draftOrderList(rvDraftOrder, tvDraftOrderCount, tvTime, layoutNotFound);
    }

    private void buildSpinner() {
        String[] timeList = {"Tất cả", "Hôm nay", "Hôm qua", "Hôm kia"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpinner.setAdapter(timeAdapter);
    }

    public void back(View view) {
        back();
    }

    public void back() {
        boolean backToMain = true;
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        if (args != null) {
            boolean isFromSelect = false;
            isFromSelect = (boolean) args.get("fromSelect");
            if (isFromSelect) {
                Intent intent2 = new Intent(DraftOrderActivity.this, SelectProductActivity.class);
                startActivity(intent2);
                backToMain = false;
            }
        }
        if (backToMain) {
            Intent intent2 = new Intent(DraftOrderActivity.this, MainActivity.class);
            startActivity(intent2);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
}