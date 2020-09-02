package com.koit.capstonproject_version_1.view;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.controller.CameraController;
import com.koit.capstonproject_version_1.controller.ListCategoryController;
import com.koit.capstonproject_version_1.controller.CreateOrderController;
import com.koit.capstonproject_version_1.controller.OrderHistoryController;
import com.koit.capstonproject_version_1.controller.SelectProductController;
import com.koit.capstonproject_version_1.controller.SwipeController;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.StatusBar;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.Helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    private CheckBox checkBoxSelectMultiProduct;
    private LinearLayout layoutButton;
    private static SelectProductActivity instance;
    private Toolbar toolbar_top;
    private TextView orderDraftQuantity;
    private RelativeLayout cart_badge;
    private OrderHistoryController orderHistoryController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_select_product);
        initView();
        setOrderDraftQuantity();
        searchView.requestFocus();
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

        selectProductController = new SelectProductController(this);

        selectProductController.getListProduct(null, recyclerViewListProduct,
                linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectProductController.getListProduct(newText, recyclerViewListProduct,
                        linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
                return false;
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
                    selectProductController.getListProduct(recyclerViewListProduct,
                            categoryName, linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        orderHistoryController = new OrderHistoryController(this);
        orderHistoryController.setTotalDraftOrder(cart_badge, orderDraftQuantity);

        instance = this;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLEBACK");
        if (bundle != null) {
            if (bundle.getString("barcode") != null) {
                searhByBarcode(bundle.getString("barcode"));
//                searchView.clearFocus();
            }
            if (!bundle.getBoolean("isSearchText")) {
                toolbar_top.setVisibility(View.VISIBLE);
                searchView.clearFocus();

            }
            toolbar_top.setVisibility(View.GONE);
        } else {
            toolbar_top.setVisibility(View.VISIBLE);
            searchView.clearFocus();
        }
    }

    private void initView() {
        category_Spinner = findViewById(R.id.category_Spinner);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmptyProduct);
        layoutSearch = findViewById(R.id.layoutSearch);
        layoutNotFoundItem = findViewById(R.id.layout_not_found_item);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        pBarList = findViewById(R.id.pBarList);
        imgbtnBarcodeInList = findViewById(R.id.imgbtnBarcodeInList);
        searchView = findViewById(R.id.searchViewInList);
        orderDraftQuantity = findViewById(R.id.orderDraftQuantity);
        cart_badge = findViewById(R.id.cart_badge);
        checkBoxSelectMultiProduct = findViewById(R.id.checkBoxSelectMultiProduct);
        layoutButton = findViewById(R.id.layoutBtnSelectedProduct);
        toolbar_top = findViewById(R.id.toolbar_top);
    }

    private void setOrderDraftQuantity() {
        orderDraftQuantity.setText("0");
    }

    //icon back
    public void back(View view) {
        backToMain();
    }

    private void backToMain() {
        Intent intentget = getIntent();
        Bundle bundle = intentget.getBundleExtra("BUNDLEBACK");
        if (bundle != null) {
            final List<Product> listSelectedProductInOrder = (ArrayList<Product>) bundle.getSerializable("listSelectedProductInOrder");
            if (listSelectedProductInOrder.size() > 0) {
                //show dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn có muốn lưu đơn tạm không?")
                        .setPositiveButton("Lưu đơn", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //remove item on right click
                                CreateOrderController createOrderController = new CreateOrderController(SelectProductActivity.this);
                                createOrderController.insertDraftOrder(listSelectedProductInOrder);
                                Intent intent = new Intent(SelectProductActivity.this, MainActivity.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Hủy đơn", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(SelectProductActivity.this, MainActivity.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();
            } else {
                tranToMain();
            }
        } else
            tranToMain();
    }

    //transfer intent
    private void tranToMain() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        this.startActivity(intent);
    }

    public void searchByBarcodeInOrder() {
        CameraController cameraController = new CameraController(SelectProductActivity.this);
        cameraController.askCameraPermission(CreateProductActivity.BARCODE_PER_CODE);
    }

    public void searchByBarcode(View view) {
        searchByBarcodeInOrder();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
//                searchView.setText("");
            } else {
                String barcode = intentResult.getContents().trim() + "Se!@#";
                Log.d("barcodeSelect", barcode);
                searhByBarcode(barcode);
                //    searchView.setQuery(barcode.substring(0, barcode.length() - 5), false);
//                searchView.clearFocus();
            }
        }
    }

    public void searhByBarcode(String barcode) {
        selectProductController.getListProduct(barcode, recyclerViewListProduct,
                linearLayoutEmpty, layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, checkBoxSelectMultiProduct, layoutButton);
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
                if (!Helper.getInstance().addProductToListInOrder(listSelectedProductInOrder, product)) {
                } else {
                    //add new product
                    listSelectedProductWarehouse.add(product);
                }
//                listSelectedProductInOrder.add(productInOrder);
            }
        Intent intent2 = new Intent(SelectProductActivity.this, CreateOrderActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
        args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
        intent2.putExtra("BUNDLE", args2);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(this.getlayoutSearch(), "layoutSearch");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(intent2, options.toBundle());
    }

    private LinearLayout getlayoutSearch() {
        return layoutSearch;
    }

    public static SelectProductActivity getInstance() {
        return instance;
    }

    public void tranToListItemsInOrder(View view) {
        transferToListItemInOrder(selectProductController.getListSelectedProduct());
    }


    @Override
    public void onBackPressed() {
        backToMain();
    }

    public void transferToDraftOrder(View view) {
        Intent intentget = getIntent();
        Bundle bundle = intentget.getBundleExtra("BUNDLEBACK");
        if (bundle != null) {
            final List<Product> listSelectedProductInOrder = (ArrayList<Product>) bundle.getSerializable("listSelectedProductInOrder");
            if (listSelectedProductInOrder.size() > 0) {
                //show dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn sẽ chuyển sang danh sách đơn tạm.\n Bạn có muốn lưu đơn này không? ")
                        .setPositiveButton("Lưu đơn", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                CreateOrderController createOrderController = new CreateOrderController(SelectProductActivity.this);
                                createOrderController.insertDraftOrder(listSelectedProductInOrder);
                                Intent intent = new Intent(SelectProductActivity.this, DraftOrderActivity.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Hủy đơn", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(SelectProductActivity.this, DraftOrderActivity.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();
            } else {
                intentToDraftOrder();
            }
        } else {
            intentToDraftOrder();
        }
    }

    private void intentToDraftOrder() {
        Intent intent = new Intent(SelectProductActivity.this, DraftOrderActivity.class);
        Bundle args2 = new Bundle();
        args2.putBoolean("fromSelect", true);
        intent.putExtra("BUNDLE", args2);
        startActivity(intent);

    }
}