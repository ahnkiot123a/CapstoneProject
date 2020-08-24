package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.koit.capstonproject_version_1.Adapter.ItemBeforeOrderAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.SelectProductActivity;

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
                if (product.isActive()){
                    listProduct.add(product);
                itemAdapter.notifyDataSetChanged();}
            }

        };
        product.getListProduct(searchText, listProductInterface, linearLayoutEmpty,
                layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, layoutButton);
    }

    public void getListProduct( RecyclerView recyclerViewListProduct, String categoryName, LinearLayout linearLayoutEmpty, LinearLayout
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
                if (product.isActive()){
                    listProduct.add(product);
                itemAdapter.notifyDataSetChanged();}
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
//    public void deleteListItemSelected() {
//        for (int i = 0; i < itemAdapter.getItemCount(); i++) {
//            ItemBeforeOrderAdapter.MyViewHolder viewHolder = (ItemBeforeOrderAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
//            viewHolder.getItemCount().setText("0");
//        }
//    }
}
