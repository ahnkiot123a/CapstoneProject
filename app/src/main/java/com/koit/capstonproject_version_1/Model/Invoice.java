package com.koit.capstonproject_version_1.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;

public class Invoice implements Serializable {
    private String invoiceId, debtorId, debtorName, invoiceDate, invoiceTime, debtorImage;
    private long debitAmount, discount, firstPaid, total;
    private boolean isDrafted;

    public Invoice() {
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

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDebtorName() {
        return debtorName;
    }

//    public void setDebtorName() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Debtors").child(UserDAO.getInstance().getUserID()).child(debtorId);
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Customer customer
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addListenerForSingleValueEvent(valueEventListener);
//    }

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
        isDrafted = drafted;
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
                ", debtorId='" + debtorId + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", invoiceTime='" + invoiceTime + '\'' +
                ", debtorImage='" + debtorImage + '\'' +
                ", debitAmount=" + debitAmount +
                ", discount=" + discount +
                ", firstPaid=" + firstPaid +
                ", total=" + total +
                ", isDrafted=" + isDrafted +
                '}';
    }
}
