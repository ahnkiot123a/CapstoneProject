package com.koit.capstonproject_version_1.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;

public class Invoice implements Serializable {
    private String invoiceId, debtorId, invoiceDate, invoiceTime, debtorImage, debtorName;
    private long debitAmount, discount, firstPaid, total;
    private boolean isDrafted;
    private long payMoney;
    private static Invoice mInstance;

    public static Invoice getInstance() {
        if (mInstance == null) {
            mInstance = new Invoice();
        }
        return mInstance;
    }

    public Invoice() {

    }

    public Invoice(String invoiceId, long debitAmount, long payMoney) {
        this.invoiceId = invoiceId;
        this.debitAmount = debitAmount;
        this.payMoney = payMoney;
    }
    public Invoice(String invoiceId, long total, long payMoney, String invoiceDate) {
        this.invoiceId = invoiceId;
        this.total = total;
        this.payMoney = payMoney;
        this.invoiceDate = invoiceDate;
    }

    public Invoice(String invoiceId, String debtorId, String invoiceDate, String invoiceTime, String debtorImage, long debitAmount, long discount, long firstPaid, long total, boolean isDrafted) {
        this.invoiceId = invoiceId;
        this.debtorId = debtorId;
        this.invoiceDate = invoiceDate;
        this.invoiceTime = invoiceTime;
        this.debtorImage = debtorImage;
        this.debitAmount = debitAmount;
        this.discount = discount;
        this.firstPaid = firstPaid;
        this.total = total;
        this.isDrafted = isDrafted;
    }

    public Invoice(String invoiceId, String debtorId, String invoiceDate, String invoiceTime, String debtorImage, String debtorName, long debitAmount, long discount, long firstPaid, long total, boolean isDrafted) {
        this.invoiceId = invoiceId;
        this.debtorId = debtorId;
        this.invoiceDate = invoiceDate;
        this.invoiceTime = invoiceTime;
        this.debtorImage = debtorImage;
        this.debtorName = debtorName;
        this.debitAmount = debitAmount;
        this.discount = discount;
        this.firstPaid = firstPaid;
        this.total = total;
        this.isDrafted = isDrafted;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getDebtorImage() {
        return debtorImage;
    }

    public void setDebtorImage(String debtorImage) {
        this.debtorImage = debtorImage;
    }

    public boolean isDrafted() {
        return isDrafted;
    }

    public void setDrafted(boolean drafted) {
        this.isDrafted = drafted;
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

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", debtorId='" + debtorId + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", invoiceTime='" + invoiceTime + '\'' +
                ", debtorImage='" + debtorImage + '\'' +
                ", debtorName='" + debtorName + '\'' +
                ", debitAmount=" + debitAmount +
                ", discount=" + discount +
                ", firstPaid=" + firstPaid +
                ", total=" + total +
                ", isDrafted=" + isDrafted +
                ", payMoney=" + payMoney +
                '}';
    }

    public void addInvoiceToFirebase(Invoice invoice) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (invoice.getInvoiceId() != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference = databaseReference.child("Invoices").child(UserDAO.getInstance().getUserID()).
                    child(invoice.getInvoiceId());
            invoice.setInvoiceId(null);
            databaseReference.setValue(invoice);
            databaseReference.keepSynced(true);
        }

    }

    public void updateDebitAmount(Invoice invoice) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("Invoices").child(UserDAO.getInstance().getUserID()).
                child(invoice.getInvoiceId()).child("debitAmount");
        databaseReference.setValue(invoice.getDebitAmount());
        databaseReference.keepSynced(true);
    }
}
