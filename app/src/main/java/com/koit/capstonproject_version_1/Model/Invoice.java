package com.koit.capstonproject_version_1.Model;

public class Invoice {
    private String invoiceId, customerId, invoiceDate, invoiceTime;
    private long discount, paidAmount;
    private boolean isDebt, isPaid;

    public Invoice() {
    }

    public Invoice(String invoiceId, String customerId, String invoiceDate, String invoiceTime, long discount, long paidAmount, boolean isDebt, boolean isPaid) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.invoiceTime = invoiceTime;
        this.discount = discount;
        this.paidAmount = paidAmount;
        this.isDebt = isDebt;
        this.isPaid = isPaid;
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

    public long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(long paidAmount) {
        this.paidAmount = paidAmount;
    }

    public boolean isDebt() {
        return isDebt;
    }

    public void setDebt(boolean debt) {
        isDebt = debt;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", invoiceTime='" + invoiceTime + '\'' +
                ", discount=" + discount +
                ", paidAmount=" + paidAmount +
                ", isDebt=" + isDebt +
                ", isPaid=" + isPaid +
                '}';
    }
}
