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

import com.koit.capstonproject_version_1.Adapter.EditConvertRateAdapter;
import com.koit.capstonproject_version_1.Adapter.EditUnitAdapter;
import com.koit.capstonproject_version_1.Controller.AddProductQuantityController;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditConvertRateActivity extends AppCompatActivity {
    private TextView tvToolbarTitle,tvConvertRate;
    private Product currentProduct;
    private RecyclerView recyclerConvertRate;
    private Button btnEditConvertRate;
    private List<Unit> unitList;
    private EditConvertRateAdapter editConvertRateAdapter;
    private AddProductQuantityController addProductQuantityController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_convert_rate);
        initView();
        tvToolbarTitle.setText("Chỉnh sửa chuyển đổi đơn vị");
        getProduct();
        buildRecyclerviewConvertRate();
        actionBtnEditConvertRate();
    }

    private void actionBtnEditConvertRate() {
        btnEditConvertRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnitFromRv();
                currentProduct.setUnits(unitList);
                addProductQuantityController.addUnitsToFireBase(currentProduct,unitList);
                Intent intent = new Intent();
                intent.putExtra("product",currentProduct);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    }
    private void getUnitFromRv(){
        // ArrayList<Unit> list = new ArrayList<>();
        for (int i = 0; i < editConvertRateAdapter.getItemCount(); i++) {
            EditConvertRateAdapter.ViewHolder viewHolder = (EditConvertRateAdapter.ViewHolder) recyclerConvertRate.findViewHolderForAdapterPosition(i);
            String unitBigName = viewHolder.getEtBigUnitName().getText().toString().trim();
            String convertRate = viewHolder.getEtConvertRate().getText().toString().trim();

            //Log.i("price", unitPrice);
            if(!unitBigName.isEmpty() && !convertRate.isEmpty()){
                //unitList.get(i).setUnitName(unitName);
                unitList.get(i).setConvertRate(Long.parseLong(convertRate));
//                Unit unit = new Unit();
//                unit
//                unit.setUnitPrice(Long.parseLong(unitPrice));
//                list.add(unit);
            }
        }
        //Log.i("listUnit", list.get(0).getUnitPrice() +"");
      //  return unitList;
    }

    private void buildRecyclerviewConvertRate() {
        recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerConvertRate.setLayoutManager(linearLayoutManager);
        editConvertRateAdapter = new EditConvertRateAdapter( unitList,this);
        recyclerConvertRate.setAdapter(editConvertRateAdapter);
    }

    private void initView() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnEditConvertRate = findViewById(R.id.btnEditConvertRate);
        tvConvertRate = findViewById(R.id.tvConvertRate);
        recyclerConvertRate = findViewById(R.id.recyclerConvertRate);
        addProductQuantityController = new AddProductQuantityController();
    }
    private void getProduct() {
        Intent intent = getIntent();
        currentProduct =(Product) intent.getSerializableExtra("product");
        unitList = currentProduct.getUnits();


    }
}