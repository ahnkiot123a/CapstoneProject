package com.koit.capstonproject_version_1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koit.capstonproject_version_1.Controller.PayDebtController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.UIModel.MyDialog;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.MoneyEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InputPayDebtMoneyActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 11;

    private TextView tvDebtTotal, tvRemainingDebt, tvChangeMoney;
    private MoneyEditText etPayAmount;
    private LinearLayout layoutChangeMoney;
    private long inputMoney;
    private long remainingDebt;
    private long changeMoney;
    private FloatingActionButton btnConfirm;
    private PayDebtController payDebtController;
    public static final String ITEM_DEBTOR = "ITEM_DEBTOR";
    private Debtor debtor;
    private LinearLayout linearInputPayDebt;
    private ConstraintLayout constraintPayAllDebt;
    private Disposable internetDisposable;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_input_pay_debt_money);

        debtor = getCurrentDebtor();
        initView();

        setDebtMoneyView();
        setEtPayAmountEvent();
        dialog = new MyDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                            if (isConnected) {
                                if (dialog != null)
                                    dialog.cancelConnectionDialog();
                            } else {
                                dialog.showInternetError();
                            }
                        }

                );
    }

    @Override
    protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    private void setEtPayAmountEvent() {
        etPayAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sMoney = s.toString();
                CharSequence sq = ".";
                Log.d("sMoney", sMoney);
                if (sMoney.equals(sq)) {
                    etPayAmount.setText(sMoney.substring(0, sMoney.length() - 1));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTextView(s);
            }
        });
    }


    private void setTextView(CharSequence s) {
        if (!s.toString().isEmpty()) {
            inputMoney = Money.getInstance().reFormatVN(s.toString());
            long debtMoney = debtor.getRemainingDebit() - inputMoney;
            remainingDebt = debtMoney > 0 ? debtMoney : 0;
            changeMoney = debtMoney < 0 ? -debtMoney : 0;
            tvRemainingDebt.setText(Money.getInstance().formatVN(remainingDebt) + " đ");
            tvChangeMoney.setText(Money.getInstance().formatVN(changeMoney) + " đ");
        } else {
            tvRemainingDebt.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
            tvChangeMoney.setText(Money.getInstance().formatVN(0) + " đ");
        }
    }

    private void setDebtMoneyView() {
//        tvDebtTotal.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
//        tvRemainingDebt.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
        Debtor.getInstance().setDebtMoneyView(debtor.getDebtorId(), debtor, tvDebtTotal, tvRemainingDebt,
                constraintPayAllDebt, linearInputPayDebt, btnConfirm);
    }

    private void initView() {
        tvDebtTotal = findViewById(R.id.tvDebtTotal);
        tvRemainingDebt = findViewById(R.id.tvRemainingDebt);
        tvChangeMoney = findViewById(R.id.tvChangeMoney);
        etPayAmount = findViewById(R.id.etPayAmount);
        layoutChangeMoney = findViewById(R.id.layoutChangeMoney);
        btnConfirm = findViewById(R.id.btnConfirm);
        linearInputPayDebt = findViewById(R.id.linearInputPayDebt);
        constraintPayAllDebt = findViewById(R.id.constraintPayAllDebt);

        //set title in toolbar
        Toolbar toolbar = findViewById(R.id.toolbarGeneral);
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText(R.string.InputDebtMoney);
    }

    private Debtor getCurrentDebtor() {
        Debtor debtor;
        Intent intent = getIntent();
        debtor = (Debtor) intent.getSerializableExtra(DebitOfDebtorActivity.ITEM_DEBTOR);
        return debtor;
    }

    public void paydebtMoney(View view) {
        long payAmount = 0;
        try {
            payAmount = Money.getInstance().reFormatVN(etPayAmount.getText().toString());
        } catch (Exception e) {
            payAmount = 0;
            Log.d("payAmount", e.toString());
        }
        if (payAmount > debtor.getRemainingDebit()) payAmount = debtor.getRemainingDebit();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        if (payAmount > 0) {
            final long finalPayAmount = payAmount;
            builder.setMessage("Bạn có chắc chắn trừ " + Money.getInstance().formatVN(payAmount)
                    + " đ vào nợ của " + debtor.getFullName() + " không?")
                    .setCancelable(true)
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            payDebtController = new PayDebtController(InputPayDebtMoneyActivity.this, debtor, finalPayAmount);
                            payDebtController.payDebt();
                        }
                    })
                    .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme));
                    alert.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                }
            });
            alert.show();
        } else {
            builder.setMessage("Mời bạn nhập số tiền khách trả!")
                    .setCancelable(true)
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            final androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme));
                }
            });
            alert.show();
        }


    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DebitOfDebtorActivity.class);
        intent.putExtra(ITEM_DEBTOR, debtor);
        startActivity(intent);
        finish();
    }
}