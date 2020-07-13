package com.koit.capstonproject_version_1.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hsalf.smileyrating.SmileyRating;
import com.koit.capstonproject_version_1.Adapter.ConvertRateRecyclerAdapter;
import com.koit.capstonproject_version_1.Adapter.UnitRecyclerAdapter;
import com.koit.capstonproject_version_1.Controller.DetailProductController;
import com.koit.capstonproject_version_1.Controller.UpdateProductController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity   {
    private ImageView productImage;
    private EditText edProductName, edBarcode, edDescription;
    private TextView categoryName,tvUnitQuantity,tvConvertRate;
    private RecyclerView recyclerUnits, recyclerConvertRate;
    private Switch switchActive;
    private Spinner spinnerUnit;
    private Product product;
    private Unit unit;
    private Button btnUpdateProduct;
    private Button btnDeleteProduct;
    private DetailProductController detailProductController;
    private TextView tvToolbarTitle;
    public static  final int REQUEST_QUANTITY_CODE = 3;

    Category category;
    ArrayList<Unit> unitList;
    UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
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
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                product.removeProduct(userDAO.getUserID(),product.getProductId());
                                unit.removeProductUnits(userDAO.getUserID(),product.getProductId());
                                Intent intent = new Intent(DetailProductActivity.this,ListProductActivity.class);
                                startActivity(intent);
                               Toast.makeText(getApplicationContext(),"Bạn đã xoá thành công sản phẩm",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void actionBtnUpdateProduct(){
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, UpdateProductInformationActivity.class);
                intent.putExtra("product",  product);
                startActivity(intent);
            }
        });
    }
    private void initView(){
        productImage =  findViewById(R.id.productImage);
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
        detailProductController = new DetailProductController();
        tvToolbarTitle.setText("Chi tiết sản phẩm");
        userDAO = new UserDAO();
        unit= new Unit();

    }
    private void getProduct(){
       Intent intent = getIntent();
        product =(Product) intent.getSerializableExtra("product");
        unitList = (ArrayList<Unit>) product.getUnits();
        detailProductController.sortUnitByPrice(unitList);

    }
    private void setProductInformation(){

       detailProductController.setProductImageView(productImage,product);
        edBarcode.setText(product.getBarcode());
        edProductName.setText(product.getProductName());
        edDescription.setText(product.getProductDescription());
        categoryName.setText("Loại sản phẩm: "+ product.getCategoryName());

        recyclerUnits.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUnits.setLayoutManager(linearLayoutManager);

        UnitRecyclerAdapter unitRecyclerAdapter = new UnitRecyclerAdapter(unitList,getApplicationContext());
        recyclerUnits.setAdapter(unitRecyclerAdapter);

        if (unitList.size() <2 ) tvConvertRate.setVisibility(View.INVISIBLE);
       recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerConvertRate.setLayoutManager(linearLayoutManager1);

        ConvertRateRecyclerAdapter convertRateRecyclerAdapter = new ConvertRateRecyclerAdapter(unitList,getApplicationContext());
        recyclerConvertRate.setAdapter(convertRateRecyclerAdapter);

        setSpinnerUnit();

    if (product.isActive())   switchActive.setChecked(true);
    switchActive.setEnabled(false);
    }

    private void setSpinnerUnit(){
        ArrayAdapter<Unit> adapter =
                new ArrayAdapter<Unit>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, unitList);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvUnitQuantity.setText(unitList.get(position).getUnitQuantity() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void back(View view) {
        onBackPressed();
    }
    public  void addProductQuantity(View view){
        Intent intent = new Intent(DetailProductActivity.this, AddQuantityActivity.class);
        intent.putExtra("product",product);

        startActivityForResult(intent, REQUEST_QUANTITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_QUANTITY_CODE && resultCode == Activity.RESULT_OK){
            product = (Product) data.getSerializableExtra("product");
            unitList = (ArrayList<Unit>) product.getUnits();
            setSpinnerUnit();
//            Log.d("category", category);
//            tvCategory.setText(category);
        }
    }
}