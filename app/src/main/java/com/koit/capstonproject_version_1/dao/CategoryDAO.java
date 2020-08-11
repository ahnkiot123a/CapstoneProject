package com.koit.capstonproject_version_1.dao;

import android.content.Context;
import android.service.autofill.UserData;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.Interface.ICategory;
import com.koit.capstonproject_version_1.Model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static CategoryDAO mInstance;
    private DatabaseReference databaseReference;
    String userId;
    private Category category;

    public CategoryDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = UserDAO.getInstance().getUserID();
        category = new Category();
    }

    public static CategoryDAO getInstance() {
        if (mInstance == null) {
            mInstance = new CategoryDAO();
        }
        return mInstance;
    }

    public void getListCategory(final Context context, final ListView listView) {
        final List<Category> categoryList = new ArrayList<>();
        ICategory iCategory = new ICategory() {
            @Override
            public void getCategory(Category category) {
                Log.d("kiemtra", category.getCategoryName() + "");
                categoryList.add(category);
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1, categoryList);
                listView.setAdapter(adapter);
            }
        };
        category.getListCategory(iCategory);
    }
    public List<Category> getCategories(){
        final List<Category> categories = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Categories")
                .child(UserDAO.getInstance().getUserID());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot dataSnapshotCategory : snapshot.getChildren()){
                    String id = dataSnapshotCategory.getKey();
                    String name = (String) dataSnapshotCategory.getValue();
                    Category category = new Category(id,name);

                    categories.add(category);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("ktCategories",categories.size()+"");
        return  categories;
    }
}
