package com.koit.capstonproject_version_1.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.ListProductController;
import com.koit.capstonproject_version_1.Controller.SwipeController;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListProductActivity extends AppCompatActivity {
    private ImageView imageView;
    private Spinner category_Spinner;
    private TextView tvTotalQuantity;
    private SearchView searchView;
    private RecyclerView recyclerViewListProduct;
    private ListProductController listProductController;
    private ListCategoryController listCategoryController;
    private LinearLayout linearLayoutEmpty;
    private ConstraintLayout layoutSearch;
    private LinearLayout layoutNotFoundItem;
    private ImageButton btnAddNewProduct;
    private ProgressBar pBarList;
    private SwipeController swipeController = null;
    private ImageButton imgbtnBarcodeInList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_list_product);
        searchView = findViewById(R.id.searchViewInList);
        category_Spinner = findViewById(R.id.category_Spinner);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmptyProduct);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutNotFoundItem = findViewById(R.id.layout_not_found_item);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        pBarList = findViewById(R.id.pBarList);
        imgbtnBarcodeInList = findViewById(R.id.imgbtnBarcodeInList);
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listProductController.tranIntent(ListProductActivity.this, CreateProductActivity.class);
            }
        });
        searchView.clearFocus();
        listProductController = new ListProductController(this.getApplicationContext());
        //swipe
        listProductController.setupRecyclerView(recyclerViewListProduct, this);
        listProductController.getListProduct(null, recyclerViewListProduct, tvTotalQuantity,
                linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listProductController.getListProduct(newText, recyclerViewListProduct, tvTotalQuantity,
                        linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
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
                    listProductController.getListProduct(null, recyclerViewListProduct, tvTotalQuantity,
                            linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                } else {
                    listProductController.getListProduct(getApplicationContext(), recyclerViewListProduct,
                            categoryName, tvTotalQuantity, linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void addNewProduct(View view) {
        Intent intent = new Intent(this, CreateProductActivity.class);
        startActivity(intent);
    }


    public void back(View view) {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        this.startActivity(intent);
    }

    public void searchByBarcode(View view) {
        CameraController cameraController = new CameraController(this);
        cameraController.askCameraPermission(CreateProductActivity.BARCODE_PER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
//                etBarcode.setText("");
            } else {
                String barcode = intentResult.getContents();
                listProductController.getListProduct(barcode, recyclerViewListProduct, tvTotalQuantity,
                        linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                searchView.setQuery(barcode, false);
            }
        }
    }


}