package com.koit.capstonproject_version_1.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.adapter.AddQuantityAdapter;
import com.koit.capstonproject_version_1.adapter.EditConvertRateAdapter;
import com.koit.capstonproject_version_1.adapter.EditProductQuantityAdapter;
import com.koit.capstonproject_version_1.controller.CreateProductController;
import com.koit.capstonproject_version_1.controller.DetailProductController;
import com.koit.capstonproject_version_1.controller.EditProductQuantityController;
import com.koit.capstonproject_version_1.controller.ListCategoryController;
import com.koit.capstonproject_version_1.model.Category;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class ConvertRateActivity extends AppCompatActivity {

    public static final String IS_SUCCESS = "SUCCESS";

    private Toolbar toolbar;
    private TextView tvToolbarTitle;
    private Product currentProduct;
    private RecyclerView rvConvertRate;
    private RecyclerView rvUnitQuantity;
    private Button btnSuccess;
    private ConstraintLayout layoutConvertRate;
    private List<Unit> unitList = new ArrayList<>();
    private EditConvertRateAdapter editConvertRateAdapter;
    private AddQuantityAdapter addQuantityAdapter;
    private EditProductQuantityAdapter editProductQuantityAdapter;
    private CreateProductController createProductController;
    private EditProductQuantityController editProductQuantityController;
    private DetailProductController detailProductController;

    private ListCategoryController listCategoryController;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_rate);

        initView();
        tvToolbarTitle.setText("Thêm sản phẩm");

        getProduct();
        buildRecyclerViewUnits();
        buildRecyclerUnitQuantity();

    }


    private void initView() {
        toolbar = findViewById(R.id.toolbarGeneral);
        tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        btnSuccess = findViewById(R.id.btnSuccess);
        rvConvertRate = findViewById(R.id.rvConvertRate);
        rvUnitQuantity = findViewById(R.id.rvQuantityAdd);
        layoutConvertRate = findViewById(R.id.layoutConvertRate);

        createProductController = new CreateProductController(this);
        editProductQuantityController = new EditProductQuantityController();
        detailProductController = new DetailProductController();

        listCategoryController = new ListCategoryController(this);
    }

    public void addProduct(View view) {
        addProductToFirebase();
    }

    private void addProductToFirebase() {

        try {
//            Dialog dialog = new Dialog(this);
//            dialog.showLoadingDialog(R.raw.loading_animation);
            boolean result = addUnitToFirebase();
            if (result) {
                createProductController.addProductInFirebase(currentProduct);

                createProductController.addCategory(currentProduct);
                createProductController.addImageProduct();
                Toast.makeText(this.getApplicationContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListProductActivity.class);
                intent.putExtra(IS_SUCCESS, true);
                intent.putExtra(CreateProductActivity.NEW_PRODUCT, currentProduct);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(this.getApplicationContext(), "Tỉ lệ chuyển đổi giữa các đơn vị phải lớn hơn 1", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.d("createProduct", e.toString());
            Toast.makeText(this, "Thêm sản phẩm thất bại! Vui lòng thử lại...", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean addUnitToFirebase() {
        if (unitList.size() > 1) {
            setConvertRateFromRv();
            for (int i = 0; i < unitList.size() - 1; i++) {
                if (unitList.get(i).getConvertRate() < 2) {
                    return false;
                }
            }
        } else {
            unitList.get(0).setConvertRate(1);
        }

        setUnitQuantityFromRV();
        currentProduct.setUnits(unitList);
        editProductQuantityController.addUnitsToFireBase(currentProduct, unitList);
        return true;
    }


    private void getProduct() {
        Intent intent = getIntent();
        currentProduct = (Product) intent.getSerializableExtra(CreateProductActivity.NEW_PRODUCT);
        unitList = currentProduct.getUnits();
        detailProductController.sortUnitByPrice(unitList);

    }

    private void buildRecyclerViewUnits() {
        if (unitList.size() > 1) {
            rvConvertRate.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvConvertRate.setLayoutManager(linearLayoutManager);
            editConvertRateAdapter = new EditConvertRateAdapter(unitList, this);
            rvConvertRate.setAdapter(editConvertRateAdapter);
        } else {
            layoutConvertRate.setVisibility(View.GONE);
        }
    }

    private void buildRecyclerUnitQuantity() {
        rvUnitQuantity.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUnitQuantity.setLayoutManager(linearLayoutManager);
        addQuantityAdapter = new AddQuantityAdapter(unitList, this);
        rvUnitQuantity.setAdapter(addQuantityAdapter);
    }

    private void setConvertRateFromRv() {
        for (int i = 0; i < unitList.size(); i++) {
            unitList.get(i).setConvertRate(1);
        }
        for (int i = 0; i < editConvertRateAdapter.getItemCount(); i++) {
            EditConvertRateAdapter.ViewHolder viewHolder = (EditConvertRateAdapter.ViewHolder) rvConvertRate.findViewHolderForAdapterPosition(i);
            String convertRate = viewHolder.getEtConvertRate().getText().toString().trim();
            long rate = convertRate.isEmpty() ? 1 : Long.parseLong(convertRate);
            unitList.get(i).setConvertRate(rate);
        }
        Log.i("unitListAfterConvert", unitList.toString());

    }

    private void setUnitQuantityFromRV() {
        for (int i = 0; i < addQuantityAdapter.getItemCount(); i++) {
            AddQuantityAdapter.ViewHolder viewHolder = (AddQuantityAdapter.ViewHolder) rvUnitQuantity.findViewHolderForAdapterPosition(i);
            String unitQuantity = viewHolder.getEtProductQuantity().getText().toString().trim();
            long quantity = unitQuantity.isEmpty() ? 0 : Long.parseLong(unitQuantity);
            unitList.get(i).setUnitQuantity(quantity);
        }
        Log.i("unitList", unitList.toString());
        editProductQuantityController.calInventoryByUnit(unitList);
    }

    public void back(View view) {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
