package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductController {
    //    private ListProductActivity listProductActivity;
//    private DatabaseReference myRef;
//    private ArrayList<Product> list;
    private Context context;
    private Product product;
    ItemAdapter itemAdapter;
    private int item_count;
    private List<Product> productList;

    public ListProductController(Context context) {
        this.context = context;
        product = new Product();
    }

    public void getListProduct(String searchText,  RecyclerView recyclerViewListProduct, final TextView textView,
                               LinearLayout linearLayoutEmpty, ConstraintLayout constraintLayout, LinearLayout layoutNotFoundItem, Spinner category_Spinner) {
        final List<Product> listProduct = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                textView.setText(listProduct.size() + " sản phẩm");
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(searchText, listProductInterface, textView, linearLayoutEmpty, constraintLayout, layoutNotFoundItem, category_Spinner);
    }

    public void getListProduct(Context context, RecyclerView recyclerViewListProduct, String categoryName,
                               final TextView textView, LinearLayout linearLayoutEmpty, ConstraintLayout constraintLayout, LinearLayout layoutNotFoundItem, Spinner category_Spinner) {
        final List<Product> listProduct = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                textView.setText(listProduct.size() + " sản phẩm");
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(listProductInterface, categoryName,linearLayoutEmpty,constraintLayout,layoutNotFoundItem,textView,category_Spinner);

    }
    public void tranIntent(Activity activity1, Class activity2){
        Intent intent = new Intent(activity1.getApplicationContext(),activity2);
        activity1.startActivity(intent);
    }

}
