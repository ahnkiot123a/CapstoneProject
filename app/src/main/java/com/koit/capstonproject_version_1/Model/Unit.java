package com.koit.capstonproject_version_1.Model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class Unit implements Serializable {
    private String unitId;
    private String unitName;
    private long convertRate, unitPrice, unitQuantity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    public Unit(String unitId, String unitName) {
        this.unitId = unitId;
        this.unitName = unitName;
    }

    public Unit(String unitName, long convertRate, long unitPrice, long unitQuantity) {
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

    public Unit(String unitId, String unitName, long convertRate, long unitPrice, long unitQuantity) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

    public Unit() {
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    @Override
    public String toString() {
        return "Unit{" +
                "unitId='" + unitId + '\'' +
                ", unitName='" + unitName + '\'' +
                ", convertRate=" + convertRate +
                ", unitPrice=" + unitPrice +
                ", unitQuantity=" + unitQuantity +
                '}';
    }

    public void removeProductUnits(String userId, String productId){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Units").child(userId).child(productId);
        databaseReference.removeValue();
    }
    public void addUnitsToFirebase(String userId, String productId, List<Unit> unitList){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Units").child(userId).child(productId);
//        databaseReference.push().setValue(this);
        databaseReference.setValue(unitList);
    }
}
