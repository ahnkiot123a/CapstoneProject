package com.koit.capstonproject_version_1.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.View.LoginActivity;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;

public class Product {
    private String userId, productId, barcode, categoryName, productDescription, productImageUrl;
    private String productName;
    private boolean active;
    DatabaseReference nodeRoot;
   private List<Unit> units;
    private List<Product> listProduct;

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
                LayDanhSachSanPham(dataSnapshot, listProductInterface);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if (dataRoot != null) {
            LayDanhSachSanPham(dataRoot, listProductInterface);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void LayDanhSachSanPham(DataSnapshot dataSnapshot, ListProductInterface listProductInterface) {
        DataSnapshot dataSnapshotProduct = dataSnapshot.child("Products").child("0399271212");
        //Lấy danh sách san pham
        for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
            Product product = valueProduct.getValue(Product.class);
            product.setProductId(product.getProductId());
            //Lấy danh sách hình ảnh của quán ăn theo mã
//            DataSnapshot dataSnapshotHinhQuanAn = dataSnapshot.child("hinhanhquanans").child(valueQuanAn.getKey());
//
//            List<String> hinhanhlist = new ArrayList<>();
//
//            for (DataSnapshot valueHinhQuanAn : dataSnapshotHinhQuanAn.getChildren()){
//                hinhanhlist.add(valueHinhQuanAn.getValue(String.class));
//            }
//            quanAnModel.setHinhanhquanan(hinhanhlist);

            //Lấy danh sách bình luân của quán ăn
//            DataSnapshot snapshotBinhLuan = dataSnapshot.child("binhluans").child(quanAnModel.getMaquanan());
//            List<BinhLuanModel> binhLuanModels = new ArrayList<>();
//
//            for (DataSnapshot valueBinhLuan : snapshotBinhLuan.getChildren()){
//                BinhLuanModel binhLuanModel = valueBinhLuan.getValue(BinhLuanModel.class);
//                binhLuanModel.setManbinhluan(valueBinhLuan.getKey());
//                ThanhVienModel thanhVienModel = dataSnapshot.child("thanhviens").child(binhLuanModel.getMauser()).getValue(ThanhVienModel.class);
//                binhLuanModel.setThanhVienModel(thanhVienModel);
//
//                List<String> hinhanhBinhLuanList = new ArrayList<>();
//                DataSnapshot snapshotNodeHinhAnhBL = dataSnapshot.child("hinhanhbinhluans").child(binhLuanModel.getManbinhluan());
//                for (DataSnapshot valueHinhBinhLuan : snapshotNodeHinhAnhBL.getChildren()){
//                    hinhanhBinhLuanList.add(valueHinhBinhLuan.getValue(String.class));
//                }
//                binhLuanModel.setHinhanhBinhLuanList(hinhanhBinhLuanList);
//
//                binhLuanModels.add(binhLuanModel);
//            }
//            quanAnModel.setBinhLuanModelList(binhLuanModels);

            //Lấy chi nhánh quán ăn
//            DataSnapshot snapshotChiNhanhQuanAn = dataSnapshot.child("chinhanhquanans").child(quanAnModel.getMaquanan());
//            List<ChiNhanhQuanAnModel> chiNhanhQuanAnModels = new ArrayList<>();
//
//            for (DataSnapshot valueChiNhanhQuanAn : snapshotChiNhanhQuanAn.getChildren()){
//                ChiNhanhQuanAnModel chiNhanhQuanAnModel = valueChiNhanhQuanAn.getValue(ChiNhanhQuanAnModel.class);
//                Location vitriquanan = new Location("");
//                vitriquanan.setLatitude(chiNhanhQuanAnModel.getLatitude());
//                vitriquanan.setLongitude(chiNhanhQuanAnModel.getLongitude());
//
//                double khoangcach = vitrihientai.distanceTo(vitriquanan)/1000;
//                chiNhanhQuanAnModel.setKhoangcach(khoangcach);
//
//                chiNhanhQuanAnModels.add(chiNhanhQuanAnModel);
//            }
//
//            quanAnModel.setChiNhanhQuanAnModelList(chiNhanhQuanAnModels);
//
            listProductInterface.getListProductModel(product);
        }
    }
}
