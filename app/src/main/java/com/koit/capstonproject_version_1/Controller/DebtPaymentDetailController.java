package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Controller.Interface.IDebtPayment;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPaymentDetail;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.DebtPaymentDetail;
import com.koit.capstonproject_version_1.Model.UIModel.Money;

import java.util.ArrayList;
import java.util.List;

public class DebtPaymentDetailController {
    private Activity activity;
    private DebtPaymentDetail debtPaymentDetail;
    public List<DebtPaymentDetail> debtPaymentDetailList;

    public DebtPaymentDetailController() {
    }

    public DebtPaymentDetailController(Activity activity) {
        this.activity = activity;
        debtPaymentDetail = new DebtPaymentDetail();
        debtPaymentDetailList = new ArrayList<>();
    }

    public void getListDebtPayments(final TextView tvPaid) {
        IDebtPaymentDetail iDebtPaymentDetail = new IDebtPaymentDetail() {
            @Override
            public void getDebtPaymentDetail(DebtPaymentDetail debtPaymentDetail) {
                if (debtPaymentDetail != null){
                    debtPaymentDetailList.add(debtPaymentDetail);
                    tvPaid.setText(Money.getInstance().formatVN(getTotalPayAmountByAllDebtors()));
//                    Log.d("PayAmountAllDebtors",Money.getInstance().formatVN(getTotalPayAmountByAllDebtors()));
                }
            }
        };
        debtPaymentDetail.getListDebtPayments(iDebtPaymentDetail);
    }

    public long getTotalPayAmountByAllDebtors() {
        long total = 0;
        for (DebtPaymentDetail debtPaymentDetail : debtPaymentDetailList) {
            for (DebtPayment debtPayment : debtPaymentDetail.getDebtPaymentList())
                total += debtPayment.getPayAmount();
        }
        return total;
    }

}
