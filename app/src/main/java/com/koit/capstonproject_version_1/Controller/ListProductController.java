package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

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

    public void getListProduct(Context context, RecyclerView recyclerViewListProduct) {
        final List<Product> listProduct = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(listProductInterface);

    }
    public void getListProduct(Context context, RecyclerView recyclerViewListProduct, String categoryName) {
        final List<Product> listProduct = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(context, listProduct, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(listProductInterface,categoryName);

    }
    public void setTVTotalProductInCate(TextView textView) {
         product.setTotalProductCate(textView);
    }

    public void firebaseProductSearch(RecyclerView recyclerViewListProduct, String searchText, Context context) {
        product.firebaseProductSearch(recyclerViewListProduct, searchText, context);
    }


}
