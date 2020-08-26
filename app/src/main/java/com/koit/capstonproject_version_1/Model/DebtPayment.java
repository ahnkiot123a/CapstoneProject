package com.koit.capstonproject_version_1.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPayment;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DebtPayment implements Serializable {
    private String debitPaymentId, debtorId;
    private long payAmount, debtBeforePay;
    private String payDate, payTime;
    private List<Invoice> invoices;

    public DebtPayment() {
    }

    public DebtPayment(String debitPaymentId, String debtorId, long payAmount, String payDate, String payTime, long debtBeforePay) {
        this.debitPaymentId = debitPaymentId;
        this.debtorId = debtorId;
        this.payAmount = payAmount;
        this.payDate = payDate;
        this.payTime = payTime;
        this.debtBeforePay = debtBeforePay;
    }

    public DebtPayment(String debitPaymentId, String debtorId, long payAmount, long debtBeforePay, String payDate, String payTime, List<Invoice> invoices) {
        this.debitPaymentId = debitPaymentId;
        this.debtorId = debtorId;
        this.payAmount = payAmount;
        this.debtBeforePay = debtBeforePay;
        this.payDate = payDate;
        this.payTime = payTime;
        this.invoices = invoices;
    }

    public long getDebtBeforePay() {
        return debtBeforePay;
    }

    public void setDebtBeforePay(long debtBeforePay) {
        this.debtBeforePay = debtBeforePay;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
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

    @Override
    public String toString() {
        return "DebtPayment{" +
                "debitPaymentId='" + debitPaymentId + '\'' +
                ", debtorId='" + debtorId + '\'' +
                ", payAmount=" + payAmount +
                ", debtBeforePay=" + debtBeforePay +
                ", payDate='" + payDate + '\'' +
                ", payTime='" + payTime + '\'' +
                ", invoices=" + invoices +
                '}';
    }

    public void getListDebtPaymentByDebtor(final IDebtPayment iDebtPayment, final String debtorId) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getListDebtPaymentByDebtor(snapshot, iDebtPayment, debtorId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListDebtPaymentByDebtor(DataSnapshot dataSnapshot, IDebtPayment iDebtPayment, String debtorId) {
        DataSnapshot dataSnapshotDebtPayment = dataSnapshot.child("DebtPayments")
                .child(UserDAO.getInstance().getUserID()).child(debtorId);
        if (dataSnapshotDebtPayment != null) {
            for (DataSnapshot valueDebtPayment : dataSnapshotDebtPayment.getChildren()) {
                DebtPayment debtPayment = valueDebtPayment.getValue(DebtPayment.class);
                debtPayment.setDebitPaymentId(valueDebtPayment.getKey());
                debtPayment.setDebtorId(debtorId);
                DataSnapshot dataSnapshotInvoiceDebtPayment = dataSnapshotDebtPayment
                        .child(debtPayment.getDebitPaymentId()).child("invoiceDebtPayments");
                List<Invoice> invoiceList = new ArrayList<>();
                for (DataSnapshot valueInvoiceDebtPayment : dataSnapshotInvoiceDebtPayment.getChildren()){
                    Invoice invoice = valueInvoiceDebtPayment.getValue(Invoice.class);
                    invoice.setInvoiceId(valueInvoiceDebtPayment.getKey());
                    invoiceList.add(invoice);
                }
                debtPayment.setInvoices(invoiceList);



                iDebtPayment.getDebtPayment(debtPayment);
                Log.d("ListDebtPaymentByDebtor", debtPayment.toString());
            }
        }
    }

    private void getListAllDebtPayments(DataSnapshot dataSnapshot, IDebtPayment iDebtPayment) {
        DataSnapshot dataSnapshotAllDebtPayments = dataSnapshot.child("DebtPayments")
                .child(UserDAO.getInstance().getUserID());
        if (dataSnapshotAllDebtPayments != null) {
            for (DataSnapshot valueAllDebtPayments : dataSnapshotAllDebtPayments.getChildren()) {

            }
        }

    }

    public void addDebtPaymentToFirebase(DebtPayment debtPayment) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("DebtPayments").child(UserDAO.getInstance().getUserID())
                .child(debtPayment.getDebtorId()).child(debtPayment.getDebitPaymentId());
        databaseReference.child("payAmount").setValue(debtPayment.getPayAmount());
        databaseReference.child("payDate").setValue(debtPayment.getPayDate());
        databaseReference.child("payTime").setValue(debtPayment.getPayTime());
        databaseReference.child("debtBeforePay").setValue(debtPayment.getDebtBeforePay());
//        databaseReference.keepSynced(true);
    }

    public void addInvoiceToDebtPaymentFirebase(DebtPayment debtPayment, Invoice invoice) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("DebtPayments").child(UserDAO.getInstance().getUserID())
                .child(debtPayment.getDebtorId()).child(debtPayment.getDebitPaymentId())
                .child("invoiceDebtPayments").child(invoice.getInvoiceId());
        databaseReference.child("payMoney").setValue(invoice.getPayMoney());
        databaseReference.child("debitAmount").setValue(invoice.getDebitAmount());
        databaseReference.child("invoiceDate").setValue(invoice.getInvoiceDate());
        databaseReference.child("invoiceTime").setValue(invoice.getInvoiceTime());
//        databaseReference.child("payAmount").setValue(debtPayment.getPayAmount());
//        databaseReference.child("payDate").setValue(debtPayment.getPayDate());
//        databaseReference.child("payTime").setValue(debtPayment.getPayTime());
//        databaseReference.keepSynced(true);
    }


}
