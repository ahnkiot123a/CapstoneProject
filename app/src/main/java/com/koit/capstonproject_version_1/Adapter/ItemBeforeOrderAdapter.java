package com.koit.capstonproject_version_1.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.SelectProductActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ItemBeforeOrderAdapter extends RecyclerView.Adapter<ItemBeforeOrderAdapter.MyViewHolder> {

    private List<Product> listProduct;
    private List<Product> listSelectedProduct;
    private int resourse;
    private Activity context;
    private CheckBox checkBoxCount;

    public ItemBeforeOrderAdapter(Activity context, List<Product> list, int resourse, CheckBox checkBoxCount, List<Product> listSelectedProduct) {
        this.listProduct = list;
        this.resourse = resourse;
        this.context = context;
        this.checkBoxCount = checkBoxCount;
        this.listSelectedProduct = listSelectedProduct;
    }

    //View Holder class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView itemName;
        TextView itemPrice;
        TextView tvBarcode;
        ConstraintLayout itemProduct;
        ImageView imageViewCheckIcon;
        ShimmerFrameLayout shimmerFrameLayout;
        //        LinearLayout layoutCount;
//        TextView itemCount;

        public ImageView getImageViewCheckIcon() {
            return imageViewCheckIcon;
        }

//        public LinearLayout getLayoutCount() {
//            return layoutCount;
//        }

//        public TextView getItemCount() {
//            return itemCount;
//        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvProductName);
            itemPrice = itemView.findViewById(R.id.tvProductPrice);
            imageView = itemView.findViewById(R.id.imgViewProductPicture);
            tvBarcode = itemView.findViewById(R.id.tvBarcode);
            imageViewCheckIcon = itemView.findViewById(R.id.widget_title_icon);
//            layoutCount = itemView.findViewById(R.id.linearCountMultiSelectItem);
//            itemCount = itemView.findViewById(R.id.itemQuantity);
            itemProduct = itemView.findViewById(R.id.itemProduct);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
        }
    }

    @NonNull
    @Override
    public ItemBeforeOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_before_order, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Product product = listProduct.get(position);
        for (Product product1 : listSelectedProduct) {
            //set tick for selected product
            if (product1.getProductId().equals(product.getProductId())) {
                holder.imageViewCheckIcon.setVisibility(View.VISIBLE);
            }
        }
        //set Value for Holder
        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText(Money.getInstance().formatVN(getMinProductPrice(product.getUnits())));
        holder.tvBarcode.setText(product.getBarcode());
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        holder.shimmerFrameLayout.startShimmer();
        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty() && !product.getProductImageUrl().equals("")) {
            storageReference.child("ProductPictures").child(product.getProductImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.isDestroyed()) {
                        return;
                    } else {
                        Glide.with(context)
                                .load(uri)
                                .centerCrop()
                                .into(holder.imageView);
                        holder.shimmerFrameLayout.stopShimmer();
                        holder.shimmerFrameLayout.setShimmer(null);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    holder.shimmerFrameLayout.stopShimmer();
                    holder.shimmerFrameLayout.setShimmer(null);
                }
            });
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
        }

        //set Image for image view
//        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty()) {
//            StorageReference storagePicture = FirebaseStorage.getInstance().getReference().child("ProductPictures").
//                    child(product.getProductImageUrl());
//            Glide.with(context)
//                    .load(storagePicture)
//                    .fitCenter()
//                    .into(holder.imageView);
//            Log.d("ImageLink", storagePicture.toString());
//        }


        //https://firebase.google.com/docs/storage/android/download-files
        //save iamge offline
//        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty()) {
//            try {
//                final File localFile = File.createTempFile(product.getProductImageUrl(), "jpg");
//                StorageReference storagePicture = FirebaseStorage.getInstance().getReference().child("ProductPictures").child(product.getProductImageUrl());
//                storagePicture.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Log.d("SaveFileFSuccess", taskSnapshot.toString());
//                        // Local temp file has been created
//                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
////                        holder.imageView.setImageBitmap(bitmap);
//                        Glide.with(context)
//                            .load(bitmap)
//                            .fitCenter()
//                            .into(holder.imageView);
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
        holder.itemProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxCount.isChecked()) {
                    boolean isProductExist = false;
                    for (Product product1 : listSelectedProduct) {
                        //selected list contain searched product or not
                        if (product1.getProductId().equals(product.getProductId())) {
                            isProductExist = true;
                            listSelectedProduct.remove(product1);
                            break;
                        }
                    }
                    //contain
                    if (isProductExist) {
                        holder.imageViewCheckIcon.setVisibility(View.GONE);
                        Log.d("ListSelectedProductRe", listSelectedProduct.size() + "");
                    } else
                    //not contain
                    {
                        holder.imageViewCheckIcon.setVisibility(View.VISIBLE);
                        listSelectedProduct.add(product);
                        Log.d("ListSelectedProductAd", listSelectedProduct.size() + "");
                    }

                } else {
                    listSelectedProduct.add(product);
                    SelectProductActivity.getInstance().transferToListItemInOrder(listSelectedProduct);
                }
            }
        });
    }

    public long getMinProductPrice(List<Unit> unitList) {
        long minPrice = 0;
        Log.d("CheckList", unitList.toString());
        if (unitList != null)
            for (Unit unit : unitList) {
                Log.d("CheckListUnit", unit.toString());

                if (unit.getConvertRate() == 1) {
                    minPrice = unit.getUnitPrice();
                    break;
                }
            }
        return minPrice;
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

}
