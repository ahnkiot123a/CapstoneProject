package com.koit.capstonproject_version_1.dao;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
                categoryList.add(category);
                final ArrayAdapter<Category> adapter =
                        new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                categoryList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        };
        category.getListCategory(iCategory);
    }


}
