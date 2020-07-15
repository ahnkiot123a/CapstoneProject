package com.koit.capstonproject_version_1.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.AddProductQuantityAdapter;
import com.koit.capstonproject_version_1.Adapter.EditConvertRateAdapter;
import com.koit.capstonproject_version_1.Controller.AddProductQuantityController;
import com.koit.capstonproject_version_1.Controller.CreateProductController;
import com.koit.capstonproject_version_1.Controller.DetailProductController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.CreateProductDAO;

import java.util.ArrayList;
import java.util.List;

public class ConvertRateActivity extends AppCompatActivity {

    private TextView tvToolbarTitle;
    private Product currentProduct;
    private RecyclerView rvConvertRate;
    private RecyclerView rvUnitQuantity;
    private Button btnSuccess;
    private LinearLayout layoutConvertRate;
    private ArrayList<Unit> unitList;
    private EditConvertRateAdapter editConvertRateAdapter;
    private AddProductQuantityAdapter addProductQuantityAdapter;
    private CreateProductController createProductController;
    private AddProductQuantityController addProductQuantityController;
    private DetailProductController detailProductController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_rate);
//        tvToolbarTitle.setText("Thêm sản phẩm");

        initView();

        getProduct();
        buildRecyclerViewUnits();
        buildRecyclerUnitQuantity();

    }



    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnSuccess = findViewById(R.id.btnSuccess);
        rvConvertRate = findViewById(R.id.rvConvertRate);
        rvUnitQuantity = findViewById(R.id.rvQuantityAdd);
        layoutConvertRate = findViewById(R.id.layoutConvertRate);

        createProductController = new CreateProductController(this);
        addProductQuantityController = new AddProductQuantityController();
        detailProductController = new DetailProductController();

    }

    public void addProduct(View view){
        addProductToFirebase();
    }

    private void addProductToFirebase() {
        addUnitToFirebase();
        createProductController.addImageProduct();
        createProductController.addProductInFirebase(currentProduct);
    }

    private void addUnitToFirebase() {
        if(unitList.size() >1){
            setConvertRateFromRv();
        }else{
            unitList.get(0).setConvertRate(1);
        }
        setUnitQuantityFromRV();
        currentProduct.setUnits(unitList);
        addProductQuantityController.addUnitsToFireBase(currentProduct,unitList);
    }


    private void getProduct() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra(CreateProductActivity.NEW_PRODUCT);
        unitList = new ArrayList<>();
        unitList = (ArrayList<Unit>) currentProduct.getUnits();
        detailProductController.sortUnitByPrice(unitList);

    }

    private void buildRecyclerViewUnits() {
        if(unitList.size() >1){
            rvConvertRate.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvConvertRate.setLayoutManager(linearLayoutManager);
            editConvertRateAdapter = new EditConvertRateAdapter( unitList,this);
            rvConvertRate.setAdapter(editConvertRateAdapter);
        }else{
            layoutConvertRate.setVisibility(View.GONE);
        }
    }

    private void buildRecyclerUnitQuantity() {
        rvUnitQuantity.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUnitQuantity.setLayoutManager(linearLayoutManager);
        addProductQuantityAdapter = new AddProductQuantityAdapter( unitList,this);
        rvUnitQuantity.setAdapter(addProductQuantityAdapter);
    }

    private void setConvertRateFromRv() {
        for (int i = 0; i < editConvertRateAdapter.getItemCount(); i++) {
            EditConvertRateAdapter.ViewHolder viewHolder = (EditConvertRateAdapter.ViewHolder) rvConvertRate.findViewHolderForAdapterPosition(i);
            String convertRate = viewHolder.getEtConvertRate().getText().toString().trim();
            long rate = convertRate.isEmpty() ? 1 : Long.parseLong(convertRate);
            unitList.get(i).setConvertRate(rate);
        }
    }

    private void setUnitQuantityFromRV() {
        List<Unit> list = new ArrayList<>();
        for (int i = 0; i < addProductQuantityAdapter.getItemCount(); i++) {
            AddProductQuantityAdapter.ViewHolder viewHolder = (AddProductQuantityAdapter.ViewHolder) rvUnitQuantity.findViewHolderForAdapterPosition(i);
            String unitQuantity = viewHolder.getEtProductQuantity().getText().toString().trim();
            long quantity = unitQuantity.isEmpty() ? 0 : Long.parseLong(unitQuantity);
            unitList.get(i).setUnitQuantity(quantity);
        }
        Log.i("unitList", unitList.toString());
        addProductQuantityController.convertUnitList2(unitList);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
