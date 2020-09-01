package com.koit.capstonproject_version_1.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.ListProductController;
import com.koit.capstonproject_version_1.Controller.SwipeController;
import com.koit.capstonproject_version_1.Model.UIModel.MyDialog;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

        initView();


        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listProductController.tranIntent(ListProductActivity.this, CreateProductActivity.class);
            }
        });
        listProductController = new ListProductController(this);
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

        listCategoryController = new ListCategoryController(getApplicationContext());
        listCategoryController.getListCategory(getApplicationContext(), category_Spinner);

        category_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = category_Spinner.getSelectedItem().toString();
                if (categoryName.equals("Tất cả các loại sản phẩm")) {
                    listProductController.getListProduct(null, recyclerViewListProduct, tvTotalQuantity,
                            linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                } else {
                    listProductController.getListProduct(recyclerViewListProduct,
                            categoryName, tvTotalQuantity, linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getIntentFromHome();
    }

    private void initView() {
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
    }

    private void getIntentFromHome() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            if (bundle.getBoolean("isFromHomeFragment")) {
                searchView.requestFocus();
                Log.d("isFromhone", 1 + "");
            } else {
                searchView.clearFocus();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.setQuery("", false);
        layoutSearch.requestFocus();
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    public void addNewProduct(View view) {
        Intent intent = new Intent(this, CreateProductActivity.class);
        startActivity(intent);
    }


    public void back(View view) {
        onBackPressed();
    }

    public void searchByBarcode(View view) {
        CameraController cameraController = new CameraController(this);
        cameraController.askCameraPermission(CreateProductActivity.BARCODE_PER_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        this.startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        listProductController.getListProduct(null, recyclerViewListProduct, tvTotalQuantity,
//                linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
//                etBarcode.setText("");
            } else {
                String barcode = intentResult.getContents().trim();
                searchView.setQuery(barcode, true);
                searchView.clearFocus();
            }
        }
    }


}