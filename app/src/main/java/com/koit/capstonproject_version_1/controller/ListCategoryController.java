package com.koit.capstonproject_version_1.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.koit.capstonproject_version_1.controller.Interface.ICategory;
import com.koit.capstonproject_version_1.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryController {
    private Context context;
    private Category category;
    private List<Category> categoryList;


    public ListCategoryController(Context context) {
        this.context = context;
        category = new Category();
    }

    public ListCategoryController() {
    }
    public void getListCategory(final Context context, final Spinner spinnerCategory) {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("00","Tất cả các loại sản phẩm"));
        ICategory iCategory = new ICategory() {
            @Override
            public void getCategory(Category category) {
                Log.d("kiemtra", category.getCategoryName() + "");
                categoryList.add(category);
                ArrayAdapter<Category> adapter =
                        new ArrayAdapter<Category>(context,
                                android.R.layout.simple_spinner_dropdown_item,
                                categoryList);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                spinnerCategory.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        category.getListCategory(iCategory);
    }



    public void getListCategory(final Context context, final ListView listView, final ConstraintLayout layout_not_found_item) {
        categoryList = new ArrayList<>();
        layout_not_found_item.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        ICategory iCategory = new ICategory() {
            @Override
            public void getCategory(Category category) {
                Log.d("kiemtra", category.getCategoryName() + "");
                layout_not_found_item.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                categoryList.add(category);
                ArrayAdapter<Category> adapter =
                        new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                categoryList);
                listView.setAdapter(adapter);
            }
        };
        category.getListCategory(iCategory);
    }
    public void getListCategory(final Context context) {
        categoryList = new ArrayList<>();
        ICategory iCategory = new ICategory() {
            @Override
            public void getCategory(Category category) {
              //  Log.d("kiemtra", category.getCategoryName() + "");
                categoryList.add(category);

            }
        };
        category.getListCategory(iCategory);
    }

    public List<Category> getCategories() {
        return categoryList;
    }
}
