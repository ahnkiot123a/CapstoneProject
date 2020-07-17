package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.SelectProductController;
import com.koit.capstonproject_version_1.Controller.SwipeController;
import com.koit.capstonproject_version_1.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectProductActivity extends AppCompatActivity {

    private Spinner category_Spinner;
    private SearchView searchView;
    private RecyclerView recyclerViewListProduct;
    private ListCategoryController listCategoryController;
    private LinearLayout linearLayoutEmpty;
    private LinearLayout layoutSearch;
    private LinearLayout layoutNotFoundItem;
    private SelectProductController selectProductController;
    private ImageButton btnAddNewProduct;
    private ProgressBar pBarList;
    private SwipeController swipeController = null;
    private ImageButton imgbtnBarcodeInList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //t1
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        setContentView(R.layout.activity_select_product);
        category_Spinner = findViewById(R.id.category_Spinner);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmptyProduct);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutNotFoundItem = findViewById(R.id.layout_not_found_item);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        pBarList = findViewById(R.id.pBarList);
        imgbtnBarcodeInList = findViewById(R.id.imgbtnBarcodeInList);
        searchView = findViewById(R.id.searchViewInList);

        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));

        selectProductController = new SelectProductController(this.getApplicationContext());

        selectProductController.getListProduct(null, recyclerViewListProduct,
                linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectProductController.getListProduct(newText, recyclerViewListProduct,
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
                    selectProductController.getListProduct(null, recyclerViewListProduct,
                            linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                } else {
                    selectProductController.getListProduct(getApplicationContext(), recyclerViewListProduct,
                            categoryName, linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                selectProductController.getListProduct(barcode, recyclerViewListProduct,
                        linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                searchView.setQuery(barcode, false);

            }
        }
    }


    public void addNewProduct(View view) {
        selectProductController.tranIntent(SelectProductActivity.this, CreateProductActivity.class);
    }
}