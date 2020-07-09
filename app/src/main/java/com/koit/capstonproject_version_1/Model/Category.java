package com.koit.capstonproject_version_1.Model;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ICategory;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String categoryId, categoryName;
    private DataSnapshot dataRoot;
    DatabaseReference nodeRoot;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    UserDAO userDAO;
    User currentUser;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Category() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        userDAO = new UserDAO();
    }

    @Override
    public String toString() {
        return categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public void getListCategory(final ICategory iCategory) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                getListCategory(dataSnapshot, iCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if (dataRoot != null) {
            getListCategory(dataRoot, iCategory);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void getListCategory(DataSnapshot dataSnapshot, ICategory iCategory) {
        DataSnapshot dataSnapshotCategory = dataSnapshot.child("Categories").child(userDAO.getUserID());
        //Lấy danh sách san pham
        for (DataSnapshot valueCategory : dataSnapshotCategory.getChildren()) {
            Category category = valueCategory.getValue(Category.class);
            category.setCategoryId(category.getCategoryId());
            iCategory.getCategory(category);
        }
    }

    public void addCategoryToFireBase(String userId){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Categories").child(userId);
        databaseReference.push().setValue(this);
    }
}
