package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.koit.capstonproject_version_1.adapter.ItemBeforeOrderAdapter;
import com.koit.capstonproject_version_1.controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectProductController extends AppCompatActivity {
    private Activity context;
    private Product product;
    private ItemBeforeOrderAdapter itemAdapter;
    private List<Product> listProduct;
    private List<Product> listSelectedProduct;

    public SelectProductController(Activity context) {
        this.context = context;
        product = new Product();
        listSelectedProduct = new ArrayList<>();
    }

    public void getListProduct(String searchText, RecyclerView recyclerViewListProduct,
                               LinearLayout linearLayoutEmpty, LinearLayout layoutSearch,
                               LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList, CheckBox checkBoxSelectMultiProduct, LinearLayout layoutButton) {
        listProduct = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//        recyclerView = recyclerViewListProduct;
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemBeforeOrderAdapter(context, listProduct, R.layout.item_layout_before_order,
                checkBoxSelectMultiProduct, listSelectedProduct);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                if (product.isActive()) {
                    listProduct.add(product);
                    itemAdapter.notifyDataSetChanged();
                }
            }

        };
        product.getListProduct(searchText, listProductInterface, linearLayoutEmpty,
                layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, layoutButton);
    }

    public void getListProduct(RecyclerView recyclerViewListProduct, String categoryName, LinearLayout linearLayoutEmpty, LinearLayout
            layoutSearch, LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList, CheckBox checkBoxSelectMultiProduct, LinearLayout layoutButton) {
        listProduct = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemBeforeOrderAdapter(context, listProduct, R.layout.item_layout_before_order,
                checkBoxSelectMultiProduct, listSelectedProduct);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                if (product.isActive()) {
                    listProduct.add(product);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        };
        product.getListProduct(listProductInterface, categoryName, linearLayoutEmpty, layoutSearch,
                layoutNotFoundItem, pBarList, layoutButton);

    }


    public void tranIntent(Activity activity1, Class activity2) {
        Intent intent = new Intent(activity1.getApplicationContext(), activity2);
        activity1.startActivity(intent);
    }

    public List<Product> getListSelectedProduct() {
        return listSelectedProduct;
    }


}
