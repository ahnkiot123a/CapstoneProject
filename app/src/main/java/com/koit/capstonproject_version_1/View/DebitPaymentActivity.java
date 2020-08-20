package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.DebtPaymentController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class DebitPaymentActivity extends AppCompatActivity {

    public static final String ITEM_DEBTOR = "ITEM_DEBTOR";
    private RecyclerView rvDebtPaymentHistory;
    private TextView tvDebtTotal, tvPayAmountTotal, tvDebtAmountTotal, tvDebtorName, tvDebtorPhone, tvDebtorAddress;
    private Debtor currentDebtor;

    private DebtPaymentController debtPaymentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_debit_payment);

        initView();
        currentDebtor = getCurrentDebtor();

        setInformationDebtor();
        setDebtPaymentList();


    }

    private void setInformationDebtor() {
        if(currentDebtor != null){
            tvDebtorName.setText(currentDebtor.getFullName());
            tvDebtorPhone.setText(currentDebtor.getPhoneNumber());
            tvDebtorAddress.setText(currentDebtor.getAddress());
        }
    }

    private void setDebtPaymentList() {
        debtPaymentController = new DebtPaymentController(this);
        debtPaymentController.setDebtPaymentList(currentDebtor, rvDebtPaymentHistory);
    }

    private Debtor getCurrentDebtor() {
        Debtor debtor;
        Intent intent = getIntent();
        debtor = (Debtor) intent.getSerializableExtra(ITEM_DEBTOR);
        return debtor;
    }

    private void initView() {
        rvDebtPaymentHistory = findViewById(R.id.rvDebtPaymentHistory);
        tvDebtTotal = findViewById(R.id.tvDebtTotal);
        tvPayAmountTotal = findViewById(R.id.tvPayAmountTotal);
        tvDebtAmountTotal = findViewById(R.id.tvDebtAmountTotal);
        tvDebtorName = findViewById(R.id.tvDebtorName);
        tvDebtorPhone = findViewById(R.id.tvDebtorPhone);
        tvDebtorAddress = findViewById(R.id.tvDebtorAddress);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Thông tin nợ");
    }

    public void back(View view){
        onBackPressed();
    }
}