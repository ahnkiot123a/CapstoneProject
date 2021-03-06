package com.koit.capstonproject_version_1.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.controller.Interface.IInvoiceDetail;
import com.koit.capstonproject_version_1.controller.InvoiceDetailController;
import com.koit.capstonproject_version_1.controller.OrderHistoryController;
import com.koit.capstonproject_version_1.model.dao.UserDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetail implements Serializable {
    private String invoiceId;
    private List<Product> products;

    public InvoiceDetail(String invoiceId, List<Product> products) {
        this.invoiceId = invoiceId;
        this.products = products;
    }

    public InvoiceDetail() {
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addInvoiceDetailToFirebase(InvoiceDetail invoiceDetail) {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        String invoiceId = invoiceDetail.getInvoiceId();
        List<Product> listSelectedProductInOrder = invoiceDetail.getProducts();
        for (int i = 0; i < listSelectedProductInOrder.size(); i++) {
            if (!listSelectedProductInOrder.get(i).getProductId().startsWith("nonListedProduct")) {

                for (int j = 0; j < listSelectedProductInOrder.get(i).getUnits().size(); j++) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).
                            child(invoiceId).child("products").child(listSelectedProductInOrder.get(i).getProductId()).child("units");
                    databaseReference = databaseReference.child(listSelectedProductInOrder.get(i).getUnits().get(j).getUnitId());
                    String unitId = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitId();
                    long unitQuantity = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitQuantity();
                    long unitPrice = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitPrice();
                    String unitName = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitName();

                    DatabaseReference databaseReferencePrice = databaseReference.child("unitPrice");
                    databaseReferencePrice.setValue(unitPrice);
                    DatabaseReference databaseReferenceQuantity = databaseReference.child("unitQuantity");
                    databaseReferenceQuantity.setValue(unitQuantity);
                    DatabaseReference databaseReferenceName = databaseReference.child("unitName");
                    databaseReferenceName.setValue(unitName);
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).
                            child(invoiceId).child("products").child(listSelectedProductInOrder.get(i).getProductId()).child("productName");
                    databaseReference.setValue(listSelectedProductInOrder.get(i).getProductName());
                }
            } else {
                for (int j = 0; j < listSelectedProductInOrder.get(i).getUnits().size(); j++) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).
                            child(invoiceId).child("products").child(listSelectedProductInOrder.get(i).getProductId()).child("units");
                    databaseReference = databaseReference.child(listSelectedProductInOrder.get(i).getUnits().get(j).getUnitId());
                    String unitId = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitId();
                    long unitQuantity = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitQuantity();
                    long unitPrice = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitPrice();
                    String unitName = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitName();
                    DatabaseReference databaseReferencePrice = databaseReference.child("unitPrice");
                    databaseReferencePrice.setValue(unitPrice);
                    DatabaseReference databaseReferenceQuantity = databaseReference.child("unitQuantity");
                    databaseReferenceQuantity.setValue(unitQuantity);
                    DatabaseReference databaseReferenceName = databaseReference.child("unitName");
                    databaseReferenceName.setValue(unitName);

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).
                            child(invoiceId).child("products").child(listSelectedProductInOrder.get(i).getProductId()).child("productName");
                    databaseReference.setValue(listSelectedProductInOrder.get(i).getProductName());

                }
            }

        }

