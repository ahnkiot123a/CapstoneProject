package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Adapter.ConvertRateRecyclerAdapter;
import com.koit.capstonproject_version_1.Adapter.UnitRecyclerAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ICategory;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.View.DetailProductActivity;
import com.koit.capstonproject_version_1.View.UpdateProductInformationActivity;
import com.koit.capstonproject_version_1.dao.CreateProductDAO;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductController {

    private Activity activity;

    public UpdateProductController(Activity activity) {
        this.activity = activity;
    }

    public UpdateProductController() {
    }

    public void updateProduct(TextInputEditText etBarcode, TextInputEditText tetProductName,
                              TextInputEditText tetDescription, TextView tvCategory,
                              Switch switchActive, Product currentProduct
    ) {
        String barcode = etBarcode.getText().toString().trim();
        String name = tetProductName.getText().toString().trim();
        String description = tetDescription.getText().toString().trim();
        String categoryName = tvCategory.getText().toString().trim();
        Boolean active = (switchActive.isChecked()) ? true : false;
        currentProduct.setBarcode(barcode);
        currentProduct.setProductName(name);
        currentProduct.setCategoryName(categoryName);
        currentProduct.setProductDescription(description);
        currentProduct.setActive(active);
        //  Uri uri = CreateProductActivity.photoUri;
        //  String imgName = CreateProductActivity.photoName;
        if (UpdateProductInformationActivity.photoName != null) {
            if (currentProduct.getProductImageUrl() == null) {
                CreateProductDAO.getInstance().addImageProduct(UpdateProductInformationActivity.photoUri, UpdateProductInformationActivity.photoName);
                currentProduct.setProductImageUrl(UpdateProductInformationActivity.photoName);
            } else if (!currentProduct.getProductImageUrl().equals(UpdateProductInformationActivity.photoName)) {
                CreateProductDAO.getInstance().deleteImageProduct(currentProduct.getProductImageUrl());
                CreateProductDAO.getInstance().addImageProduct(UpdateProductInformationActivity.photoUri, UpdateProductInformationActivity.photoName);
                currentProduct.setProductImageUrl(UpdateProductInformationActivity.photoName);
            }
        }

        if (name.isEmpty()) {
            Toast.makeText(activity, "Tên của sản phẩm không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            Product productDAO = new Product();
            productDAO.updateProductToFirebase(UserDAO.getInstance().getUserID(), currentProduct);
            Intent intent = new Intent(activity, DetailProductActivity.class);
            intent.putExtra("product", currentProduct);
            activity.startActivity(intent);
            activity.finish();
            Toast.makeText(activity, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();

        }
    }

    public void setRecyclerViewUnits(RecyclerView recyclerUnits, ArrayList<Unit> listUnit) {
        recyclerUnits.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerUnits.setLayoutManager(linearLayoutManager);
        UnitRecyclerAdapter unitRecyclerAdapter = new UnitRecyclerAdapter(listUnit, activity.getApplicationContext());
        recyclerUnits.setAdapter(unitRecyclerAdapter);
    }

    public void setRecyclerConvertRate(ArrayList<Unit> listUnit, TextView tvConvertRate,
                                       Button btnEditConvertRate, RecyclerView recyclerConvertRate) {
        if (listUnit.size() < 2) {
            tvConvertRate.setVisibility(View.INVISIBLE);
            btnEditConvertRate.setVisibility(View.INVISIBLE);
        }
        recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        recyclerConvertRate.setLayoutManager(linearLayoutManager1);

        ConvertRateRecyclerAdapter convertRateRecyclerAdapter = new ConvertRateRecyclerAdapter(listUnit, activity.getApplicationContext());
        recyclerConvertRate.setAdapter(convertRateRecyclerAdapter);
    }

    public void setSpinnerUnit(final ArrayList<Unit> listUnit, Spinner spinnerUnit, final TextView tvUnitQuantity) {
        ArrayAdapter<Unit> adapter =
                new ArrayAdapter<Unit>(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listUnit);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvUnitQuantity.setText(listUnit.get(position).getUnitQuantity() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
