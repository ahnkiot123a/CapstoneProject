package com.koit.capstonproject_version_1.Model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.util.List;

public class InvoiceDetail implements Serializable {
    private String invoiceId;
    private List<Product> products;

    public InvoiceDetail(String invoiceId, List<Product> products) {
        this.invoiceId = invoiceId;
        this.products = products;
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
            if (listSelectedProductInOrder.get(i).getProductId().startsWith("PRODUCT")) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference = databaseReference.child("InvoiceDetail").child(UserDAO.getInstance().getUserID()).
                        child(invoiceId).child("products").child(listSelectedProductInOrder.get(i).getProductId()).child("units");
                for (int j = 0; j < listSelectedProductInOrder.get(i).getUnits().size(); j++) {
                    databaseReference = databaseReference.child(listSelectedProductInOrder.get(i).getUnits().get(j).getUnitId());
                    String unitId = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitId();
                    long unitQuantity = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitQuantity();
                    long unitPrice = listSelectedProductInOrder.get(i).getUnits().get(j).getUnitPrice();

                    DatabaseReference databaseReferencePrice = databaseReference.child("unitPrice");
                    databaseReferencePrice.setValue(unitPrice);
                    DatabaseReference databaseReferenceQuantity = databaseReference.child("unitQuantity");
                    databaseReferenceQuantity.setValue(unitQuantity);


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
}
