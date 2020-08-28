package com.koit.capstonproject_version_1.Model;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;

public class Debtor implements Serializable {
    private String debtorId, address, dateOfBirth, email, fullName, phoneNumber;
    private boolean gender;
    private long remainingDebit;
    private DatabaseReference nodeRoot;
    private DataSnapshot dataRoot;
    private static Debtor mInstance;

    public static Debtor getInstance() {
        if (mInstance == null) {
            mInstance = new Debtor();
        }
        return mInstance;
    }

    public Debtor(String debtorId, String address, String dateOfBirth, String email, String fullName, String phoneNumber, boolean gender, long remainingDebit) {
        this.debtorId = debtorId;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.remainingDebit = remainingDebit;
    }

    public Debtor() {
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public long getRemainingDebit() {
        return remainingDebit;
    }

    public void setRemainingDebit(long remainingDebit) {
        this.remainingDebit = remainingDebit;
    }

    @Override
    public String toString() {
        return "Debtor{" +
                "debtorId='" + debtorId + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", remainingDebit=" + remainingDebit +
                '}';
    }

    public void setDebtMoneyView(String id, final Debtor newDebtor, final TextView tvDebtTotal,
                                 final TextView tvRemainingDebt, final ConstraintLayout constraintPayAllDebt,
                                final LinearLayout linearInputPayDebt, final FloatingActionButton btnConfirm) {
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                if (debtor != null) {
                    newDebtor.setRemainingDebit(debtor.getRemainingDebit());
//                    debtorName = debtor.getFullName();
                }
            }
        };
        getDebtorById(id, iDebtor, tvDebtTotal, tvRemainingDebt,constraintPayAllDebt,
                linearInputPayDebt, btnConfirm);
    }

    private void getDebtorById(String id, final IDebtor iDebtor, final TextView tvDebtTotal,
                               final TextView tvRemainingDebt, final ConstraintLayout constraintPayAllDebt,
                              final LinearLayout linearInputPayDebt, final FloatingActionButton btnConfirm) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Debtors").child(UserDAO.getInstance().getUserID()).child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Debtor debtor = snapshot.getValue(Debtor.class);
                debtor.setDebtorId(snapshot.getKey());
                iDebtor.getDebtor(debtor);
                if (debtor != null) {
                    Log.d("debtorDebtMoneyView", debtor.toString());
                    if (debtor.getRemainingDebit() > 0){
                        btnConfirm.setVisibility(View.VISIBLE);
                        constraintPayAllDebt.setVisibility(View.GONE);
                        linearInputPayDebt.setVisibility(View.VISIBLE);
                        tvDebtTotal.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
                        tvRemainingDebt.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
                    } else {
                        btnConfirm.setVisibility(View.GONE);
                        constraintPayAllDebt.setVisibility(View.VISIBLE);
                        linearInputPayDebt.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());

            }
        });
    }

    public void setDebtorInformation(String id, final Debtor newDebtor,
                                 final TextView tvDebtTotal, final TextView tvDebtAmountTotal,
                                    final TextView tvPayAmountTotal) {
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                if (debtor != null) {
                    newDebtor.setRemainingDebit(debtor.getRemainingDebit());
//                    debtorName = debtor.getFullName();
                }
            }
        };
        getDebtorById(id, iDebtor, tvDebtTotal, tvDebtAmountTotal, tvPayAmountTotal);
    }

    private void getDebtorById(String id, final IDebtor iDebtor,
                               final TextView tvDebtTotal, final TextView tvDebtAmountTotal,
                               final TextView tvPayAmountTotal) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Debtors").child(UserDAO.getInstance().getUserID()).child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Debtor debtor = snapshot.getValue(Debtor.class);
                debtor.setDebtorId(snapshot.getKey());
                iDebtor.getDebtor(debtor);
                if (debtor != null) {
                    Log.d("debtorDebtMoneyView", debtor.toString());
                    tvDebtAmountTotal.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
                    long payAmountTotal = Money.getInstance().reFormatVND(tvPayAmountTotal.getText().toString());
                    long debtTotal = payAmountTotal + debtor.getRemainingDebit();
                    tvDebtTotal.setText(Money.getInstance().formatVN(debtTotal) + " đ");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());

            }
        });
    }

    public void getListDebtor(final IDebtor iDebtor) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListDebtor(dataSnapshot, iDebtor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getListDebtor(final IDebtor iDebtor, final LinearLayout layoutDebtors, final ConstraintLayout layout_not_found_item) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListDebtor(dataSnapshot, iDebtor, layoutDebtors, layout_not_found_item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getListDebtor(final IDebtor iDebtor, final LinearLayout linearLayoutEmptyDebit,
                              final LinearLayout linearLayoutDebitInfo, final LottieAnimationView animationView) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListDebtor(dataSnapshot, iDebtor, linearLayoutEmptyDebit, linearLayoutDebitInfo, animationView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListDebtor(DataSnapshot dataSnapshot, IDebtor iDebtor) {
        DataSnapshot dataSnapshotDebtor = dataSnapshot.child("Debtors").child(UserDAO.getInstance().getUserID());
        if (dataSnapshotDebtor != null) {
            for (DataSnapshot valueCustomer : dataSnapshotDebtor.getChildren()) {
                Debtor debtor = valueCustomer.getValue(Debtor.class);
                debtor.setDebtorId(valueCustomer.getKey());
                iDebtor.getDebtor(debtor);
            }
        }
    }

    private void getListDebtor(DataSnapshot dataSnapshot, IDebtor iDebtor,
                               final LinearLayout layoutDebtors, final ConstraintLayout layout_not_found_item) {
        DataSnapshot dataSnapshotDebtor = dataSnapshot.child("Debtors").child(UserDAO.getInstance().getUserID());
        if (dataSnapshotDebtor.getValue() != null) {
            layoutDebtors.setVisibility(View.VISIBLE);
            layout_not_found_item.setVisibility(View.GONE);
            for (DataSnapshot valueCustomer : dataSnapshotDebtor.getChildren()) {
                Debtor debtor = valueCustomer.getValue(Debtor.class);
                debtor.setDebtorId(valueCustomer.getKey());
                iDebtor.getDebtor(debtor);
            }
        } else {
            layout_not_found_item.setVisibility(View.VISIBLE);
            layoutDebtors.setVisibility(View.GONE);
        }
    }

    private void getListDebtor(DataSnapshot dataSnapshot, IDebtor iDebtor, final LinearLayout linearLayoutEmptyDebit,
                               final LinearLayout linearLayoutDebitInfo, LottieAnimationView animationView) {
        DataSnapshot dataSnapshotDebtor = dataSnapshot.child("Debtors").child(UserDAO.getInstance().getUserID());
        if (dataSnapshotDebtor.getValue() != null) {
            animationView.setVisibility(View.GONE);
            linearLayoutEmptyDebit.setVisibility(View.GONE);
            linearLayoutDebitInfo.setVisibility(View.VISIBLE);
            for (DataSnapshot valueCustomer : dataSnapshotDebtor.getChildren()) {
                Debtor debtor = valueCustomer.getValue(Debtor.class);
                debtor.setDebtorId(valueCustomer.getKey());
                iDebtor.getDebtor(debtor);
            }
        } else {
            linearLayoutEmptyDebit.setVisibility(View.VISIBLE);
            linearLayoutDebitInfo.setVisibility(View.GONE);
            animationView.setVisibility(View.GONE);

        }
    }

    public void addDebtorToFirebase(Debtor debtor) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("Debtors").child(UserDAO.getInstance().getUserID())
                .child(debtor.getDebtorId());
        debtor.setDebtorId(null);
        databaseReference.setValue(debtor);
        databaseReference.keepSynced(true);
    }

    public void updateDebtorToFirebase(Debtor debtor) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("Debtors").child(UserDAO.getInstance().getUserID())
                .child(debtor.getDebtorId());
        databaseReference.child("address").setValue(debtor.getAddress());
        databaseReference.child("dateOfBirth").setValue(debtor.getDateOfBirth());
        databaseReference.child("email").setValue(debtor.getEmail());
        databaseReference.child("fullName").setValue(debtor.getFullName());
        databaseReference.child("gender").setValue(debtor.isGender());
        databaseReference.child("phoneNumber").setValue(debtor.getPhoneNumber());
        databaseReference.keepSynced(true);
    }

    public void updateRemainingDebit(Debtor debtor) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Debtors")
                .child(UserDAO.getInstance().getUserID()).child(debtor.getDebtorId()).child("remainingDebit");
        databaseReference.setValue(debtor.getRemainingDebit());
    }
}
