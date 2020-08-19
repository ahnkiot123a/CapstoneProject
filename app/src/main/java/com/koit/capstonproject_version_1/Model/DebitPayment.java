package com.koit.capstonproject_version_1.Model;

import java.io.Serializable;

public class DebitPayment implements Serializable {
    private String debitPaymentId, debtorId;
    private long debitBeforePay, payAmount;
    private String payDate, payTime;

    public DebitPayment() {
    }

    public DebitPayment(String debitPaymentId, String debtorId, long debitBeforePay, long payAmount, String payDate, String payTime) {
        this.debitPaymentId = debitPaymentId;
        this.debtorId = debtorId;
        this.debitBeforePay = debitBeforePay;
        this.payAmount = payAmount;
        this.payDate = payDate;
        this.payTime = payTime;
    }

    public String getDebitPaymentId() {
        return debitPaymentId;
    }

    public void setDebitPaymentId(String debitPaymentId) {
        this.debitPaymentId = debitPaymentId;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public long getDebitBeforePay() {
        return debitBeforePay;
    }

    public void setDebitBeforePay(long debitBeforePay) {
        this.debitBeforePay = debitBeforePay;
    }

    public long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(long payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
