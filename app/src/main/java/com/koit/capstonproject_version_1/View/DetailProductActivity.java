package com.koit.capstonproject_version_1.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koit.capstonproject_version_1.Controller.DetailProductController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView productImage;
    private EditText edProductName, edBarcode, edDescription;
    private TextView categoryName, tvUnitQuantity, tvConvertRate;
    private RecyclerView recyclerUnits, recyclerConvertRate;
    private Switch switchActive;
    private Spinner spinnerUnit;
    private Product product;
    private Button btnUpdateProduct;
    private Button btnDeleteProduct;
    private DetailProductController detailProductController;
    private TextView tvToolbarTitle;
    public static final int REQUEST_QUANTITY_CODE = 3;
    ArrayList<Unit> unitList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_detail_product);
        initView();
        detailProductController = new DetailProductController(this);
        getProduct();
        setProductInformation();
        actionBtnUpdateProduct();
        actionBtnDeleteProduct();

    }

    private void actionBtnDeleteProduct() {
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailProductActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xoá sản phẩm này không? ")
                        .setCancelable(false)
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                detailProductController.removeProduct(product);
                                finish();
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void actionBtnUpdateProduct() {
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, UpdateProductInformationActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        productImage = findViewById(R.id.productImage);
        edProductName = findViewById(R.id.edProductName);
        edBarcode = findViewById(R.id.edBarcode);
        edDescription = findViewById(R.id.edDescription);
        categoryName = findViewById(R.id.categoryName);
        recyclerUnits = findViewById(R.id.recyclerUnits);
        recyclerConvertRate = findViewById(R.id.recyclerConvertRate);
        switchActive = findViewById(R.id.switchActive);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        tvUnitQuantity = findViewById(R.id.tvUnitQuantity);
        tvConvertRate = findViewById(R.id.tvConvertRate);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Chi tiết sản phẩm");

    }

    private void getProduct() {
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        unitList = (ArrayList<Unit>) product.getUnits();
        detailProductController.sortUnitByPrice(unitList);
    }

    private void setProductInformation() {
        Log.d("productUpdate", product.toString());

        detailProductController.setProductImageView(productImage, product);
        edBarcode.setText(product.getBarcode());
        edProductName.setText(product.getProductName());
        edDescription.setText(product.getProductDescription());
        categoryName.setText("Loại sản phẩm: " + product.getCategoryName());
        if (product.isActive()) switchActive.setChecked(true);
        switchActive.setEnabled(false);

        setRecyclerUnits();
        setRecyclerConvertRate();
        setSpinnerUnit();

    }

    private void setSpinnerUnit() {
        detailProductController.setSpinnerUnit(unitList, spinnerUnit, tvUnitQuantity);
    }

    private void setRecyclerUnits() {
        detailProductController.setRecyclerViewUnits(recyclerUnits, unitList);
    }

    private void setRecyclerConvertRate() {
        detailProductController.setRecyclerConvertRate(unitList, tvConvertRate, recyclerConvertRate);
    }

    public void back(View view) {
        Intent intent = new Intent(DetailProductActivity.this, ListProductActivity.class);
        startActivity(intent);
    }

    public void addProductQuantity(View view) {
        Intent intent = new Intent(DetailProductActivity.this, EditProductQuantityActivity.class);
        intent.putExtra("product", product);
        startActivityForResult(intent, REQUEST_QUANTITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QUANTITY_CODE && resultCode == Activity.RESULT_OK) {
            product = (Product) data.getSerializableExtra("product");
            unitList = (ArrayList<Unit>) product.getUnits();
            setSpinnerUnit();
        }
    }
}