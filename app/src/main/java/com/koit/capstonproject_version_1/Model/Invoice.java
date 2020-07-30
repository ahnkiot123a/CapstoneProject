package com.koit.capstonproject_version_1.Model;

import java.io.Serializable;

public class Invoice implements Serializable {
    private String invoiceId, customerId, invoiceDate, invoiceTime, customerImage;
    private long debitAmount, discount, firstPaid, total;

    public Invoice() {
    }

    public Invoice(String invoiceId, String customerId, String invoiceDate, String invoiceTime, String customerImage, long debitAmount, long discount, long firstPaid, long total) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.invoiceTime = invoiceTime;
        this.customerImage = customerImage;
        this.debitAmount = debitAmount;
        this.discount = discount;
        this.firstPaid = firstPaid;
        this.total = total;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(String invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public long getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(long debitAmount) {
        this.debitAmount = debitAmount;
    }

    public long getFirstPaid() {
        return firstPaid;
    }

    public void setFirstPaid(long firstPaid) {
        this.firstPaid = firstPaid;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", invoiceTime='" + invoiceTime + '\'' +
                ", customerImage='" + customerImage + '\'' +
                ", debitAmount=" + debitAmount +
                ", discount=" + discount +
                ", firstPaid=" + firstPaid +
                ", total=" + total +
                '}';
    }
}
