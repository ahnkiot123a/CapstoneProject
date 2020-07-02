package com.koit.capstonproject_version_1.Controller;

import android.util.Log;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;
import com.koit.capstonproject_version_1.dao.CreateProductDAO;

public class CreateProductController {

    public CreateProductController() {

    }

    public void fillProduct(String barcode, final TextInputEditText tetProductName, final TextView tvCategory) {
        final IProduct iProduct = new IProduct() {
            @Override
            public void getSuggestedProduct(SuggestedProduct product) {
                if(product != null){
                    Log.i("product", product.toString());
                    tetProductName.setText(product.getName().trim());
                    tvCategory.setText(product.getCategoryName().trim());
                }
            }
        };
        CreateProductDAO.getInstance().getSuggestedProduct(barcode, iProduct);
    }
}
