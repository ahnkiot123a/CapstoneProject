package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.Controller.OrderSwipeController;
import com.koit.capstonproject_version_1.Controller.OrderSwipeControllerActions;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        //swipe
        setupRecyclerView(recyclerViewListProduct, this);

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
    }

    public void addNoneListedProduct(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(ListItemInOrderActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.add_nonlistedproduct, null);
        // Create a AlertDialog Builder.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListItemInOrderActivity.this);
        // Set title, icon, can not cancel properties.
        alertDialogBuilder.setTitle("Nhập sản phẩm bên ngoài");
        alertDialogBuilder.setIcon(R.drawable.icons8_add_48px_3);
        alertDialogBuilder.setCancelable(true);

        // Init popup dialog view and it's ui controls.
        initPopupViewControls();

        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialogBuilder.setView(popupInputDialogView);
    }
    private void initPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(ListItemInOrderActivity.this);

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.add_nonlistedproduct, null);

        // Get user input edittext and button ui controls in the popup dialog.
        productName =  popupInputDialogView.findViewById(R.id.productName);
        price =  popupInputDialogView.findViewById(R.id.productPrice);
        quantity =  popupInputDialogView.findViewById(R.id.productQuantity);
        cancleBtn = popupInputDialogView.findViewById(R.id.button_cancel);
        addBtn = popupInputDialogView.findViewById(R.id.button_add_product);

        // Display values from the main activity list view in user input edittext.
//        initEditTextUserDataInPopupDialog();
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
                Log.d("lectedProductInOrder", listSelectedProductInOrder.toString());
                itemAdapter.notifyItemRemoved(position);
                tvTotalQuantity.setText(ItemInOrderAdapter.getTotalQuantity(listSelectedProductInOrder)+"");
                tvTotalPrice.setText(ItemInOrderAdapter.getTotalPrice(listSelectedProductInOrder)+"");
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
        Log.d("list11",listSelectedProductInOrder.toString());
        Log.d("list12",listSelectedProductWarehouse.toString());
        intent.putExtra("BUNDLE", args2);
        startActivity(intent);
    }
}