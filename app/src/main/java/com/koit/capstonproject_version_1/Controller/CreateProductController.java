package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.Category;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.View.ConvertRateActivity;
import com.koit.capstonproject_version_1.View.CreateProductActivity;
import com.koit.capstonproject_version_1.dao.CreateProductDAO;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class CreateProductController {

    private CreateProductDAO createProductDAO;
    private CameraController cameraController;
    private DetailProductController detailProductController;


    private Activity activity;

    public CreateProductController() {
    }

    public CreateProductController(Activity activity) {
        cameraController = new CameraController(activity);
        createProductDAO = new CreateProductDAO();
        detailProductController = new DetailProductController();
        this.activity = activity;

    }

    public void fillProduct(String barcode, final TextInputEditText tetProductName, final TextView tvCategory) {
        final IProduct iProduct = new IProduct() {
            @Override
            public void getSuggestedProduct(SuggestedProduct product) {
                if (product != null) {
                    Log.i("product", product.toString());
                    tetProductName.setText(product.getName().trim());
                    tvCategory.setText(product.getCategoryName().trim());
                }
            }
        };
        CreateProductDAO.getInstance().getSuggestedProduct(barcode, iProduct);
    }

    public void createProduct(TextInputEditText etBarcode, TextInputEditText tetProductName, TextInputEditText tetDescription,
                              TextView tvCategory, boolean checked) {
        String barcode = etBarcode.getText().toString().trim();
        if (barcode.isEmpty()) {
            etBarcode.setError("Bạn phải quét barcode trước!");
        } else {
            String productName = tetProductName.getText().toString().trim();
            if (productName.isEmpty()) {
                tetProductName.setError("Tên sản phẩm không được để trống");
            } else {
                String category = tvCategory.getText().toString().trim();
                if (category.isEmpty()) {
                    Toast.makeText(activity, "Bạn phải chọn phân loại cho sản phẩm!", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Unit> listUnit = CreateProductActivity.getUnitFromRv();
                    String photoName = CreateProductActivity.photoName;
                    String productID = RandomStringController.getInstance().randomString();
                    Log.i("photoPath", photoName);
                    Product product = new Product();
                    product.setProductId(productID);
                    product.setBarcode(barcode);
                    product.setProductName(productName);
                    product.setProductDescription(tetDescription.getText().toString().trim());
                    product.setCategoryName(category);
                    product.setProductImageUrl(photoName);
                    product.setActive(checked);
                    product.setUnits(listUnit);
                    Log.i("addProduct", product.toString());

                    Intent intent = new Intent(activity, ConvertRateActivity.class);
                    intent.putExtra(CreateProductActivity.NEW_PRODUCT, product);
                    activity.startActivity(intent);

                }
            }
        }
    }

    public void addProductInFirebase(Product currentProduct) {
        CreateProductDAO.getInstance().addProductInFirebase(currentProduct);
    }
    public void addCategoryToFirebase(Product currentProduct, List<Category> categoryList) {
        String userId = UserDAO.getInstance().getUserID();

        boolean flag = true;
        for (int i = 0;i<categoryList.size();i++){
            if(currentProduct.getCategoryName().equals(categoryList.get(i).getCategoryName()))
                flag = false;
        }
        // Log.i("categoryList", categoryList.get(i).getCategoryName());

        if(flag) {
            Category category = new Category(currentProduct.getCategoryName());
            category.addCategoryToFireBase(userId);
        }
//        CreateProductDAO.getInstance().addProductInFirebase(currentProduct);
    }


    public void addImageProduct() {
        Uri uri = CreateProductActivity.photoUri;
        String imgName = CreateProductActivity.photoName;
        CreateProductDAO.getInstance().addImageProduct(uri, imgName);
    }
}
