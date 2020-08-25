package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.OrderDebtorController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class OrderDebtorActivity extends AppCompatActivity {

    private TextView tvInvoiceCount;
    private RecyclerView rvOrderDebtor;
    private ConstraintLayout layout_not_found_item;

    private OrderDebtorController orderDebtorController;
    private Debtor debtor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_order_debtor);

        initView();
        debtor = getDebtorFromIntent();

        orderDebtorController = new OrderDebtorController(this);
        setRvOrderDebtor();


    }

    private Debtor getDebtorFromIntent() {
        Debtor debtor;
        Intent intent = getIntent();
        debtor = (Debtor) intent.getSerializableExtra(DebitPaymentActivity.ITEM_DEBTOR);
        return debtor;
    }

    private void setRvOrderDebtor() {
        orderDebtorController.orderDebtorList(debtor, rvOrderDebtor, tvInvoiceCount, layout_not_found_item);
    }

    private void initView() {
        tvInvoiceCount = findViewById(R.id.tvInvoiceCount);
        rvOrderDebtor = findViewById(R.id.rvOrderDebtor);
        layout_not_found_item = findViewById(R.id.layout_not_found_item);

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