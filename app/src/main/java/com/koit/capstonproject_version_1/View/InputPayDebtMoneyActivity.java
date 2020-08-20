package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

public class InputPayDebtMoneyActivity extends AppCompatActivity {

    private TextView tvDebtTotal, tvRemainingDebt, tvChangeMoney;
    private EditText etPayAmount;
    private LinearLayout layoutChangeMoney;
    private long inputMoney;
    private long remainingDebt;
    private long changeMoney;
    private FloatingActionButton btnConfirm;

    private Debtor debtor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_input_pay_debt_money);

        debtor = getCurrentDebtor();
        initView();

        setDebtMoneyView();

        setEtPayAmountEvent();

    }

    private void setEtPayAmountEvent() {
        etPayAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTextView(s);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void setTextView(CharSequence s) {
        if (!s.toString().isEmpty()) {
            inputMoney = Long.parseLong(s.toString());
            long debtMoney = debtor.getRemainingDebit() - inputMoney;
            remainingDebt = debtMoney > 0 ? debtMoney : 0;
            changeMoney = debtMoney < 0 ? -debtMoney : 0;
            tvRemainingDebt.setText(Money.getInstance().formatVN(remainingDebt) + " đ");
            tvChangeMoney.setText(Money.getInstance().formatVN(changeMoney) + " đ");
        }else{
            tvRemainingDebt.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
            tvChangeMoney.setText(Money.getInstance().formatVN(0) + " đ");
        }
    }

    private void setDebtMoneyView() {
        tvDebtTotal.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
        tvRemainingDebt.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
    }

    private void initView() {
        tvDebtTotal = findViewById(R.id.tvDebtTotal);
        tvRemainingDebt = findViewById(R.id.tvRemainingDebt);
        tvChangeMoney = findViewById(R.id.tvChangeMoney);
        etPayAmount = findViewById(R.id.etPayAmount);
        layoutChangeMoney = findViewById(R.id.layoutChangeMoney);
        btnConfirm = findViewById(R.id.btnConfirm);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(R.string.InputDebtMoney);
    }

    private Debtor getCurrentDebtor() {
        Debtor debtor;
        Intent intent = getIntent();
        debtor = (Debtor) intent.getSerializableExtra(DebitPaymentActivity.ITEM_DEBTOR);
        return debtor;
    }
}