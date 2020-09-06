package com.koit.capstonproject_version_1.model.dao;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koit.capstonproject_version_1.controller.Interface.IProduct;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.SuggestedProduct;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.view.DetailProductActivity;
import com.koit.capstonproject_version_1.helper.MoneyEditText;

import java.util.ArrayList;
import java.util.List;

public class CreateProductDAO {
    private static CreateProductDAO mInstance;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public CreateProductDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static CreateProductDAO getInstance() {
        if (mInstance == null) {
            mInstance = new CreateProductDAO();
        }
        return mInstance;
    }

    public void getSuggestedProduct(final String barcode, final IProduct iProduct) {

        if (!barcode.contains(".") && !barcode.contains("#") && !barcode.contains("$") &&
                !barcode.contains("[") && !barcode.contains("]")) {
            DatabaseReference dr = databaseReference.child("SuggestedProducts").child(barcode);
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SuggestedProduct product = dataSnapshot.getValue(SuggestedProduct.class);
                    iProduct.getSuggestedProduct(product);
                    if (product != null) {
                        Log.i("suggestedProduct", product.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("TAG", "Failed to read value.", databaseError.toException());
                }
            });
        }

    }

    public void checkExistBarcode(final String barcode, final TextInputEditText tetProductName
            , final TextView tvCategory, final Activity activity, final TextInputEditText etBarcode
            , final TextInputEditText etUnitName, final MoneyEditText etUnitPrice) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkExistBarcode(snapshot, barcode, tetProductName, tvCategory, activity, etBarcode, etUnitName, etUnitPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(valueEventListener);
    }

    private void checkExistBarcode(DataSnapshot snapshot, final String barcode
            , final TextInputEditText tetProductName, final TextView tvCategory, final Activity activity
            , final TextInputEditText etBarcode, final TextInputEditText etUnitName, final MoneyEditText etUnitPrice) {

        DataSnapshot dataSnapshot = snapshot.child("Products").child(UserDAO.getInstance().getUserID());
        DataSnapshot dataSnapshotUnits = snapshot.child("Units").child(UserDAO.getInstance().getUserID());

        boolean isExisted = false;
        for (DataSnapshot value : dataSnapshot.getChildren()) {
            final Product product = value.getValue(Product.class);
            product.setProductId(value.getKey());

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
            if (product.getBarcode().toLowerCase().equals(barcode.toLowerCase())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Sản phẩm với mã barcode này đã được tạo, bạn có muốn xem chi tiết sản phẩm này không?")
                        .setCancelable(true)
                        .setPositiveButton("Xem chi tiết", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(activity, DetailProductActivity.class);
                                intent.putExtra("product", product);
                                activity.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.theme));
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                    }
                });
                alert.show();
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            etBarcode.setText(barcode);
            fillProduct(barcode, tetProductName, tvCategory, etUnitName, etUnitPrice);
        }
    }

    public void fillProduct(String barcode, final TextInputEditText tetProductName, final TextView tvCategory
            , final TextInputEditText etUnitName, final MoneyEditText etUnitPrice) {
        final IProduct iProduct = new IProduct() {
            @Override
            public void getSuggestedProduct(SuggestedProduct product) {
                if (product != null) {
                    Log.i("product", product.toString());
                    tetProductName.setText(product.getName().trim());
                    String categoryName = product.getCategoryName().trim();
                    String formatCategoryName = categoryName.toUpperCase().charAt(0) + categoryName.substring(1).toLowerCase();
                    tvCategory.setText(formatCategoryName);
                    String unitName = product.getUnit().trim().toUpperCase();
                    String format = unitName.charAt(0) + unitName.substring(1).toLowerCase();
                    etUnitName.setText(format);
                    etUnitPrice.setText(Money.getInstance().formatVN(Long.parseLong(product.getPrice().trim())));
                }
            }

            @Override
            public void getProductById(Product product) {

            }
        };
        CreateProductDAO.getInstance().getSuggestedProduct(barcode, iProduct);
    }


    public void addProductInFirebase(Product product) {
        product.setUnits(null);
        String userId = UserDAO.getInstance().getUserID();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        dr = dr.child("Products").child(userId).child(product.getProductId());
        dr.setValue(product);
        databaseReference.keepSynced(true);
    }

    public void addImageProduct(final Uri uri, String imgName, Activity activity) {
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference image = storageReference.child("ProductPictures/" + imgName);
        if (uri != null) {
//            final File file = new File(SiliCompressor.with(activity).compress(FileUtils.getPath(activity, uri)
//                    , new File(activity.getCacheDir(), "temp")));
//            final Uri compressUri = Uri.fromFile(file);
            image.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("saveImageProduct", "onSuccess: Upload Image URI is " + uri.toString());
                    } else {
                        Log.i("saveImageProduct", "save failed");

                    }
                }

            });
        }
    }

    public void deleteImageProduct(String imgName) {
//        final StorageReference image = storageReference
        final StorageReference image = storageReference.child("ProductPictures/" + imgName);
        image.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}
