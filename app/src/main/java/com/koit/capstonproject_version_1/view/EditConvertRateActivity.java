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

import com.koit.capstonproject_version_1.adapter.EditConvertRateAdapter;
import com.koit.capstonproject_version_1.controller.EditProductQuantityController;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditConvertRateActivity extends AppCompatActivity {
    private TextView tvToolbarTitle, tvConvertRate;
    private Product currentProduct;
    private RecyclerView recyclerConvertRate;
    private Button btnEditConvertRate;
    private List<Unit> unitList;
    private EditConvertRateAdapter editConvertRateAdapter;
    private EditProductQuantityController editProductQuantityController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_edit_convert_rate);
        initView();
        tvToolbarTitle.setText("Sửa chuyển đổi đơn vị");
        getProduct();
        buildRecyclerviewConvertRate();
        actionBtnEditConvertRate();
    }

    private void actionBtnEditConvertRate() {
        btnEditConvertRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnitFromRv();
                boolean flagConvertRate = true;
                for (int i = 0; i < unitList.size() - 1; i++) {
                    if (unitList.get(i).getConvertRate() == -1) {
                        Toast.makeText(EditConvertRateActivity.this, "Tỉ lệ chuyển đổi giữa các đơn vị không được để trống", Toast.LENGTH_SHORT).show();
                        flagConvertRate = false;
                    } else if (unitList.get(i).getConvertRate() < 2) {
                        Toast.makeText(EditConvertRateActivity.this, "Tỉ lệ chuyển đổi giữa các đơn vị phải lớn hơn 1", Toast.LENGTH_SHORT).show();
                        flagConvertRate = false;
                    }
                }
                if (flagConvertRate) {
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

    private void getUnitFromRv() {
        // ArrayList<Unit> list = new ArrayList<>();
        for (int i = 0; i < editConvertRateAdapter.getItemCount(); i++) {
            EditConvertRateAdapter.ViewHolder viewHolder = (EditConvertRateAdapter.ViewHolder) recyclerConvertRate.findViewHolderForAdapterPosition(i);
            String convertRate = viewHolder.getEtConvertRate().getText().toString().trim();
            long unitSmallestQuantity = unitList.get(unitList.size() - 1).getUnitQuantity();
            //Log.i("price", unitPrice);
            if (convertRate.isEmpty()) convertRate = "-1";

            long newConvertRate = Long.parseLong(convertRate);
            unitList.get(i).setConvertRate(Long.parseLong(convertRate));
            if (newConvertRate != 0){
                long quantity = (int) unitSmallestQuantity / newConvertRate;
                unitList.get(i).setUnitQuantity(quantity);
            }


        }
        //Log.i("listUnit", list.get(0).getUnitPrice() +"");
        //  return unitList;
    }

    private void buildRecyclerviewConvertRate() {
        recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerConvertRate.setLayoutManager(linearLayoutManager);
        editConvertRateAdapter = new EditConvertRateAdapter(unitList, this);
        recyclerConvertRate.setAdapter(editConvertRateAdapter);
    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnEditConvertRate = findViewById(R.id.btnEditConvertRate);
        tvConvertRate = findViewById(R.id.tvConvertRate);
        recyclerConvertRate = findViewById(R.id.recyclerConvertRate);
        editProductQuantityController = new EditProductQuantityController();
    }

    private void getProduct() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra("product");
        unitList = currentProduct.getUnits();
    }

    public void back(View view) {
        onBackPressed();
    }
}