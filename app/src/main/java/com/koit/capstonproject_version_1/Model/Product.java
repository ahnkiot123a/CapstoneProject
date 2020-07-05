package com.koit.capstonproject_version_1.Model;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Adapter.ItemAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Product {
    private String userId, productId, barcode, categoryName, productDescription, productImageUrl;
    private String productName;
    private boolean active;
    DatabaseReference nodeRoot;
    private List<Unit> units;

    public Product(String userId, String productId, String barcode, String categoryName, String productDescription, String productImageUrl, String productName, boolean active, List<Unit> units) {
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
        nodeRoot = FirebaseDatabase.getInstance().getReference();
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

    public DatabaseReference getMyRef() {
        String curUser;
        User user = SharedPrefs.getInstance().getCurrentUser(LoginActivity.CURRENT_USER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            curUser = user.getPhoneNumber();
//        } else {
//            curUser = firebaseUser.getUid();
//        }
        DatabaseReference myRef;
        //test
        myRef = FirebaseDatabase.getInstance().getReference("Products").child("0399271212");
        return myRef;
    }

    private DataSnapshot dataRoot;

    public void getListProduct(final ListProductInterface listProductInterface) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                getListProduct(dataSnapshot, listProductInterface);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
//        if (dataRoot != null) {
//            LayDanhSachSanPham(dataRoot, listProductInterface);
//        } else {
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
//        }
    }

    public void setTotalProductCate(final TextView textView) {
        nodeRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalProduct = (int) snapshot.child("Products").child("0399271212").getChildrenCount();
                textView.setText(totalProduct + " sản phẩm");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getListProduct(DataSnapshot dataSnapshot, ListProductInterface listProductInterface) {
        DataSnapshot dataSnapshotProduct = dataSnapshot.child("Products").child("0399271212");
        int totalProduct = (int) dataSnapshot.child("Products").child("0399271212").getChildrenCount();
        DataSnapshot dataSnapshotUnits = dataSnapshot.child("Units").child("0399271212");
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

                unit.setId(valueUnit.getKey());
                unitList.add(unit);
            }
            product.setUnits(unitList);
            listProductInterface.getListProductModel(product);
        }
    }

    public void firebaseProductSearch(final RecyclerView recyclerViewListProduct, String searchText, List<Product> productList, Context context) {
        List<Product> productSearch = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(searchText.toLowerCase())) {
                productSearch.add(product);
            }
        }
        ItemAdapter itemAdapter = new ItemAdapter(context, productSearch, R.layout.item_layout);
        recyclerViewListProduct.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
//        nodeRoot.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataSnapshot dataSnapshotProduct = dataSnapshot.child("Products").child("0399271212");
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    productList.add(ds.getValue(Product.class));
//                }
//                ItemAdapter adapter = new ItemAdapter(list);
//                recyclerViewListProduct.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

}
