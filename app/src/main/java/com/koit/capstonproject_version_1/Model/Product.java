package com.koit.capstonproject_version_1.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.View.LoginActivity;

import java.util.List;

public class Product {
    private String userId, productId, barcode, categoryName, productDescription, productImageUrl;
    private String productName;
    private  boolean active;

    private List<Unit> units;

    public Product(String userId, String productId, String barcode, String categoryName, String productDescription, String productImageUrl, String productName, boolean active, List<Unit> units) {
        this.userId = userId;
        this.productId = productId;
        this.barcode = barcode;
        this.categoryName = categoryName;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.active = active;
        this.units = units;
    }

    public Product() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public DatabaseReference getMyRef() {
        String curUser;
        User user = SharedPrefs.getInstance().getCurrentUser(LoginActivity.CURRENT_USER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            curUser = user.getPhoneNumber();
//        } else {
//            curUser = firebaseUser.getUid();
//        }
        DatabaseReference myRef;
        //test
        myRef = FirebaseDatabase.getInstance().getReference("Products").child("0399271212");
        return myRef;
    }
}
