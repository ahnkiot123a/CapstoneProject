package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.DetailProductActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    List<Product> listProduct;
    //item layout
    int resourse;
    Context context;

    public ItemAdapter(Context context, List<Product> list, int resourse) {
        this.listProduct = list;
        this.resourse = resourse;
        this.context = context;
    }

    //View Holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itemName;
        TextView itemQuantity;
        TextView itemPrice;
        TextView tvMinconvertRate;
        TextView tvBarcode;
        ConstraintLayout itemProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvProductName);
            itemQuantity = itemView.findViewById(R.id.tvProductQuantity);
            itemPrice = itemView.findViewById(R.id.tvProductPrice);
            imageView = itemView.findViewById(R.id.imgViewProductPicture);
            tvMinconvertRate = itemView.findViewById(R.id.tvMinconvertRate);
            tvBarcode = itemView.findViewById(R.id.tvBarcode);
            itemProduct = itemView.findViewById(R.id.itemProduct);
        }
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Product product = listProduct.get(position);
        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText(Money.getInstance().formatVN(getMinProductPrice(product.getUnits())));
        holder.itemQuantity.setText(getProductQuantity(product.getUnits()) + "");
        holder.tvMinconvertRate.setText(getMinUnitProductName(product.getUnits()));
        holder.tvBarcode.setText(product.getBarcode());
        if (product.getProductImageUrl() != null && !product.getProductImageUrl().isEmpty()) {
            StorageReference storagePicture = FirebaseStorage.getInstance().getReference().child("ProductPictures").child(product.getProductImageUrl());
            long ONE_MEGABYTE = 1024 * 1024;
            storagePicture.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Failed loaded uri: ", e.getMessage());
                }
            });
        }
        //save iamge offline
//        try {
//            final File localFile = File.createTempFile(product.getProductImageUrl(), "jpeg");
//            storagePicture.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Log.d("SaveFileFSuccess", taskSnapshot.toString());
//                    // Local temp file has been created
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    holder.imageView.setImageBitmap(bitmap);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Log.d("SaveFileFailed", exception.getMessage());
//                    // Handle any errors
//                }
//            });
//        } catch (IOException e) {
//            Log.d("SaveFileFailed", e.getMessage());
//        }
        holder.itemProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProduct = new Intent(context, DetailProductActivity.class);
                intentProduct.putExtra("product", product);
                intentProduct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentProduct);
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

    public String getMinUnitProductName(List<Unit> unitList) {
        String minProductName = "";
        if (unitList != null)
            for (Unit unit : unitList) {
                if (unit.getConvertRate() == 1) {
                    minProductName = unit.getUnitName();
                    break;
                }

            }
        return minProductName;
    }

    public long getProductQuantity(List<Unit> unitList) {
        long minProductQuantity = 0;
        if (unitList != null)
            for (Unit unit : unitList) {
                if (unit.getConvertRate() == 1) {
                    minProductQuantity = unit.getUnitQuantity();
                    break;
                }
            }
        return minProductQuantity;
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

}
