package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;

import com.koit.capstonproject_version_1.Controller.Interface.IDebtPayment;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;

import java.util.ArrayList;
import java.util.List;

public class DebtPaymentController {
    private Activity activity;
    private DebtPayment debtPayment;
    public List<DebtPayment> debtPaymentList;
    public DebtPaymentController() {
    }

    public DebtPaymentController(Activity activity) {
        this.activity = activity;
        debtPayment = new DebtPayment();
        debtPaymentList = new ArrayList<>();
    }
    public  void getListDebtPaymentByDebtor(String debtorId){
        IDebtPayment iDebtPayment = new IDebtPayment() {
            @Override
            public void getDebtPayment(DebtPayment debtPayment) {
                if (debtPayment != null)
                {
                    debtPaymentList.add(debtPayment);
                    Log.d("TotalPayAmountByDebtor", Money.getInstance().formatVN(getTotalPayAmountByDebtor()));
                }
            }
        };
        debtPayment.getListDebtPaymentByDebtor(iDebtPayment,debtorId);
    }

    public long getTotalPayAmountByDebtor(){
        long total = 0;
        for (DebtPayment d : debtPaymentList) total += d.getPayAmount();
        return total;
    }


}
