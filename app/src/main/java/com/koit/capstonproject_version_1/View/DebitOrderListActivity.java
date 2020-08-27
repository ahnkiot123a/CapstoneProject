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

import com.koit.capstonproject_version_1.Controller.DebitOrderController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class DebitOrderListActivity extends AppCompatActivity {

    private TextView tvInvoiceCount;
    private TextView tvTime;
    private RecyclerView rvOrderDebtor;
    private ConstraintLayout layout_not_found_item;
    private Spinner timeSpinner;

    private DebitOrderController debitOrderController;
    private Debtor debtor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_order_debtor);

        initView();
        debtor = getDebtorFromIntent();

        debitOrderController = new DebitOrderController(this);
        setRvOrderDebtor();

        buildSpinner();

        debitOrderListByDate();



    }

    private void debitOrderListByDate() {
        debitOrderController.orderSpinnerEvent(rvOrderDebtor, tvInvoiceCount, timeSpinner, tvTime, layout_not_found_item, debtor);
    }

    private void buildSpinner() {
        String[] timeList = {"Tất cả", "Tuỳ chỉnh"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpinner.setAdapter(timeAdapter);
    }

    private Debtor getDebtorFromIntent() {
        Debtor debtor;
        Intent intent = getIntent();
        debtor = (Debtor) intent.getSerializableExtra(DebitOfDebtorActivity.ITEM_DEBTOR);
        return debtor;
    }

    private void setRvOrderDebtor() {
        debitOrderController.debitOrderList(debtor, rvOrderDebtor, tvInvoiceCount, layout_not_found_item, tvTime);
    }

    private void initView() {
        tvInvoiceCount = findViewById(R.id.tvInvoiceCount);
        tvTime = findViewById(R.id.tvTime);
        rvOrderDebtor = findViewById(R.id.rvOrderDebtor);
        layout_not_found_item = findViewById(R.id.layout_not_found_item);
        timeSpinner = findViewById(R.id.timeSpinner);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Đơn hàng nợ");

    }

    public void back(View view){
        onBackPressed();
    }

    public void chooseTimeFrom(View view) {
    }

    public void chooseTimeTo(View view) {
    }
}