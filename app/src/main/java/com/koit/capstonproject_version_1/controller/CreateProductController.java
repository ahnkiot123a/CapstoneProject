package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.controller.Interface.ICategory;
import com.koit.capstonproject_version_1.model.Category;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.view.ConvertRateActivity;
import com.koit.capstonproject_version_1.view.CreateProductActivity;
import com.koit.capstonproject_version_1.model.dao.CreateProductDAO;
import com.koit.capstonproject_version_1.helper.CustomToast;
import com.koit.capstonproject_version_1.helper.MoneyEditText;

import java.util.ArrayList;

public class CreateProductController {

    private CreateProductDAO createProductDAO;
    private CameraController cameraController;
    private DetailProductController detailProductController;
    private ArrayList<Unit> listUnit = new ArrayList<>();


    private Activity activity;

    public CreateProductController() {
    }

    public CreateProductController(Activity activity) {
        cameraController = new CameraController(activity);
        createProductDAO = new CreateProductDAO();
        detailProductController = new DetailProductController();
        this.activity = activity;

    }

    public void checkExistedBarcode(final String barcode, final TextInputEditText tetProductName
            , final TextView tvCategory, final TextInputEditText etBarcode, final TextInputEditText etUnitName
            , final MoneyEditText etUnitPrice) {
        createProductDAO.checkExistBarcode(barcode, tetProductName, tvCategory, activity, etBarcode, etUnitName, etUnitPrice);
    }


    public void createProduct(TextInputEditText etBarcode, TextInputEditText tetProductName, TextInputEditText tetDescription,
                              TextView tvCategory, boolean checked, LinearLayout layoutUnitList) {
        String barcode = etBarcode.getText().toString().trim();
        String productName = tetProductName.getText().toString().trim();
        if (productName.isEmpty()) {
            tetProductName.setError("Tên sản phẩm không được để trống");
            tetProductName.requestFocus();
        } else {
            String category = tvCategory.getText().toString().trim();
            if (checkValidAndReadUnit(layoutUnitList)) {
//                ArrayList<Unit> listUnit = CreateProductActivity.getUnitFromRv();
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

    private boolean checkValidAndReadUnit(LinearLayout layoutUnitList) {
        listUnit.clear();
        boolean result = true;
        for (int i = 0; i < layoutUnitList.getChildCount(); i++) {
            View unitItem = layoutUnitList.getChildAt(i);
            EditText etUnitName = unitItem.findViewById(R.id.etUnitName);
            MoneyEditText etUnitPrice = unitItem.findViewById(R.id.etUnitPrice);
            Unit unit = new Unit();
            if (listUnit.size() == 0) {
                if (!etUnitName.getText().toString().isEmpty()) {
                    unit.setUnitName(etUnitName.getText().toString().trim());
                    if (!etUnitPrice.getText().toString().isEmpty()) {
                        unit.setUnitPrice(Money.getInstance().reFormatVN(etUnitPrice.getText().toString().trim()));
                        listUnit.add(unit);
                    } else {
                        etUnitPrice.requestFocus();
                        CustomToast.makeText(activity, "Giá của đơn vị không được để trống", Toast.LENGTH_LONG
                                , CustomToast.ERROR, true, Gravity.BOTTOM).show();
                        result = false;
                        break;
                    }
                } else {
                    etUnitName.requestFocus();
                    CustomToast.makeText(activity, "Tên đơn vị không được để trống", Toast.LENGTH_LONG
                            , CustomToast.ERROR, true, Gravity.BOTTOM).show();
                    result = false;
                    break;
                }
            } else if (!etUnitName.getText().toString().isEmpty()) {
                if (!etUnitPrice.getText().toString().isEmpty()) {
                    unit.setUnitName(etUnitName.getText().toString().trim());
                    unit.setUnitPrice(Money.getInstance().reFormatVN(etUnitPrice.getText().toString().trim()));
                    listUnit.add(unit);
//                    if (InputController.isDuplicateUnit(listUnit)) {
//                        CustomToast.makeText(activity, "Tên đơn vị không được trùng nhau", Toast.LENGTH_LONG
//                                , CustomToast.ERROR, true, Gravity.BOTTOM).show();
//                        listUnit.remove(unit);
//                        result = false;
//                        break;
//                    }
                } else {
                    etUnitPrice.requestFocus();
                    CustomToast.makeText(activity, "Giá của đơn vị không được để trống", Toast.LENGTH_LONG
                            , CustomToast.ERROR, true, Gravity.BOTTOM).show();
                    result = false;
                    break;
                }
            } else if (!etUnitPrice.getText().toString().isEmpty()) {
                if (!etUnitName.getText().toString().isEmpty()) {
                    unit.setUnitName(etUnitName.getText().toString().trim());
                    unit.setUnitPrice(Money.getInstance().reFormatVN(etUnitPrice.getText().toString().trim()));
                    listUnit.add(unit);

                } else {
                    etUnitName.requestFocus();
                    CustomToast.makeText(activity, "Tên đơn vị không được để trống", Toast.LENGTH_LONG
                            , CustomToast.ERROR, true, Gravity.BOTTOM).show();
                    result = false;
                    break;
                }
            }
            if (InputController.isDuplicateUnit(listUnit)) {
                etUnitName.requestFocus();
                CustomToast.makeText(activity, "Tên đơn vị không được trùng nhau", Toast.LENGTH_LONG
                        , CustomToast.ERROR, true, Gravity.BOTTOM).show();
                result = false;
                listUnit.remove(unit);
                break;
            }
        }


        return result;
    }

    public void addProductInFirebase(Product currentProduct) {
        CreateProductDAO.getInstance().addProductInFirebase(currentProduct);

    }

    public void addCategory(final Product currentProduct) {
        ICategory iCategory = new ICategory() {
            @Override
            public void getCategory(Category category) {
                if (category == null) {
                    Category category2 = new Category(currentProduct.getCategoryName());
                    if (!currentProduct.getCategoryName().trim().equals(""))
                        category2.addCategoryToFireBase(category2);
                }
            }
        };
        Category category = new Category();
        category.getCategoryByCategoryName(currentProduct.getCategoryName(), iCategory);
    }


    public void addImageProduct() {
        Uri uri = CreateProductActivity.photoUri;
        String imgName = CreateProductActivity.photoName;
        CreateProductDAO.getInstance().addImageProduct(uri, imgName, activity);
    }
}
