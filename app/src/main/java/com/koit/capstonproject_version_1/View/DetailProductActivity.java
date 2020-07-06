package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsalf.smileyrating.SmileyRating;
import com.koit.capstonproject_version_1.Adapter.ConvertRateRecyclerAdapter;
import com.koit.capstonproject_version_1.Adapter.UnitRecyclerAdapter;
import com.koit.capstonproject_version_1.Controller.UpdateProductController;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity   {
    private ImageView productImage;
    private EditText edProductName, edBarcode, edDescription;
    private TextView categoryName,tvUnitQuantity;
    private RecyclerView recyclerUnits, recyclerConvertRate;
    private Switch switchActive;
    private Spinner spinnerUnit;
    private Product product;
    private Button btnUpdateProduct;
    private Button btnDeleteProduct;
    Category category;
    ArrayList<Unit> unitList;

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

            }
        });
    }

    private void actionBtnUpdateProduct(){
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailProductActivity.this, UpdateProductActivity.class);
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
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
    }
    private void getProduct(){
//         Intent intent = getIntent();
//         product =(Product) intent.getSerializableExtra("product");
//         if (product == null) {
//             Unit unit1 = new Unit("GOI", 1, 6000, 200);
//             Unit unit2 = new Unit("THUNG", 30, 160000, 6);
//             Unit unit3 = new Unit("LOC", 6, 30000, 33);
//          unitList = new ArrayList<>();
//             unitList.add(unit2);
//           unitList.add(unit1);
//
//             unitList.add(unit3);
//             Collections.sort(unitList, new Comparator<Unit>() {
//                 @Override
//                 public int compare(Unit o1, Unit o2) {
//                     return (int) (o1.getUnitPrice() - o2.getUnitPrice());
//                 }
//
//
//             });
//             product = new Product("0399271212", "03", "8936017361143", "mì tôm", "omachi sườn", "", "omachi sườn", true, unitList);
//         }
    }
    private void setProductInformation(){
        edBarcode.setText(product.getBarcode());
        edProductName.setText(product.getProductName());
        edDescription.setText(product.getProductDescription());
        categoryName.setText("Loại sản phẩm: "+ product.getCategoryName());
        recyclerUnits.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUnits.setLayoutManager(linearLayoutManager);

        UnitRecyclerAdapter unitRecyclerAdapter = new UnitRecyclerAdapter(unitList,getApplicationContext());
        recyclerUnits.setAdapter(unitRecyclerAdapter);


        recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerConvertRate.setLayoutManager(linearLayoutManager1);

        ConvertRateRecyclerAdapter convertRateRecyclerAdapter = new ConvertRateRecyclerAdapter(unitList,getApplicationContext());
        recyclerConvertRate.setAdapter(convertRateRecyclerAdapter);

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
    if (product.isActive())   switchActive.setChecked(true);
    switchActive.setEnabled(false);
    }



}