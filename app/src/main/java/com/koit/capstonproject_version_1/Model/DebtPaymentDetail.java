package com.koit.capstonproject_version_1.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPayment;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPaymentDetail;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DebtPaymentDetail implements Serializable {
    private String debtorId;
    private List<DebtPayment> debtPaymentList;

    public DebtPaymentDetail() {
    }

    public DebtPaymentDetail(String debtorId, List<DebtPayment> debtPaymentList) {
        this.debtorId = debtorId;
        this.debtPaymentList = debtPaymentList;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public List<DebtPayment> getDebtPaymentList() {
        return debtPaymentList;
    }

    public void setDebtPaymentList(List<DebtPayment> debtPaymentList) {
        this.debtPaymentList = debtPaymentList;
    }

    @Override
    public String toString() {
        return "DebtPaymentDetail{" +
                "debtorId='" + debtorId + '\'' +
                ", debtPaymentList=" + debtPaymentList +
                '}';
    }
    public void getListDebtPayments(final IDebtPaymentDetail iDebtPaymentDetail) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getListDebtPayments(snapshot, iDebtPaymentDetail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListDebtPayments(DataSnapshot dataSnapshot, IDebtPaymentDetail iDebtPaymentDetail) {
        DataSnapshot dataSnapshotAllDebtPayments = dataSnapshot.child("DebtPayments")
                .child(UserDAO.getInstance().getUserID());
        if (dataSnapshotAllDebtPayments != null) {
            for (DataSnapshot valueAllDebtPayments : dataSnapshotAllDebtPayments.getChildren()) {
                DebtPaymentDetail debtPaymentDetail = new DebtPaymentDetail();
                debtPaymentDetail.setDebtorId(valueAllDebtPayments.getKey());
                DataSnapshot dataSnapshotDebtPayment = dataSnapshotAllDebtPayments
                        .child(debtPaymentDetail.getDebtorId());
                List<DebtPayment> debtPayments = new ArrayList<>();
                for (DataSnapshot valueDebtPayment : dataSnapshotDebtPayment.getChildren()) {
                    DebtPayment debtPayment = valueDebtPayment.getValue(DebtPayment.class);
                    debtPayment.setDebitPaymentId(valueDebtPayment.getKey());
                    debtPayment.setDebtorId(debtPaymentDetail.getDebtorId());
                    debtPayments.add(debtPayment);
                }
                debtPaymentDetail.setDebtPaymentList(debtPayments);
                iDebtPaymentDetail.getDebtPaymentDetail(debtPaymentDetail);
                Log.d("debtPaymentDetail", debtPaymentDetail.toString());
            }
        }

    }
}
