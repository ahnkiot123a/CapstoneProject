package com.koit.capstonproject_version_1.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Adapter.ConvertRateRecyclerAdapter;
import com.koit.capstonproject_version_1.Adapter.EditUnitAdapter;
import com.koit.capstonproject_version_1.Controller.AddProductQuantityController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class EditProductUnitsActivity extends AppCompatActivity {
    private TextView tvToolbarTitle,tvConvertRate;
    private Product currentProduct;
    private RecyclerView recyclerUnits,recyclerConvertRate;
    private Button btnEditUnits;
    private List<Unit> unitList;
    private EditUnitAdapter editUnitAdapter;
    private AddProductQuantityController addProductQuantityController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                currentProduct.setUnits(unitList);
                addProductQuantityController.addUnitsToFireBase(currentProduct,unitList);
                Intent intent = new Intent();
                intent.putExtra("product",currentProduct);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    }

    private void getProduct() {
        Intent intent = getIntent();
        currentProduct =(Product) intent.getSerializableExtra("product");
        unitList = currentProduct.getUnits();


    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        recyclerUnits = findViewById(R.id.recyclerUnits);
        btnEditUnits = findViewById(R.id.btnEditUnits);
        tvConvertRate = findViewById(R.id.tvConvertRate);
        recyclerConvertRate = findViewById(R.id.recyclerConvertRate);
        addProductQuantityController = new AddProductQuantityController();
    }
    private void buildRecyclerViewUnits() {
        recyclerUnits.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUnits.setLayoutManager(linearLayoutManager);
        editUnitAdapter = new EditUnitAdapter( unitList,this);
        recyclerUnits.setAdapter(editUnitAdapter);
    }
    private List<Unit> getUnitFromRv(){
       // ArrayList<Unit> list = new ArrayList<>();
        for (int i = 0; i < editUnitAdapter.getItemCount(); i++) {
            EditUnitAdapter.ViewHolder viewHolder = (EditUnitAdapter.ViewHolder) recyclerUnits.findViewHolderForAdapterPosition(i);
            String unitName = viewHolder.getEtUnitName().getText().toString().trim();
            String unitPrice = viewHolder.getEtUnitPrice().getText().toString().trim();

            //Log.i("price", unitPrice);
            if(!unitName.isEmpty() && !unitPrice.isEmpty()){
                unitList.get(i).setUnitName(unitName);
                unitList.get(i).setUnitPrice(Long.parseLong(unitPrice));
//                Unit unit = new Unit();
//                unit
//                unit.setUnitPrice(Long.parseLong(unitPrice));
//                list.add(unit);
            }
        }
        //Log.i("listUnit", list.get(0).getUnitPrice() +"");
        return unitList;
    }

}