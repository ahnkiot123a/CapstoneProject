package com.koit.capstonproject_version_1.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.EditProductQuantityAdapter;
import com.koit.capstonproject_version_1.Controller.EditProductQuantityController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class EditProductQuantityActivity extends AppCompatActivity {
    private TextView tvToolbarTitle;
    private Product currentProduct;
    private RecyclerView recyclerUnitQuantity;
    private Button btnAddProductQuantiy;
    private List<Unit> unitList;
    private EditProductQuantityAdapter editProductQuantityAdapter;
    private EditProductQuantityController editProductQuantityController;
    UserDAO userDAO;
    Unit unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_add_quantity);
        initView();
        tvToolbarTitle.setText("Chỉnh sửa số lượng sản phẩm");
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
                    if (quantity < 0) quantity = 0;
                    unitList.get(i).setUnitQuantity(quantity);
                }
                editProductQuantityController.calInventoryByUnit(unitList);
                editProductQuantityController.addUnitsToFireBase(currentProduct, unitList);
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
        editProductQuantityAdapter = new EditProductQuantityAdapter(unitList, this);
        recyclerUnitQuantity.setAdapter(editProductQuantityAdapter);
    }

    private List<Unit> getListUnitAdd() {
        List<Unit> list = new ArrayList<>();
        for (int i = 0; i < editProductQuantityAdapter.getItemCount(); i++) {
            EditProductQuantityAdapter.ViewHolder viewHolder = (EditProductQuantityAdapter.ViewHolder) recyclerUnitQuantity.findViewHolderForAdapterPosition(i);
            String unitName = viewHolder.getTvUnitName().getText().toString().trim();
            String unitQuantity = viewHolder.getEtProductQuantity().getText().toString().trim();
            String spinnerChooseType = viewHolder.getSpinnerChooseType().getSelectedItem().toString();

            if (unitQuantity.trim().equals("")) unitQuantity = "0";
            if (!unitName.isEmpty() && !unitQuantity.isEmpty()) {
                Unit unit = new Unit();
                unit.setUnitName(unitName);
                long quantity = 0;
                if (spinnerChooseType.equals("Thêm")) {
                    quantity = Long.parseLong(unitQuantity);
                } else {
                    quantity = 0 - Long.parseLong(unitQuantity);
                }
                unit.setUnitQuantity(quantity);
                list.add(unit);
            }
        }
        return list;
    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        recyclerUnitQuantity = findViewById(R.id.recyclerUnitQuantity);
        btnAddProductQuantiy = findViewById(R.id.btnAddProductQuantiy);
        editProductQuantityController = new EditProductQuantityController();
        userDAO = new UserDAO();
        unit = new Unit();
    }

    private void setProductInformation() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra("product");
        unitList = currentProduct.getUnits();
//        Collections.reverse(unitList);
        editProductQuantityController.convertUnitList(unitList);
        //editProductQuantityController.convertUnitList2(unitList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void back(View view) {
        onBackPressed();
    }

}