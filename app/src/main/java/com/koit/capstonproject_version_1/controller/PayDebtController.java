package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.koit.capstonproject_version_1.controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.model.DebtPayment;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.view.DebitOfDebtorActivity;
import com.koit.capstonproject_version_1.model.dao.OrderHistoryDAO;

import java.util.ArrayList;

public class PayDebtController {
    private Activity activity;
    public Debtor debtor;
    public static final String ITEM_DEBTOR = "ITEM_DEBTOR";

    private OrderHistoryDAO invoiceHistoryDAO;
    private long payAmount;
    //    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<Invoice> invoiceList;

    public PayDebtController() {
    }

    public PayDebtController(Activity activity, Debtor debtor, long payAmount) {
        this.activity = activity;
        this.debtor = debtor;
        this.payAmount = payAmount;
        invoiceHistoryDAO = new OrderHistoryDAO();
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    public long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(long payAmount) {
        this.payAmount = payAmount;
    }

    public void payDebt() {
        invoiceList = new ArrayList<>();
        String debitPaymentId = RandomStringController.getInstance().randomDebtPaymentId();
        String payDate = TimeController.getInstance().getCurrentDate();
        String payTime = TimeController.getInstance().getCurrentTime();
        long debtBeforePay = debtor.getRemainingDebit();
        final DebtPayment debtPayment = new DebtPayment(debitPaymentId, debtor.getDebtorId(), payAmount, payDate, payTime, debtBeforePay);
        debtPayment.addDebtPaymentToFirebase(debtPayment);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null) {
                    if (!invoice.isDrafted()) {
                        if (invoice.getDebtorId().equals(debtor.getDebtorId())) {
                            invoiceList.add(invoice);
                            long debitAmount = invoice.getDebitAmount();
                            long remainingDebit = debtor.getRemainingDebit();
                            if (payAmount > 0) {
                                if (payAmount > debitAmount) {
                                    payAmount = payAmount - invoice.getDebitAmount();
                                    long payMoney = invoice.getDebitAmount();
                                    remainingDebit = remainingDebit - invoice.getDebitAmount();
                                    invoice.setDebitAmount(0);
                                    invoice.setPayMoney(payMoney);
                                    if (remainingDebit < 0)remainingDebit = 0;
                                    debtor.setRemainingDebit(remainingDebit);
                                } else {
                                    if (debitAmount - payAmount < 0) invoice.setDebitAmount(0);
                                    invoice.setDebitAmount(debitAmount - payAmount);
                                    remainingDebit = remainingDebit - payAmount;
                                    long payMoney = payAmount;
                                    payAmount = 0;
                                    invoice.setPayMoney(payMoney);
                                    if (remainingDebit < 0)remainingDebit = 0;
                                    debtor.setRemainingDebit(remainingDebit);
                                }
                                debtPayment.addInvoiceToDebtPaymentFirebase(debtPayment, invoice);
                                Invoice.getInstance().updateDebitAmount(invoice);
                                Debtor.getInstance().updateRemainingDebit(debtor);
                                Intent intent = new Intent(activity, DebitOfDebtorActivity.class);
                                intent.putExtra(ITEM_DEBTOR, debtor);
                                Toast.makeText(activity, "Trừ nợ thành công", Toast.LENGTH_SHORT).show();
                                activity.startActivity(intent);
                                activity.finish();
                            }
                            Log.d("payMoney", invoice.getPayMoney() + "");
                            Log.d("dbtor", debtor.toString());
                            Log.d("ktInvoice", invoice.toString());
                            Log.d("payAmountRemaining", payAmount + "");

                        }

                    }

                }

            }

        };
        invoiceHistoryDAO.getDebitInvoiceList(iInvoice);
    }

}
