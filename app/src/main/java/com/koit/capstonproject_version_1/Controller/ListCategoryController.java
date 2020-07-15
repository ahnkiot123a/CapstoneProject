package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ICategory;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

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



    public void getListCategory(final Context context, final ListView listView) {
        categoryList = new ArrayList<>();
        ICategory iCategory = new ICategory() {
            @Override
            public void getCategory(Category category) {
                Log.d("kiemtra", category.getCategoryName() + "");
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
