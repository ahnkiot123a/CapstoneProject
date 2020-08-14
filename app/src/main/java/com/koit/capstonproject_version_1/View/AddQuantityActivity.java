package com.koit.capstonproject_version_1.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.AddProductQuantityAdapter;
import com.koit.capstonproject_version_1.Controller.AddProductQuantityController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class AddQuantityActivity extends AppCompatActivity {
    private TextView tvToolbarTitle;
    private Product currentProduct;
    private RecyclerView recyclerUnitQuantity;
    private Button btnAddProductQuantiy;
    private List<Unit> unitList;
    private AddProductQuantityAdapter addProductQuantityAdapter;
    private AddProductQuantityController addProductQuantityController;
    UserDAO userDAO;
    Unit unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_add_quantity);
        initView();
        tvToolbarTitle.setText("Thêm số lượng sản phẩm");
        setProductInformation();
        buildRyclerUnitQuantity();
        actionBtnAddProductQuantiy();
    }

    private void actionBtnAddProductQuantiy() {
        btnAddProductQuantiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Unit> addUnitList = getListUnitAdd();
                for (int i = 0; i < unitList.size(); i++) {
                    long quantity = unitList.get(i).getUnitQuantity() + addUnitList.get(i).getUnitQuantity();
                    unitList.get(i).setUnitQuantity(quantity);
                }
                addProductQuantityController.calInventoryByUnit(unitList);
                addProductQuantityController.addUnitsToFireBase(currentProduct, unitList);
                currentProduct.setUnits(unitList);
                Intent intent = new Intent();
                intent.putExtra("product", currentProduct);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void buildRyclerUnitQuantity() {
        recyclerUnitQuantity.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUnitQuantity.setLayoutManager(linearLayoutManager);
        addProductQuantityAdapter = new AddProductQuantityAdapter(unitList, this);
        recyclerUnitQuantity.setAdapter(addProductQuantityAdapter);
    }

    private List<Unit> getListUnitAdd() {
        List<Unit> list = new ArrayList<>();
        for (int i = 0; i < addProductQuantityAdapter.getItemCount(); i++) {
            AddProductQuantityAdapter.ViewHolder viewHolder = (AddProductQuantityAdapter.ViewHolder) recyclerUnitQuantity.findViewHolderForAdapterPosition(i);
            String unitName = viewHolder.getTvUnitName().getText().toString().trim();
            String unitQuantity = viewHolder.getEtProductQuantity().getText().toString().trim();
            if (unitQuantity.trim().equals("")) unitQuantity = "0";
            if (!unitName.isEmpty() && !unitQuantity.isEmpty()) {
                Unit unit = new Unit();
                unit.setUnitName(unitName);
                unit.setUnitQuantity(Long.parseLong(unitQuantity));
                list.add(unit);
            }
        }
        return list;
    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        recyclerUnitQuantity = findViewById(R.id.recyclerUnitQuantity);
        btnAddProductQuantiy = findViewById(R.id.btnAddProductQuantiy);
        addProductQuantityController = new AddProductQuantityController();
        userDAO = new UserDAO();
        unit = new Unit();
    }

    private void setProductInformation() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra("product");
        unitList = currentProduct.getUnits();
//        Collections.reverse(unitList);
        addProductQuantityController.convertUnitList(unitList);
        //addProductQuantityController.convertUnitList2(unitList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void back(View view) {
        onBackPressed();
    }

}