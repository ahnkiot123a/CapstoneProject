package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.ListProductController;
import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageButton imgbtnBarcode;
    private Spinner category_Spinner;
    private TextView tvTotalQuantity;
    private SearchView searchView;
    private RecyclerView recyclerViewListProduct;
    private ListProductController listProductController;
    private ListCategoryController listCategoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        searchView = findViewById(R.id.searchView);
        imgbtnBarcode = findViewById(R.id.imgbtnBarcode);
        category_Spinner = findViewById(R.id.category_Spinner);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));

        listProductController = new ListProductController(this.getApplicationContext());
        listProductController.getListProduct(getApplicationContext(),recyclerViewListProduct);
        listProductController.setTVTotalProductInCate(tvTotalQuantity);
        imgbtnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listProductController.firebaseProductSearch(recyclerViewListProduct, newText,getApplicationContext());
                return true;
            }

        });

        listCategoryController = new ListCategoryController(this.getApplicationContext());
        listCategoryController.getListCategory(getApplicationContext(),category_Spinner);

        category_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = category_Spinner.getSelectedItem().toString();
                if (categoryName.equals("Tất cả các loại sản phẩm")) {
                    listProductController.getListProduct(getApplicationContext(),recyclerViewListProduct);
                    listProductController.setTVTotalProductInCate(tvTotalQuantity);
                } else  {
                    listProductController.getListProduct(getApplicationContext(),recyclerViewListProduct,categoryName);
                    listProductController.setTVTotalProductInCate(tvTotalQuantity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}