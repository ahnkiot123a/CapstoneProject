package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.DebtPaymentAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPayment;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;

import java.util.ArrayList;
import java.util.List;

public class DebtPaymentController {
    private Activity activity;
    private DebtPayment debtPayment;
    public List<DebtPayment> debtPaymentList = new ArrayList<>();
    private DebtPaymentAdapter debtPaymentAdapter;

    public DebtPaymentController() {
    }

    public DebtPaymentController(Activity activity) {
        this.activity = activity;
        debtPayment = new DebtPayment();
    }

    public void getListDebtPaymentByDebtor(String debtorId) {
        IDebtPayment iDebtPayment = new IDebtPayment() {
            @Override
            public void getDebtPayment(DebtPayment debtPayment) {
                if (debtPayment != null) {
                    debtPaymentList.add(debtPayment);
                    Log.d("TotalPayAmountByDebtor", Money.getInstance().formatVN(getTotalPayAmountByDebtor()));
                }
            }
        };
        debtPayment.getListDebtPaymentByDebtor(iDebtPayment, debtorId);
    }

    public long getTotalPayAmountByDebtor() {
        long total = 0;
        for (DebtPayment d : debtPaymentList) total += d.getPayAmount();
        return total;
    }

    public void setDebtPaymentList(Debtor debtor, RecyclerView rvDebtPayment) {
        final List<DebtPayment> list = new ArrayList<>();
        if (debtor != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            rvDebtPayment.setLayoutManager(layoutManager);
            debtPaymentAdapter = new DebtPaymentAdapter(list);
            IDebtPayment iDebtPayment = new IDebtPayment() {
                @Override
                public void getDebtPayment(DebtPayment debtPayment) {
                    if (debtPayment != null) {
                        list.add(debtPayment);
                        Log.d("TotalPayAmountByDebtor", Money.getInstance().formatVN(getTotalPayAmountByDebtor()));
                    }
                }
            };
            debtPayment.getListDebtPaymentByDebtor(iDebtPayment, debtor.getDebtorId());
            debtPaymentAdapter.notifyDataSetChanged();
        }
    }

}