//        databaseReference.keepSynced(true);
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "invoiceId='" + invoiceId + '\'' +
                ", products=" + products +
                '}';
    }

    public void getListProductInOrder(final IInvoiceDetail iInvoiceDetail, final String invoiceId) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getListProductInOrder(snapshot, iInvoiceDetail, invoiceId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);


    }


    public void getListProductInDraftOrder(final IInvoiceDetail iInvoiceDetail, final String invoiceId) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getListProductInDraftOrder(snapshot, iInvoiceDetail, invoiceId);
                OrderHistoryController.semaphore.release();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        
    }

    private void getListProductInOrder(DataSnapshot dataSnapshot, IInvoiceDetail iInvoiceDetail, String invoiceId) {
        DataSnapshot dataSnapshotInvoiceDetail = dataSnapshot.child("InvoiceDetail").child(UserDAO.getInstance().getUserID())
                .child(invoiceId).child("products");
        if (dataSnapshotInvoiceDetail != null) {
            for (DataSnapshot valueInvoiceDetail : dataSnapshotInvoiceDetail.getChildren()) {
                Product productInOrder = new Product();
                DataSnapshot dataSnapshotProduct = dataSnapshotInvoiceDetail.child(valueInvoiceDetail.getKey()).child("productName");
                productInOrder.setProductName(dataSnapshotProduct.getValue().toString());
                productInOrder.setProductId(valueInvoiceDetail.getKey());
                DataSnapshot dataSnapshotUnits = dataSnapshotInvoiceDetail.child(productInOrder.getProductId()).child("units");
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnits.getChildren()) {
                    Unit unit = valueUnit.getValue(Unit.class);
                    if (unit != null) {
                        unit.setUnitId(valueUnit.getKey());
                        unitList.add(unit);
                    }
                }
                productInOrder.setUnits(unitList);
                iInvoiceDetail.getListProductInOrder(productInOrder);
                if (productInOrder != null) {
                    InvoiceDetailController.loadSuccess = true;
                    Log.d("ListProductInOrder", productInOrder.toString());

                } else Log.d("ListProductInOrder", "null");
            }

        }

    }

    public void getListProductInDraftOrder(DataSnapshot dataSnapshot, IInvoiceDetail iInvoiceDetail, String invoiceId) {
        DataSnapshot dataSnapshotInvoiceDetail = dataSnapshot.child("InvoiceDetail").child(UserDAO.getInstance().getUserID())
                .child(invoiceId).child("products");
        if (dataSnapshotInvoiceDetail != null) {
            for (DataSnapshot valueInvoiceDetail : dataSnapshotInvoiceDetail.getChildren()) {
                Log.d("ktdataSnapshotProduct", dataSnapshot.toString());
                Product productInOrder = new Product();
                Product productInWarehouse = new Product();
                DataSnapshot dataSnapshotProduct = dataSnapshotInvoiceDetail.child(valueInvoiceDetail.getKey()).child("productName");
                productInOrder.setProductName(dataSnapshotProduct.getValue().toString());
                productInOrder.setProductId(valueInvoiceDetail.getKey());
                DataSnapshot dataSnapshotUnits = dataSnapshotInvoiceDetail.child(productInOrder.getProductId()).child("units");
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnits.getChildren()) {
                    Unit unit = valueUnit.getValue(Unit.class);

                    if (unit != null) {
                        unit.setUnitId(valueUnit.getKey());
                        unitList.add(unit);
                    }
                }
                productInOrder.setUnits(unitList);
                productInWarehouse.setProductName(productInOrder.getProductName());
                productInWarehouse.setUnits(productInOrder.getUnits());
                productInWarehouse.setProductId(productInOrder.getProductId());
                productInWarehouse.setActive(productInOrder.isActive());
                productInWarehouse.setBarcode(productInOrder.getBarcode());
                productInWarehouse.setCategoryName(productInOrder.getCategoryName());
                productInWarehouse.setProductDescription(productInOrder.getProductDescription());
                productInWarehouse.setProductImageUrl(productInOrder.getProductImageUrl());
                productInWarehouse.setUserId(productInOrder.getUserId());

                if (!productInWarehouse.getProductId().startsWith("nonListedProduct")) {
                    DataSnapshot dataSnapshotUnitInWarehouse = dataSnapshot.child("Units").child(UserDAO.getInstance().getUserID())
                            .child(productInWarehouse.getProductId());
                    List<Unit> unitInWarehouse = new ArrayList<>();
                    for (DataSnapshot valueUnit : dataSnapshotUnitInWarehouse.getChildren()) {
                        Unit unit = valueUnit.getValue(Unit.class);

                        if (unit != null) {
                            unit.setUnitId(valueUnit.getKey());
                            unitInWarehouse.add(unit);
                        }
                    }
                    if (unitInWarehouse.size() > 0)
                        productInWarehouse.setUnits(unitInWarehouse);
                    else {
                        productInOrder = null;
                        productInWarehouse = null;
                    }
                }
                if (productInWarehouse != null) {
                    for (int i = 0; i < productInOrder.getUnits().size(); i++) {
                        for (int j = 0; j < productInWarehouse.getUnits().size(); j++) {
                            if (productInOrder.getUnits().get(i).getUnitId()
                                    .equals(productInWarehouse.getUnits().get(j).getUnitId())) {
                                productInOrder.getUnits().get(i).setUnitName(productInWarehouse.getUnits().get(j).getUnitName());
                            }
                        }
                    }
                }


                iInvoiceDetail.getListProductInOrderWWarehouse(productInOrder,productInWarehouse);
                if (productInOrder != null)
                    Log.d("ListProductInOrder", productInOrder.toString());
                else Log.d("ListProductInOrder", "null");
                if (productInWarehouse != null)
                    Log.d("ListProductWarehouse", productInWarehouse.toString());
                else
                    Log.d("ListProductWarehouse", "null");

            }

        }

    }

}
