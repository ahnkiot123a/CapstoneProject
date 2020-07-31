package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemInOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewListProduct;
    private SearchView searchViewInList;
    private TextView tvTotalQuantity;
    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_list_item_in_order);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        searchViewInList = findViewById(R.id.searchViewInList);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        searchViewInList.clearFocus();
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));
        getListProduct();
    }

    private void getListProduct() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        List<Product> listSelectedProduct = (ArrayList<Product>) args.getSerializable("ListSelectedProduct");
        List<Product> listSelectedProductInOrder = new ArrayList<>();
        //add product to new list
        //item in new list contain name, id, Unit(name, price, quantity)
        for (Product product : listSelectedProduct
        ) {
            Product productInOrder = new Product();
            productInOrder.setProductId(product.getProductId());
            productInOrder.setProductName(product.getProductName());
            productInOrder.setUnits(getMinUnit(product.getUnits()));
            listSelectedProductInOrder.add(productInOrder);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        ItemInOrderAdapter itemAdapter = new ItemInOrderAdapter(this, R.layout.item_layout_in_order,
                listSelectedProduct, tvTotalQuantity, tvTotalPrice, listSelectedProductInOrder);
        recyclerViewListProduct.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }

    public List<Unit> getMinUnit(List<Unit> unitList) {
        List<Unit> list = new ArrayList<>();
        if (unitList != null)
            for (Unit unit : unitList) {
                if (unit.getConvertRate() == 1) {
                    Unit unitInOrder = new Unit();
                    unitInOrder.setUnitId(unit.getUnitId());
                    unitInOrder.setUnitName(unit.getUnitName());
                    unitInOrder.setUnitPrice(unit.getUnitPrice());
                    unitInOrder.setUnitQuantity(1);
                    list.add(unitInOrder);
                    break;
                }
            }
        // list contain max 1 item
        return list;
    }


    public void back(View view) {
        onBackPressed();
    }

    public void searchByBarcode(View view) {
    }

    public void addNoneListedProduct(View view) {
    }
}