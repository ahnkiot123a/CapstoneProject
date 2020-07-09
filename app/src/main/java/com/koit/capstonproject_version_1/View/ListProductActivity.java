package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.ListProductController;
import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private LinearLayout linearLayoutEmpty;
    private ConstraintLayout layoutSearch;
    private LinearLayout layoutNotFoundItem;
    ImageButton btnAddNewProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //t1
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        setContentView(R.layout.activity_list_product);
        searchView = findViewById(R.id.searchView);
        imgbtnBarcode = findViewById(R.id.imgbtnBarcode);
        category_Spinner = findViewById(R.id.category_Spinner);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmptyProduct);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutNotFoundItem = findViewById(R.id.layout_not_found_item);
        btnAddNewProduct =findViewById(R.id.btnAddNewProduct);
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listProductController.tranIntent(ListProductActivity.this, CreateProductActivity.class);
            }
        });

        listProductController = new ListProductController(this.getApplicationContext());
        listProductController.getListProduct(null, recyclerViewListProduct, tvTotalQuantity, linearLayoutEmpty, layoutSearch, layoutNotFoundItem,category_Spinner);
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
                listProductController.getListProduct(newText,recyclerViewListProduct, tvTotalQuantity, linearLayoutEmpty, layoutSearch,layoutNotFoundItem,category_Spinner);
                return true;
            }

        });

        listCategoryController = new ListCategoryController(this.getApplicationContext());
        listCategoryController.getListCategory(getApplicationContext(), category_Spinner);

        category_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = category_Spinner.getSelectedItem().toString();
                if (categoryName.equals("Tất cả các loại sản phẩm")) {
                    listProductController.getListProduct(null,recyclerViewListProduct, tvTotalQuantity,
                            linearLayoutEmpty, layoutSearch,layoutNotFoundItem,category_Spinner);
                } else {
                    listProductController.getListProduct(getApplicationContext(), recyclerViewListProduct,
                            categoryName, tvTotalQuantity, linearLayoutEmpty, layoutSearch,layoutNotFoundItem,category_Spinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}