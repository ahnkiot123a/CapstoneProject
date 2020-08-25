package com.koit.capstonproject_version_1.dao;

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
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;
import com.koit.capstonproject_version_1.Model.UIModel.Dialog;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.DetailProductActivity;

import java.io.File;

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
            , final TextView tvCategory, final Activity activity, final TextInputEditText etBarcode) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkExistBarcode(snapshot, barcode, tetProductName, tvCategory, activity, etBarcode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(valueEventListener);
    }

    private void checkExistBarcode(DataSnapshot snapshot, final String barcode
            , final TextInputEditText tetProductName, final TextView tvCategory, final Activity activity, final TextInputEditText etBarcode) {

        DataSnapshot dataSnapshot = snapshot.child("Products").child(UserDAO.getInstance().getUserID());
        boolean isExisted = false;
        for (DataSnapshot value : dataSnapshot.getChildren()) {
            final Product product = value.getValue(Product.class);
            if (product.getBarcode().toLowerCase().equals(barcode.toLowerCase())) {
                isExisted = true;
                break;
            } else {
                isExisted = false;
            }
        }
        if (isExisted) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Sản phẩm với mã barcode này đã được tạo, bạn có muốn xem chi tiết sản phẩm này không?")
                    .setCancelable(true)
                    .setPositiveButton("Xem chi tiết", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(activity, DetailProductActivity.class);
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
        } else {
            etBarcode.setText(barcode);
            fillProduct(barcode, tetProductName, tvCategory);
        }
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
//        databaseReference.keepSynced(true);
    }

    public void addImageProduct(final Uri uri, String imgName, final Activity activity, final Dialog dialog) {
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference image = storageReference.child("ProductPictures/" + imgName);
        if (uri != null) {
            final File file = new File(SiliCompressor.with(activity).compress(FileUtils.getPath(activity, uri)
                    , new File(activity.getCacheDir(), "temp")));
            final Uri compressUri = Uri.fromFile(file);
            image.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
//                        if (!activity.isFinishing() && dialog != null) {
//                            dialog.dismissLoadingDialog();
//                        }
                        Log.i("saveImageProduct", "onSuccess: Upload Image URI is " + compressUri.toString());
                    } else {
                        Log.i("saveImageProduct", "save failed");

                    }
                    file.delete();
                }
            });

//            ProgressBar mProgressBar = null;
//            mProgressBar.setVisibility(ProgressBar.VISIBLE);
//            Bitmap bmp = null;
//            try {
//                bmp = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            //here you can choose quality factor in third parameter(ex. i choosen 25)
//            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
//            byte[] fileInBytes = baos.toByteArray();
//
//            StorageReference photoref = storageReference.child(uri.getLastPathSegment());
//
//            //here i am uploading
//            photoref.putBytes(fileInBytes).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // When the image has successfully uploaded, we get its download URL
//                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//
//                }
//            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        Log.i("saveImageProduct", "onSuccess: Upload Image URI is " + compressUri.toString());
//                    } else {
//                        Log.i("saveImageProduct", "save failed");
//
//                    }
//                }
//            });
        }

    }

    public void addImageProduct(final Uri uri, String imgName, Activity activity) {
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference image = storageReference.child("ProductPictures/" + imgName);
        if (uri != null) {
            final File file = new File(SiliCompressor.with(activity).compress(FileUtils.getPath(activity, uri)
                    , new File(activity.getCacheDir(), "temp")));
            final Uri compressUri = Uri.fromFile(file);
            image.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.i("saveImageProduct", "onSuccess: Upload Image URI is " + compressUri.toString());
                    } else {
                        Log.i("saveImageProduct", "save failed");

                    }
                    file.delete();
                }
            });

//            ProgressBar mProgressBar = null;
//            mProgressBar.setVisibility(ProgressBar.VISIBLE);
//            Bitmap bmp = null;
//            try {
//                bmp = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            //here you can choose quality factor in third parameter(ex. i choosen 25)
//            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
//            byte[] fileInBytes = baos.toByteArray();
//
//            StorageReference photoref = storageReference.child(uri.getLastPathSegment());
//
//            //here i am uploading
//            photoref.putBytes(fileInBytes).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // When the image has successfully uploaded, we get its download URL
//                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
//
//                }
//            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        Log.i("saveImageProduct", "onSuccess: Upload Image URI is " + compressUri.toString());
//                    } else {
//                        Log.i("saveImageProduct", "save failed");
//
//                    }
//                }
//            });
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
