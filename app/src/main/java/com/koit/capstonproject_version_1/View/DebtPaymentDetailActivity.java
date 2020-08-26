package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.DebtPaymentDetailAdapter;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class DebtPaymentDetailActivity extends AppCompatActivity {

    public static final String DEBT_PAYMENT_DETAIL = "DEBT_PAYMENT_DETAIL";

    private TextView tvDateTime, tvDebtTotal, tvChangeMoney, tvRemainingDebt;
    private RecyclerView rvInvoicePayment;

    private DebtPayment debtPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_debt_payment_detail);

        initView();

        debtPayment = getDebtPayment();

        setInfoDebtPayDetail();

    }

    private void setInfoDebtPayDetail() {
        tvDateTime.setText(debtPayment.getPayTime() + "   " + debtPayment.getPayDate());
        long changeMoney = debtPayment.getPayAmount();
        long debtBeforePay = debtPayment.getDebtBeforePay();
        long debtAfterPay = debtBeforePay - changeMoney;

        tvDebtTotal.setText(Money.getInstance().formatVN(debtBeforePay) + " đ");
        tvChangeMoney.setText(Money.getInstance().formatVN(changeMoney) + " đ");
        tvRemainingDebt.setText(Money.getInstance().formatVN(debtAfterPay) + " đ");
        setRvInvoicePay();
    }

    private void setRvInvoicePay() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvInvoicePayment.setLayoutManager(layoutManager);
        DebtPaymentDetailAdapter adapter = new DebtPaymentDetailAdapter(debtPayment.getInvoices());
        rvInvoicePayment.setAdapter(adapter);
    }

    private DebtPayment getDebtPayment() {
        Intent intent = getIntent();
        return (DebtPayment) intent.getSerializableExtra(DEBT_PAYMENT_DETAIL);
    }

    private void initView() {

        tvDateTime = findViewById(R.id.tvDateTime);
        tvDebtTotal = findViewById(R.id.tvDebtTotal);
        tvChangeMoney = findViewById(R.id.tvChangeMoney);
        tvRemainingDebt = findViewById(R.id.tvRemainingDebt);
        rvInvoicePayment = findViewById(R.id.rvInvoicePayment);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Chi tiết trả nợ");
    }

    public void back(View view) {
        onBackPressed();
    }
}