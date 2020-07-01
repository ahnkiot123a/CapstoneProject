package com.koit.capstonproject_version_1.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ListUnitInterface;

import androidx.annotation.NonNull;

public class Unit {
    private String unitName;
    private long convertRate, unitPrice, unitQuantity;
    DatabaseReference nodeRoot;

    public Unit(String unitName, long convertRate, long unitPrice, long unitQuantity) {
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

    public Unit() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public long getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(long convertRate) {
        this.convertRate = convertRate;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(long unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public void getListUnit(final ListUnitInterface listUnitInterface) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotUnit = dataSnapshot.child("Products").child("0399271212").child("02");
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Unit unit = valueUnit.getValue(Unit.class);
                    Log.d("Test", unit.getUnitName() + "");
                    listUnitInterface.getListUnitModel(unit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
}
