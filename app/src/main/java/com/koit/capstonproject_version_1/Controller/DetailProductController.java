package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koit.capstonproject_version_1.Adapter.ConvertRateRecyclerAdapter;
import com.koit.capstonproject_version_1.Adapter.UnitRecyclerAdapter;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.View.ListProductActivity;
import com.koit.capstonproject_version_1.dao.CreateProductDAO;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailProductController {
    private Activity activity;

    public DetailProductController() {
    }

    public DetailProductController(Activity activity) {
        this.activity = activity;
    }

    public void setProductImageView(final ImageView productImage, Product product) {
//        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty()) {
//            StorageReference storagePicture = FirebaseStorage.getInstance().getReference().child("ProductPictures").child(product.getProductImageUrl());
//            try {
//                final File localFile = File.createTempFile(product.getProductImageUrl(), "jpeg");
//                storagePicture.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Log.d("SaveFileFSuccess", taskSnapshot.toString());
//                        // Local temp file has been created
//                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                        productImage.setImageBitmap(bitmap);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Log.d("SaveFileFailed", exception.getMessage());
//                        // Handle any errors
//                    }
//                });
//            } catch (IOException e) {
//                Log.d("SaveFileFailed", e.getMessage());
//            }
//        }

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty() && !product.getProductImageUrl().equals("")) {
            storageReference.child("ProductPictures").child(product.getProductImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
                        return;
                    } else {
                        Glide.with(activity)
                                .load(uri)
                                .centerCrop()
                                .into(productImage);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                }
            });
        } else {

        }
    }

    public void sortUnitByPrice(ArrayList<Unit> unitList) {
        Collections.sort(unitList, new Comparator<Unit>() {
            @Override
            public int compare(Unit o1, Unit o2) {
                return (int) (o2.getUnitPrice() - o1.getUnitPrice());
            }
        });
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

    public void setRecyclerConvertRate(ArrayList<Unit> listUnit, LinearLayout linearConvertRate, RecyclerView recyclerConvertRate) {
        if (listUnit.size() < 2) {
            linearConvertRate.setVisibility(View.GONE);
//            btnEditConvertRate.setVisibility(View.INVISIBLE);
        }
        recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        recyclerConvertRate.setLayoutManager(linearLayoutManager1);

        ConvertRateRecyclerAdapter convertRateRecyclerAdapter = new ConvertRateRecyclerAdapter(listUnit, activity.getApplicationContext());
        recyclerConvertRate.setAdapter(convertRateRecyclerAdapter);
    }

    public void setRecyclerConvertRate(ArrayList<Unit> listUnit, TextView tvConvertRate,
                                       RecyclerView recyclerConvertRate) {
        if (listUnit.size() < 2) {
            tvConvertRate.setVisibility(View.INVISIBLE);
        }
        recyclerConvertRate.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(activity);
        recyclerConvertRate.setLayoutManager(linearLayoutManager1);

        ConvertRateRecyclerAdapter convertRateRecyclerAdapter = new ConvertRateRecyclerAdapter(listUnit, activity.getApplicationContext());
        recyclerConvertRate.setAdapter(convertRateRecyclerAdapter);
    }

    public void setSpinnerUnit(final ArrayList<Unit> listUnit, Spinner spinnerUnit, final TextView tvUnitQuantity) {
        ArrayList<String> listUnitname = new ArrayList<>();
        for (int i = 0; i < listUnit.size(); i++) {
            listUnitname.add(listUnit.get(i).getUnitName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listUnitname);
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

    public void removeProduct(Product product) {
        product.removeProduct(product);
        Unit unit = new Unit();
        unit.removeProductUnits(UserDAO.getInstance().getUserID(), product.getProductId());
        Intent intent = new Intent(activity, ListProductActivity.class);
        activity.startActivity(intent);
        Toast.makeText(activity.getApplicationContext(), "Bạn đã xoá thành công sản phẩm", Toast.LENGTH_LONG).show();
    }
}
