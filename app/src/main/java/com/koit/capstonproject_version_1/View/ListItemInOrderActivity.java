package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koit.capstonproject_version_1.Adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.Controller.CameraController;
import com.koit.capstonproject_version_1.Controller.OrderSwipeController;
import com.koit.capstonproject_version_1.Controller.OrderSwipeControllerActions;
import com.koit.capstonproject_version_1.Controller.RandomStringController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemInOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewListProduct;
    private SearchView searchViewInList;
    private TextView tvTotalQuantity;
    private TextView tvTotalPrice;
    private List<Product> listSelectedProductWarehouse = new ArrayList<>();
    private List<Product> listSelectedProductInOrder = new ArrayList<>();
    OrderSwipeController orderSwipeController = null;
    ItemInOrderAdapter itemAdapter;
    private View popupInputDialogView = null;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private Button cancleBtn;
    private Button addBtn;
    private EditText unitName;
    LinearLayout LinearUpper;
    int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_list_item_in_order);
        recyclerViewListProduct = findViewById(R.id.recyclerViewListProduct);
        searchViewInList = findViewById(R.id.searchViewInList);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        LinearUpper = findViewById(R.id.LinearUpper);
        searchViewInList.clearFocus();
        recyclerViewListProduct.setHasFixedSize(true);
        recyclerViewListProduct.setLayoutManager(new LinearLayoutManager(this));
        getListProduct();
        //swipe
        setupRecyclerView(recyclerViewListProduct, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchViewInList.setQuery("", false);
        LinearUpper.requestFocus();
    }

    private void getListProduct() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        listSelectedProductWarehouse = (ArrayList<Product>) args.getSerializable("listSelectedProductWarehouse");
        listSelectedProductInOrder = (ArrayList<Product>) args.getSerializable("listSelectedProductInOrder");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        itemAdapter = new ItemInOrderAdapter(this, R.layout.item_layout_in_order, listSelectedProductWarehouse,
                tvTotalQuantity, tvTotalPrice, listSelectedProductInOrder);
        recyclerViewListProduct.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }


    public void back(View view) {
        backToPrevious();
    }

    private void backToPrevious() {
        Intent intent = new Intent(this, SelectProductActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
        args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
        intent.putExtra("BUNDLEBACK", args2);
        startActivity(intent);
    }

    public void searchByBarcode(View view) {
//        SelectProductActivity.getInstance().searchByBarcodeInOrder();
        CameraController cameraController = new CameraController(ListItemInOrderActivity.this);
        cameraController.askCameraPermission(CreateProductActivity.BARCODE_PER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //search by barcode
//        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
//            if (resultCode == SelectProductActivity.RESULT_OK) {
//                Product product = (Product) data.getBundleExtra("BUNDLE_PRODUCT").getSerializable("product");
//                listSelectedProductWarehouse.add(product);
//                Product productInOrder = new Product();
//                productInOrder.setProductId(product.getProductId());
//                productInOrder.setProductName(product.getProductName());
//                productInOrder.setUnits(SelectProductActivity.getMinUnit(product.getUnits()));
//                listSelectedProductInOrder.add(productInOrder);
//            }
//            if (resultCode == SelectProductActivity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
        //scan
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String barcode = intentResult.getContents().trim() + "!@#$%";
                Intent intent2 = new Intent(ListItemInOrderActivity.this, SelectProductActivity.class);
                Bundle args2 = new Bundle();
                args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
                args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
                args2.putString("barcode", barcode);
                intent2.putExtra("BUNDLEBACK", args2);
                startActivity(intent2);
            }
        }
    }

    public void addNoneListedProduct(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(ListItemInOrderActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.add_nonlistedproduct, null);
        // Create a AlertDialog Builder.
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListItemInOrderActivity.this);
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
                } else if (!checkValidNumber(price.getText().toString(), 9)) {
                    //show error
                    price.setError("Số tiền không hợp lệ");
                    price.requestFocus();

                } else if (!checkValidNumber(quantity.getText().toString(), 4)) {
                    //show error
                    quantity.setError("Số lượng không hợp lệ");
                    quantity.requestFocus();
                } else if ((unitName.getText().toString().trim().length() > 100)) {
                    unitName.setError("Tên đơn vị không hợp lệ");
                    unitName.requestFocus();
                } else {
                    try {
                        pName = productName.getText().toString();
                        pPrice = Long.parseLong(price.getText().toString());
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

                    itemAdapter.notifyDataSetChanged();
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

    public boolean checkValidNumber(String numStr, int maxNumOfDigits) {
        numStr = numStr.trim();
        if (numStr.trim().length() > maxNumOfDigits) return false;
        else if (!numStr.matches("\\d+")) return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        backToPrevious();
    }

    public void setupRecyclerView(RecyclerView recyclerView, final ListItemInOrderActivity listItemInOrderActivity) {
        orderSwipeController = new OrderSwipeController(new OrderSwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //remove item in 2 list
                listSelectedProductInOrder.remove(position);
                listSelectedProductWarehouse.remove(position);
                itemAdapter.notifyItemRemoved(position);

                tvTotalQuantity.setText(ItemInOrderAdapter.getTotalQuantity(listSelectedProductInOrder) + "");
                tvTotalPrice.setText(ItemInOrderAdapter.getTotalPrice(listSelectedProductInOrder) + "");
//                itemAdapter.notifyItemRangeChanged(position, itemAdapter.getItemCount());
            }

        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(orderSwipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                orderSwipeController.onDraw(c);
            }
        });
    }

    public void transferToSelectDebtor(View view) {
        Intent intent = new Intent(this, SelectDebtorActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
        args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
        intent.putExtra("BUNDLE", args2);
        startActivity(intent);
    }

    public void transferToPayment(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        Bundle args2 = new Bundle();
        args2.putSerializable("listSelectedProductInOrder", (Serializable) listSelectedProductInOrder);
        args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
        intent.putExtra("BUNDLE", args2);
        startActivity(intent);
    }
}