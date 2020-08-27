package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.DebtPaymentAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPayment;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;

import java.util.ArrayList;
import java.util.List;

public class DebitOfDebtorController {
    private Activity activity;
    private DebtPayment debtPayment;
    public List<DebtPayment> debtPaymentList = new ArrayList<>();
    private DebtPaymentAdapter debtPaymentAdapter;

    public DebitOfDebtorController() {
    }

    public DebitOfDebtorController(Activity activity) {
        this.activity = activity;
        debtPayment = new DebtPayment();
    }

//    public void getListDebtPaymentByDebtor(String debtorId) {
//        IDebtPayment iDebtPayment = new IDebtPayment() {
//            @Override
//            public void getDebtPayment(DebtPayment debtPayment) {
//                if (debtPayment != null) {
//                    debtPaymentList.add(debtPayment);
//                    Log.d("TotalPayAmountByDebtor", Money.getInstance().formatVN(getTotalPayAmountByDebtor()));
//                }
//            }
//        };
//        debtPayment.getListDebtPaymentByDebtor(iDebtPayment, debtorId);
//    }

    public long getTotalPayAmountByDebtor(List<DebtPayment> list) {
        long total = 0;
        for (DebtPayment d : list) total += d.getPayAmount();
        return total;
    }

    public void setDebtPaymentList(Debtor debtor, RecyclerView rvDebtPayment, final TextView tvPayAmountTotal,
                                   final TextView tvDebtTotal, final TextView tvDebtAmountTotal, final ProgressBar pbDebt) {
        final List<DebtPayment> list = new ArrayList<>();
        if (debtor != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            rvDebtPayment.setLayoutManager(layoutManager);
            debtPaymentAdapter = new DebtPaymentAdapter(list, activity);
            IDebtPayment iDebtPayment = new IDebtPayment() {
                @Override
                public void getDebtPayment(DebtPayment debtPayment) {
                    list.add(debtPayment);
                    debtPaymentAdapter.notifyDataSetChanged();
                    long debtTotal = Money.getInstance().reFormatVND(tvDebtAmountTotal.getText().toString());
                    long payAmountToTal = getTotalPayAmountByDebtor(list);
                    long debtAmountTotal = debtTotal + payAmountToTal;
                    pbDebt.setMax((int) debtAmountTotal);
                    pbDebt.setProgress((int) payAmountToTal);
                    tvPayAmountTotal.setText(Money.getInstance().formatVN(payAmountToTal) + " đ");
                    tvDebtTotal.setText(Money.getInstance().formatVN(debtAmountTotal) + " đ");
                    Log.d("TotalPayAmountByDebtor", Money.getInstance().formatVN(getTotalPayAmountByDebtor(list)));
                }
            };
            Log.d("qlandbac", list.toString());
            debtPayment.getListDebtPaymentByDebtor(iDebtPayment, debtor.getDebtorId());
            rvDebtPayment.setAdapter(debtPaymentAdapter);
        }
    }


    public void checkDebtMoneyAndButtonView(Debtor currentDebtor, Button btnPayment) {
        if (currentDebtor.getRemainingDebit() > 0) {
            btnPayment.setVisibility(View.VISIBLE);
        } else {
            btnPayment.setVisibility(View.GONE);
        }
    }
}
