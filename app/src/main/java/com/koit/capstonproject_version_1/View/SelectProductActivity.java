package com.koit.capstonproject_version_1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.ListCategoryController;
import com.koit.capstonproject_version_1.Controller.SelectProductController;
import com.koit.capstonproject_version_1.Controller.SwipeController;
import com.koit.capstonproject_version_1.Controller.SwipeControllerActions;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private CheckBox checkBoxSelectMultiProduct;
    private LinearLayout layoutButton;
    private static SelectProductActivity instance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //t1
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        StatusBar.setStatusBar(this);
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
        checkBoxSelectMultiProduct = findViewById(R.id.checkBoxSelectMultiProduct);
        layoutButton = findViewById(R.id.layoutBtnSelectedProduct);
        searchView.clearFocus();

        checkBoxSelectMultiProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutButton.setVisibility(View.VISIBLE);
                } else {
                    layoutButton.setVisibility(View.GONE);
                }
            }
        });
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));

        selectProductController = new SelectProductController(this.getApplicationContext());

        selectProductController.getListProduct(null, recyclerViewListProduct,
                linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectProductController.getListProduct(newText, recyclerViewListProduct,
                        linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
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
                            linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
                } else {
                    selectProductController.getListProduct(getApplicationContext(), recyclerViewListProduct,
                            categoryName, linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        instance = this;
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
//                searchView.setText("");
            } else {
                String barcode = intentResult.getContents().trim() + "!@#$%";
                selectProductController.getListProduct(barcode, recyclerViewListProduct,
                        linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
//                searchView.setQuery(barcode, false);
                searchView.clearFocus();
            }
        }
    }

    public void addNewProduct(View view) {
        selectProductController.tranIntent(SelectProductActivity.this, CreateProductActivity.class);
    }

    public void transferToListItemInOrder(List<Product> listSelectedProductCurrent) {
        List<Product> listSelectedProductInOrder = new ArrayList<>();
        List<Product> listSelectedProductWarehouse = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLEBACK");
        if (bundle != null) {
            listSelectedProductInOrder = (ArrayList<Product>) bundle.getSerializable("listSelectedProductInOrder");
            listSelectedProductWarehouse = (ArrayList<Product>) bundle.getSerializable("listSelectedProductWarehouse");
        }
        //add product to new list
        //item in new list contain name, id, Unit(name, price, quantity)
        if (listSelectedProductCurrent != null)
            for (Product product : listSelectedProductCurrent
            ) {
                Product productInOrder = new Product();
                productInOrder.setProductId(product.getProductId());
                productInOrder.setProductName(product.getProductName());
                productInOrder.setUnits(getMinUnit(product.getUnits()));
                //add to 2 lists
                listSelectedProductInOrder.add(productInOrder);
                listSelectedProductWarehouse.add(product);
            }

        Intent intent2 = new Intent(SelectProductActivity.this, ListItemInOrderActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
        args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
        intent2.putExtra("BUNDLE", args2);
        startActivity(intent2);
    }

    public static SelectProductActivity getInstance() {
        return instance;
    }

    public void tranToListItemsInOrder(View view) {
        transferToListItemInOrder(selectProductController.getListSelectedProduct());
    }

    public static List<Unit> getMinUnit(List<Unit> unitList) {
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectProductActivity.this, MainActivity.class);
        startActivity(intent);
    }

}