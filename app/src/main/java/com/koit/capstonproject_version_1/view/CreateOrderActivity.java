package com.koit.capstonproject_version_1.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.controller.CreateOrderController;
import com.koit.capstonproject_version_1.controller.InputController;
import com.koit.capstonproject_version_1.controller.ListItemInOrderController;
import com.koit.capstonproject_version_1.controller.RandomStringController;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.helper.MoneyEditText;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.model.dao.OrderHistoryDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CreateOrderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private RecyclerView recyclerViewListProduct;
    private SearchView searchViewInList;
    private TextView tvTotalQuantity;
    private TextView tvTotalPrice;
    private List<Product> listSelectedProductWarehouse = new ArrayList<>();
    private List<Product> listSelectedProductInOrder = new ArrayList<>();
    private ItemInOrderAdapter itemAdapter;
    private EditText productName;
    public static final int BARCODE_PER_CODE = 101;
    private MoneyEditText price;
    private EditText quantity;
    private Button cancleBtn;
    private Button addBtn;
    private EditText unitName;
    private LinearLayout LinearUpper;
    private LinearLayout layoutSearch;
    private CaptureManager captureManager;
    private Toolbar toolbar;
    private DecoratedBarcodeView barcodeView;
    private Bundle savedInstanceState2;
    private ListItemInOrderController listItemInOrderController;
    private ZXingScannerView mScannerView;
    private int mCameraId = -1;
    private ViewGroup contentFrame;
    private ImageView imgbtnBarcode;
    private boolean isCameraActive;
    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        savedInstanceState2 = savedInstanceState;
        setContentView(R.layout.activity_list_item_in_order);
        initView();
        searchViewInList.clearFocus();
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));
        getIntentListProduct();
        listItemInOrderController.getListProduct("", recyclerViewListProduct, tvTotalQuantity, tvTotalPrice);
        //swipe
        searchViewInList.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                backToPrevious(true);
            }
        });
        listItemInOrderController.setupRecyclerView(recyclerViewListProduct, tvTotalQuantity, tvTotalPrice);
        isFirstTime = true;
        isCameraActive = false;
    }

    private void initView() {
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        searchViewInList = findViewById(R.id.searchViewInList);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        imgbtnBarcode = findViewById(R.id.imgbtnBarcode);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        LinearUpper = findViewById(R.id.LinearUpper);
        layoutSearch = findViewById(R.id.layoutSearchInOrder);
        toolbar = findViewById(R.id.toolbar_top);
        contentFrame = findViewById(R.id.zxing_barcode_scanner);
//        mScannerView = new ZXingScannerView(this);
//        contentFrame.addView(mScannerView);
    }


    private void getIntentListProduct() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        if (args != null) {
            listSelectedProductWarehouse = (ArrayList<Product>) args.getSerializable("listSelectedProductWarehouse");
            listSelectedProductInOrder = (ArrayList<Product>) args.getSerializable("listSelectedProductInOrder");

            if (listSelectedProductInOrder == null || listSelectedProductWarehouse == null) {
                listSelectedProductInOrder = new ArrayList<>();
                listSelectedProductWarehouse = new ArrayList<>();
            }

            String invoiceId = (String) args.get("invoiceId");
            //chuyen tu trang don tam sang
            if (invoiceId != null) {

                Log.d("LIOlistSelected", listSelectedProductInOrder.toString());
                Log.d("LIOWarehouse", listSelectedProductWarehouse.toString());

                listItemInOrderController = new ListItemInOrderController(this, listSelectedProductInOrder,
                        listSelectedProductWarehouse);
                listItemInOrderController.getListProductInDraftOrder(invoiceId);
                //delete don tam
                OrderHistoryDAO invoiceHistoryDAO = new OrderHistoryDAO();
                invoiceHistoryDAO.deleteDraftOrder(invoiceId);
            } else {
                listItemInOrderController = new ListItemInOrderController(this, listSelectedProductInOrder,
                        listSelectedProductWarehouse);
            }
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//            recyclerViewListProduct.setLayoutManager(layoutManager);
//            itemAdapter = new ItemInOrderAdapter(this, R.layout.item_layout_in_order, listSelectedProductWarehouse,
//                    tvTotalQuantity, tvTotalPrice, listSelectedProductInOrder);
//            recyclerViewListProduct.setAdapter(itemAdapter);
//            itemAdapter.notifyDataSetChanged();
        }
    }


    public void back(View view) {
        backToPrevious(false);
    }

    private void backToPrevious(boolean isSearchText) {
        Intent intent = new Intent(this, SelectProductActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
        args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
        args2.putBoolean("isSearchText", isSearchText);
        intent.putExtra("BUNDLEBACK", args2);
//        Pair[] pairs = new Pair[1];
//        pairs[0] = new Pair<View, String>(this.getlayoutSearch(), "layoutSearch");
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(intent);
    }

    private LinearLayout getlayoutSearch() {
        return layoutSearch;
    }

    public void searchByBarcode(View view) {
        //hide and show scan layout
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //search by barcode
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String barcode = intentResult.getContents().trim() + "CO!@#";
                listItemInOrderController.getListProduct(barcode, recyclerViewListProduct, tvTotalQuantity, tvTotalPrice);
                Log.d("ResultBarcode", barcode);
//                Intent intent2 = new Intent(CreateOrderActivity.this, SelectProductActivity.class);
//                Bundle args2 = new Bundle();
//                args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
//                args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
//                args2.putString("barcode", barcode);
//                intent2.putExtra("BUNDLEBACK", args2);
//                startActivity(intent2);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstTime) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera(mCameraId);
        }
        //to set flash
//        mScannerView.setFlash(true);
        //to set autoFocus
//        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isFirstTime) {
            mScannerView.stopCamera();
        }// Stop camera on pause
    }

    //scan resulta
    @Override
    public void handleResult(Result rawResult) {
        try {
            //beep sound
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP, 200);

            listItemInOrderController.getListProduct(rawResult.getText() + "CO!@#", recyclerViewListProduct, tvTotalQuantity, tvTotalPrice);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera(mCameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNoneListedProduct(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(CreateOrderActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.add_nonlistedproduct, null);
        // Create a AlertDialog Builder.
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateOrderActivity.this);

        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Nhập sản phẩm bên ngoài");
        alertDialogBuilder.setIcon(R.drawable.icons8_add_48px_3);
        alertDialogBuilder.setCancelable(false);

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);
        // Get user input edittext and button ui controls in the popup dialog.
        productName = popupInputDialogView.findViewById(R.id.productName);
        price = popupInputDialogView.findViewById(R.id.productPrice);
        quantity = popupInputDialogView.findViewById(R.id.productQuantity);
        unitName = popupInputDialogView.findViewById(R.id.unitName);
        cancleBtn = popupInputDialogView.findViewById(R.id.button_cancel);
        addBtn = popupInputDialogView.findViewById(R.id.button_add_product);
        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                price.setText(Money.getInstance().formatVN(Long.parseLong(price.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // When user click the save user data button in the popup dialog.
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pName = "", pUnitName = "";
                long pPrice = 0;
                int pQuantity = 0;
                if ((productName.getText().toString().trim().length() > 100) || (productName.getText().toString().trim().length() == 0)) {
                    //show error
                    productName.setError("Tên sản phẩm không hợp lệ");
                    productName.requestFocus();
                } else if (price.getText().toString().trim().isEmpty()) {
                    //show error
                    price.setError("Số tiền không hợp lệ");
                    price.requestFocus();

                } else if (!InputController.checkValidNumber(quantity.getText().toString(), 4)) {
                    //show error
                    quantity.setError("Số lượng không hợp lệ");
                    quantity.requestFocus();
                } else if ((unitName.getText().toString().trim().length() > 100)) {
                    unitName.setError("Tên đơn vị không hợp lệ");
                    unitName.requestFocus();
                } else {
                    try {
                        pName = productName.getText().toString();
                        pPrice =Money.getInstance().reFormatVN(price.getText().toString());
                        pQuantity = Integer.parseInt(quantity.getText().toString());
                        pUnitName = unitName.getText().toString();
                    } catch (Exception e) {

                    }
                    // Create NonelistedProduct
                    List<Unit> unitList = new ArrayList<>();
                    Unit unit = new Unit();
                    unit.setUnitId(RandomStringController.randomID());
                    unit.setUnitPrice(pPrice);
                    unit.setUnitQuantity(pQuantity);
                    unit.setUnitName(pUnitName);
                    unitList.add(unit);

                    Product product = new Product();
                    product.setProductId("nonListedProduct" + RandomStringController.randomID());
                    product.setProductName(pName);
                    product.setUnits(unitList);
                    Log.d("checkAddProduct", product.toString());
                    listSelectedProductInOrder.add(product);
                    Log.d("checkListInOrder", listSelectedProductInOrder.toString());
                    listSelectedProductWarehouse.add(product);
                    Log.d("checkListWareHouse", listSelectedProductWarehouse.toString());

                    listItemInOrderController.getListProduct(null, recyclerViewListProduct, tvTotalQuantity, tvTotalPrice);
                    alertDialog.cancel();
                }
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

    }


    @Override
    public void onBackPressed() {
        backToPrevious(false);
    }


    public void transferToSelectDebtor(View view) {
        if (listSelectedProductInOrder.size() > 0) {
            Intent intent = new Intent(this, SelectDebtorActivity.class);
            Bundle args2 = new Bundle();
            args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
            args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
            intent.putExtra("BUNDLE", args2);
            startActivity(intent);
        } else {
            callDialogEmpty();
        }
    }

    private void callDialogEmpty() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn chưa chọn sản phẩm nào!")

                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void transferToPayment(View view) {
        if (listSelectedProductInOrder.size() > 0) {
            Intent intent = new Intent(this, CustomerPayActivity.class);
            Bundle args2 = new Bundle();
            args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
            args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
            intent.putExtra("BUNDLE", args2);
            startActivity(intent);
        } else {
            callDialogEmpty();
        }
    }

    public void backToPrevious(View view) {
        backToPrevious(true);
    }

    public void drraftOrder(View view) {
        if (listSelectedProductInOrder.size() > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có muốn lưu đơn tạm không?")
                    .setPositiveButton("Lưu đơn", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //remove item on right click
                            CreateOrderController createOrderController = new CreateOrderController(CreateOrderActivity.this);
                            createOrderController.insertDraftOrder(listSelectedProductInOrder);
                            Intent intent = new Intent(CreateOrderActivity.this, SelectProductActivity.class);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Hủy đơn", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(CreateOrderActivity.this, SelectProductActivity.class);
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
            backToPrevious(false);
        }

    }


    public void showHideBarcode(View view) {
        if (isFirstTime) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
            mScannerView = new ZXingScannerView(this);
            contentFrame.addView(mScannerView);
            mScannerView.setResultHandler(this);
            isFirstTime = false;
        }
        if (isCameraActive) {
            //hide and stop scan
            Log.d("contentFramLI", "hide");
            contentFrame.setVisibility(View.GONE);
            imgbtnBarcode.setImageResource(R.drawable.icons8_no_barcode_100px);
            mScannerView.stopCamera();
            isCameraActive = false;
        } else {
            //show and start scan
            Log.d("contentFramLI", "show");
            contentFrame.setVisibility(View.VISIBLE);
            mScannerView.startCamera(mCameraId);
            imgbtnBarcode.setImageResource(R.drawable.icons8_barcode_100px);
            isCameraActive = true;
        }
    }
}