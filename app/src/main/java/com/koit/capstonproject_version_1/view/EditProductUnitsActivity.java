package com.koit.capstonproject_version_1.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.adapter.EditUnitAdapter;
import com.koit.capstonproject_version_1.controller.EditProductQuantityController;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditProductUnitsActivity extends AppCompatActivity {
    private TextView tvToolbarTitle;
    private Product currentProduct;
    private RecyclerView recyclerUnits;
    private Button btnEditUnits;
    private List<Unit> unitList;
    private EditUnitAdapter editUnitAdapter;
    private EditProductQuantityController editProductQuantityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_edit_product_units);
        initView();
        tvToolbarTitle.setText("Chỉnh sửa đơn vị");
        getProduct();
        buildRecyclerViewUnits();
        actionBtnEditUnits();
    }


    private void actionBtnEditUnits() {
        btnEditUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitList = getUnitFromRv();
                boolean flagUnitName = true;
                boolean flagUnitPrice = true;
                long baseUnitPrice = unitList.get(unitList.size() - 1).getUnitPrice();
                String baseUnitName = unitList.get(unitList.size() - 1).getUnitName();
                for (int i = 0; i < unitList.size(); i++) {
                    if (unitList.get(i).getUnitName().trim().isEmpty()) flagUnitName = false;
                    else if (unitList.get(i).getUnitPrice() == 0) flagUnitPrice = false;
                }
                boolean flagCompareUnit = true;
                if (flagUnitName && flagUnitPrice) {
                    for (int i = 0; i < unitList.size() - 1; i++) {
                        if (unitList.get(i).getUnitPrice() < baseUnitPrice) {
//                            Toast.makeText(EditProductUnitsActivity.this, "Giá của đơn vị " + baseUnitName + " không được lớn hơn giá của đơn vị " +
//                                    unitList.get(i).getUnitName(), Toast.LENGTH_SHORT).show();
                            flagCompareUnit = false;
                        }

                    }
                }
                boolean flagDuplicateUnitName = true;
                for (int i = 0; i < unitList.size(); i++) {
                    for (int j = i + 1; j < unitList.size(); j++) {
                        if (unitList.get(i).getUnitName().equals(unitList.get(j).getUnitName())) {
                            flagDuplicateUnitName = false;
                            break;
                        }
                    }

                }
                if (!flagCompareUnit) {
                    if (flagUnitName && flagUnitPrice) {
                        for (int i = 0; i < unitList.size() - 1; i++) {
                            if (unitList.get(i).getUnitPrice() < baseUnitPrice) {
//                            Toast.makeText(EditProductUnitsActivity.this, "Giá của đơn vị " + baseUnitName + " không được lớn hơn giá của đơn vị " +
//                                    unitList.get(i).getUnitName(), Toast.LENGTH_SHORT).show();
                                flagCompareUnit = false;
                            }

                        }
                    }
                } else if (!flagUnitName)
                    Toast.makeText(EditProductUnitsActivity.this, "Tên đơn vị không được để trống", Toast.LENGTH_SHORT).show();
                else if (!flagUnitPrice)
                    Toast.makeText(EditProductUnitsActivity.this, "Giá của sản phẩm ở từng đơn vị phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                else if (!flagDuplicateUnitName)
                    Toast.makeText(EditProductUnitsActivity.this, "Tên đơn vị không được trùng nhau", Toast.LENGTH_SHORT).show();
                else {
                    currentProduct.setUnits(unitList);
                    editProductQuantityController.addUnitsToFireBase(currentProduct, unitList);
                    Intent intent = new Intent();
                    intent.putExtra("product", currentProduct);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void getProduct() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra("product");
        unitList = currentProduct.getUnits();


    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        recyclerUnits = findViewById(R.id.recyclerUnits);
        btnEditUnits = findViewById(R.id.btnEditUnits);
        editProductQuantityController = new EditProductQuantityController();
    }

    private void buildRecyclerViewUnits() {
        recyclerUnits.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUnits.setLayoutManager(linearLayoutManager);
        editUnitAdapter = new EditUnitAdapter(unitList, this);
        recyclerUnits.setAdapter(editUnitAdapter);
    }

    private List<Unit> getUnitFromRv() {
        // ArrayList<Unit> list = new ArrayList<>();
        for (int i = 0; i < editUnitAdapter.getItemCount(); i++) {
            EditUnitAdapter.ViewHolder viewHolder = (EditUnitAdapter.ViewHolder) recyclerUnits.findViewHolderForAdapterPosition(i);
            String unitName = viewHolder.getEtUnitName().getText().toString().trim();
            String unitPrice = viewHolder.getEtUnitPrice().getText().toString().trim();
            if (unitPrice.isEmpty()) unitPrice = "0";
            //Log.i("price", unitPrice);
            unitList.get(i).setUnitName(unitName);
            unitList.get(i).setUnitPrice(Money.getInstance().reFormatVN(unitPrice));

//                Unit unit = new Unit();
//                unit
//                unit.setUnitPrice(Long.parseLong(unitPrice));
//                list.add(unit);

        }
        //Log.i("listUnit", list.get(0).getUnitPrice() +"");
        return unitList;
    }

    public void back(View view) {
        onBackPressed();
    }

}