package com.koit.capstonproject_version_1.Model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.View.SelectProductActivity;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Product implements Serializable {
    private String userId, productId, barcode, categoryName, productDescription, productImageUrl;
    private String productName;
    private boolean active;
    private DatabaseReference nodeRoot;
    private List<Unit> units;

    private static List<Product> productListSearch;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserDAO userDAO;

    public Product(String userId, String productId, String barcode, String categoryName, String productDescription,
                   String productImageUrl, String productName, boolean active, List<Unit> units) {
        this.userId = userId;
        this.productId = productId;
        this.barcode = barcode;
        this.categoryName = categoryName;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.active = active;
        this.units = units;
    }

    public Product() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public void addUnit(Unit u){
        boolean flag =true;
        for(Unit u1: units){
            if(u.getUnitId().equals(u1.getUnitId())){
                long quant = u1.getUnitQuantity() + u.getUnitQuantity();
                u1.setUnitQuantity(quant);
                flag = false;
                break;
            }
        }
        if(flag){
            units.add(u);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", barcode='" + barcode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productName='" + productName + '\'' +
                ", active=" + active +
                ", units=" + units +
                ", userDAO=" + userDAO +
                '}';
    }

    public static void show(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    //for ListProductController
    public void getListProduct(final String searchText, final ListProductInterface
            listProductInterface, final TextView textView, final LinearLayout linearLayoutEmpty,
                               final ConstraintLayout layoutSearch, final LinearLayout layoutNotFoundItem,
                               final Spinner category_Spinner, final ProgressBar pBarList) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListProduct(dataSnapshot, listProductInterface, searchText, textView, linearLayoutEmpty,
                        layoutSearch, layoutNotFoundItem, category_Spinner, pBarList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
    //for ListProductController

    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface,
                                String searchText, TextView textView, LinearLayout linearLayoutEmpty,
                                ConstraintLayout constraintLayoutfound, LinearLayout layoutNotFoundItem,
                                Spinner category_Spinner, ProgressBar pBarList) {
        pBarList.setVisibility(View.VISIBLE);
        userDAO = new UserDAO();
        DataSnapshot dataSnapshotProduct;
        dataSnapshotProduct = dataSnapshot.child("Products").child(userDAO.getUserID());
        boolean isFound = false;
        if (dataSnapshotProduct.getValue() == null) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            constraintLayoutfound.setVisibility(View.GONE);
            layoutNotFoundItem.setVisibility(View.GONE);
            pBarList.setVisibility(View.GONE);
        } else {
            DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child(userDAO.getUserID());
            layoutNotFoundItem.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
            constraintLayoutfound.setVisibility(View.VISIBLE);
            pBarList.setVisibility(View.GONE);
            //Lấy danh sách san pham
            for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
                Product product = valueProduct.getValue(Product.class);

                product.setProductId(valueProduct.getKey());
                DataSnapshot dataSnapshotUnit = dataSnapshotUnits.child(product.getProductId());

                //lay unit theo ma san pham
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Unit unit = valueUnit.getValue(Unit.class);

                    if (unit != null) {
                        unit.setUnitId(valueUnit.getKey());
                        unitList.add(unit);
                    }
                }
                product.setUnits(unitList);
                Log.d("kiemtraProduct", unitList + "");

                if (searchText == null) {
                    listProductInterface.getListProductModel(product);
                } else {
                    //user searched
                    category_Spinner.setSelection(0);
                    if (searchText == "") {
                        //list product in first time or list all product
                        listProductInterface.getListProductModel(product);
                    } else {
                        //product contains searched characters or barcode
                        if (deAccent(product.getProductName().toLowerCase()).contains(deAccent(searchText.toLowerCase()))
                                || product.getBarcode().contains(searchText)) {
                            isFound = true;
                            listProductInterface.getListProductModel(product);
                        }
                        if (!isFound) {
                            textView.setText("0 sản phẩm");
                            layoutNotFoundItem.setVisibility(View.VISIBLE);
                        } else {
                            layoutNotFoundItem.setVisibility(View.GONE);

                        }
                    }
                }

            }
        }


    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    //Cate Tu Beo
    //for ListProductController

    public void getListProduct(final ListProductInterface listProductInterface, final String categoryName,
                               final LinearLayout linearLayoutEmpty, final ConstraintLayout constraintLayout,
                               final LinearLayout layoutNotFoundItem, final TextView textView,
                               Spinner category_Spinner, final ProgressBar pBarList) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListProduct(dataSnapshot, listProductInterface, categoryName, linearLayoutEmpty,
                        constraintLayout, layoutNotFoundItem, textView, pBarList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
    //for ListProductController

    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface, String categoryName,
                                LinearLayout linearLayoutEmpty, ConstraintLayout constraintLayout,
                                LinearLayout layoutNotFoundItem, TextView textView, ProgressBar pBarList) {
        userDAO = new UserDAO();
        pBarList.setVisibility(View.VISIBLE);
        DataSnapshot dataSnapshotProduct = dataSnapshot.child("Products").child(userDAO.getUserID());
        DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child(userDAO.getUserID());
        boolean isFound = false;
        if (dataSnapshotProduct == null) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            layoutNotFoundItem.setVisibility(View.GONE);
            pBarList.setVisibility(View.GONE);
        } else {
            layoutNotFoundItem.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            pBarList.setVisibility(View.GONE);


            //Lấy danh sách san pham
            for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
                Product product = valueProduct.getValue(Product.class);
                product.setProductId(valueProduct.getKey());

                DataSnapshot dataSnapshotUnit = dataSnapshotUnits.child(product.getProductId());

                Log.d("kiemtraProductID", product.getProductId() + "");
                //lay unit theo ma san pham
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Log.d("kiemtraUnit", valueUnit + "");
                    Unit unit = valueUnit.getValue(Unit.class);

                    unit.setUnitId(valueUnit.getKey());
                    unitList.add(unit);
                }
                product.setUnits(unitList);
                if (product.getCategoryName().equals(categoryName)) {
                    isFound = true;
                    listProductInterface.getListProductModel(product);
                }
                if (!isFound) {
                    textView.setText("0 sản phẩm");
                    layoutNotFoundItem.setVisibility(View.VISIBLE);
                } else {
                    layoutNotFoundItem.setVisibility(View.GONE);

                }
            }
        }
    }

    public void removeProduct(String userId, String productId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Products").child(userId).child(productId);
        databaseReference.removeValue();
    }

    public void updateProductToFirebase(String userId, Product product) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products")
                .child(userId).child(product.getProductId());
        databaseReference.child("active").setValue(product.isActive());
        databaseReference.child("barcode").setValue(product.getBarcode());
        databaseReference.child("categoryName").setValue(product.getCategoryName());
        databaseReference.child("productDescription").setValue(product.getProductDescription());
        databaseReference.child("productImageUrl").setValue(product.getProductImageUrl());
        databaseReference.child("productName").setValue(product.getProductName());

    }

    //for SelectProductController
    public void getListProduct(final String searchText, final ListProductInterface
            listProductInterface, final LinearLayout linearLayoutEmpty,
                               final LinearLayout layoutSearch, final LinearLayout layoutNotFoundItem,
                               final Spinner category_Spinner, final ProgressBar pBarList, final LinearLayout layoutButton) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListProduct(dataSnapshot, listProductInterface, searchText, linearLayoutEmpty,
                        layoutSearch, layoutNotFoundItem, category_Spinner, pBarList, layoutButton);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface,
                                String searchText, LinearLayout linearLayoutEmpty,
                                LinearLayout layoutSearch, LinearLayout layoutNotFoundItem,
                                Spinner category_Spinner, ProgressBar pBarList, LinearLayout layoutButton) {
        pBarList.setVisibility(View.VISIBLE);
        userDAO = new UserDAO();
        DataSnapshot dataSnapshotProduct;
        dataSnapshotProduct = dataSnapshot.child("Products").child(userDAO.getUserID());
        boolean isFound = false;
        // khong co san pham nao
        if (dataSnapshotProduct.getValue() == null) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            layoutSearch.setVisibility(View.GONE);
            layoutNotFoundItem.setVisibility(View.GONE);
            pBarList.setVisibility(View.GONE);
            layoutButton.setVisibility(View.GONE);
        } else {
            DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child(userDAO.getUserID());
            layoutNotFoundItem.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
            layoutSearch.setVisibility(View.VISIBLE);
            pBarList.setVisibility(View.GONE);
            layoutButton.setVisibility(View.VISIBLE);

            //Lấy danh sách san pham
            for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
                Product product = valueProduct.getValue(Product.class);

                product.setProductId(valueProduct.getKey());
                DataSnapshot dataSnapshotUnit = dataSnapshotUnits.child(product.getProductId());

                //lay unit theo ma san pham
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Unit unit = valueUnit.getValue(Unit.class);

                    if (unit != null) {
                        unit.setUnitId(valueUnit.getKey());
                        unitList.add(unit);
                    }
                }
                product.setUnits(unitList);

                if (searchText == null) {
                    listProductInterface.getListProductModel(product);
                } else {
                    //user searched
                    category_Spinner.setSelection(0);
                    if (searchText == "") {
                        //list product in first time or list all product
                        listProductInterface.getListProductModel(product);
                    } else {
                        //check is barcode search or not
                        //search by barcode in select product
                        if (searchText.contains("!@#$%")) {
                            searchText = searchText.substring(0, searchText.length() - 5);
                            if (product.getBarcode().equals(searchText)&&(product.isActive())) {
                                isFound = true;
                                // transferToListItemInOrder
                                List<Product> productList = new ArrayList<>();
                                productList.add(product);
                                SelectProductActivity.getInstance().transferToListItemInOrder(productList);
                            }
                            if (!isFound) {
//                            textView.setText("0 sản phẩm");
                                layoutNotFoundItem.setVisibility(View.VISIBLE);
                            } else {
                                layoutNotFoundItem.setVisibility(View.GONE);
                            }
                        }
                        // search in list product in order
                        else if (searchText.contains("%$#@!")) {
                            searchText = searchText.substring(0, searchText.length() - 5);
                            if (product.getBarcode().equals(searchText)&&(product.isActive())) {
                                isFound = true;
                                // transferToListItemInOrder
                                List<Product> productList = new ArrayList<>();
                                productList.add(product);
                                SelectProductActivity.getInstance().transferToListItemInOrder(productList);
                            }
                            if (!isFound) {
//                            textView.setText("0 sản phẩm");
                                layoutNotFoundItem.setVisibility(View.VISIBLE);
                                SelectProductActivity.getInstance().transferToListItemInOrder(null);
                                Toast.makeText(SelectProductActivity.getInstance(), "Không tìm thấy sản phẩm.", Toast.LENGTH_SHORT).show();
                            } else {
                                layoutNotFoundItem.setVisibility(View.GONE);
                            }
                        } else
                        //search text
                        {
                            if (deAccent(product.getProductName().toLowerCase()).contains(deAccent(searchText.toLowerCase()))
                                    || product.getBarcode().contains(searchText)) {
                                isFound = true;
                                listProductInterface.getListProductModel(product);
                            }
                            if (!isFound) {
//                            textView.setText("0 sản phẩm");
                                layoutNotFoundItem.setVisibility(View.VISIBLE);
                            } else {
                                layoutNotFoundItem.setVisibility(View.GONE);
                            }
                        }
                        //product contains searched characters or barcode

                    }
                }
            }
        }
    }

    //Cate tus beo for SelectProductController
    public void getListProduct(final ListProductInterface listProductInterface, final String categoryName,
                               final LinearLayout linearLayoutEmpty, final LinearLayout layoutSearch,
                               final LinearLayout layoutNotFoundItem, final ProgressBar pBarList, final LinearLayout layoutButton) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListProduct(dataSnapshot, listProductInterface, categoryName, linearLayoutEmpty,
                        layoutSearch, layoutNotFoundItem, pBarList, layoutButton);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface, String categoryName,
                                LinearLayout linearLayoutEmpty, LinearLayout layoutSearch,
                                LinearLayout layoutNotFoundItem, ProgressBar pBarList, LinearLayout layoutbutton) {
        userDAO = new UserDAO();
        pBarList.setVisibility(View.VISIBLE);
        DataSnapshot dataSnapshotProduct = dataSnapshot.child("Products").child(userDAO.getUserID());
        DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child(userDAO.getUserID());
        boolean isFound = false;
        if (dataSnapshotProduct == null) {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            layoutSearch.setVisibility(View.GONE);
            layoutNotFoundItem.setVisibility(View.GONE);
            pBarList.setVisibility(View.GONE);
        } else {
            layoutNotFoundItem.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
            layoutSearch.setVisibility(View.VISIBLE);
            pBarList.setVisibility(View.GONE);


            //Lấy danh sách san pham
            for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
                Product product = valueProduct.getValue(Product.class);
                product.setProductId(valueProduct.getKey());

                DataSnapshot dataSnapshotUnit = dataSnapshotUnits.child(product.getProductId());

                Log.d("kiemtraProductID", product.getProductId() + "");
                //lay unit theo ma san pham
                List<Unit> unitList = new ArrayList<>();
                for (DataSnapshot valueUnit : dataSnapshotUnit.getChildren()) {
                    Log.d("kiemtraUnit", valueUnit + "");
                    Unit unit = valueUnit.getValue(Unit.class);

                    unit.setUnitId(valueUnit.getKey());
                    unitList.add(unit);
                }
                product.setUnits(unitList);
                if (product.getCategoryName().equals(categoryName)) {
                    isFound = true;
                    listProductInterface.getListProductModel(product);
                }
                if (!isFound) {
//                    textView.setText("0 sản phẩm");
                    layoutNotFoundItem.setVisibility(View.VISIBLE);
                } else {
                    layoutNotFoundItem.setVisibility(View.GONE);

                }
            }
        }
    }

}
