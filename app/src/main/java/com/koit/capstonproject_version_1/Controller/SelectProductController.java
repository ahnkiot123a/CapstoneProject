package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Adapter.ItemBeforeOrderAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.ListProductActivity;
import com.koit.capstonproject_version_1.View.UpdateProductActivity;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectProductController extends AppCompatActivity {
    private Context context;
    private Product product;
    ItemBeforeOrderAdapter itemAdapter;
    private List<Product> listProduct;
    SwipeController swipeController = null;
    RecyclerView recyclerView;

    public SelectProductController(Context context) {
        this.context = context;
        product = new Product();
    }

    public void getListProduct(String searchText, RecyclerView recyclerViewListProduct,
                               LinearLayout linearLayoutEmpty, LinearLayout layoutSearch,
                               LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList) {
        listProduct = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView = recyclerViewListProduct;
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemBeforeOrderAdapter(context, listProduct, R.layout.item_layout_before_order);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                itemAdapter.notifyDataSetChanged();
            }

        };
        product.getListProduct(searchText, listProductInterface, linearLayoutEmpty,
                layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
    }

    public void getListProduct(Context context, RecyclerView recyclerViewListProduct, String categoryName, LinearLayout linearLayoutEmpty, LinearLayout
            layoutSearch, LinearLayout layoutNotFoundItem, Spinner category_Spinner, ProgressBar pBarList) {
        listProduct = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemBeforeOrderAdapter(context, listProduct, R.layout.item_layout_before_order);
        recyclerViewListProduct.setAdapter(itemAdapter);
        ListProductInterface listProductInterface = new ListProductInterface() {
            @Override
            public void getListProductModel(Product product) {
                listProduct.add(product);
                itemAdapter.notifyDataSetChanged();
            }
        };
        product.getListProduct( listProductInterface, categoryName,linearLayoutEmpty, layoutSearch,
                        layoutNotFoundItem, pBarList);

    }



    public void tranIntent(Activity activity1, Class activity2) {
        Intent intent = new Intent(activity1.getApplicationContext(), activity2);
        activity1.startActivity(intent);
    }
}
