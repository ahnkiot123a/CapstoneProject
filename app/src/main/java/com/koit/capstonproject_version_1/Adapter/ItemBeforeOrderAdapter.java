package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ItemBeforeOrderAdapter extends RecyclerView.Adapter<ItemBeforeOrderAdapter.MyViewHolder> {

    private List<Product> listProduct;
    private int resourse;
    private Context context;
    private CheckBox checkBoxCount;

    public ItemBeforeOrderAdapter(Context context, List<Product> list, int resourse, CheckBox checkBoxCount) {
        this.listProduct = list;
        this.resourse = resourse;
        this.context = context;
        this.checkBoxCount = checkBoxCount;
    }

    //View Holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itemName;
        TextView itemPrice;
        TextView tvBarcode;
        ConstraintLayout itemProduct;
        ImageView imageViewCheckIcon;
        LinearLayout layoutCount;
        TextView itemCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvProductName);
            itemPrice = itemView.findViewById(R.id.tvProductPrice);
            imageView = itemView.findViewById(R.id.imgViewProductPicture);
            tvBarcode = itemView.findViewById(R.id.tvBarcode);
            imageViewCheckIcon = itemView.findViewById(R.id.widget_title_icon);
            layoutCount = itemView.findViewById(R.id.linearCountMultiSelectItem);
            itemCount = itemView.findViewById(R.id.itemQuantity);
            itemProduct = itemView.findViewById(R.id.itemProduct);
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Product product = listProduct.get(position);
        //select product
        final int[] countItemSelected = {0};

        //set Value for Holder
        holder.itemName.setText(product.getProductName());
        holder.itemPrice.setText(getMinProductPrice(product.getUnits()) + "");
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
//    }
        holder.itemProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selected items
                if (checkBoxCount.isChecked()) {
                    setVisibleLayout(holder.imageViewCheckIcon, holder.layoutCount);
                    countItemSelected[0]++;
                    holder.itemCount.setText(countItemSelected[0] + "");
                    Log.d(holder.itemName + "", countItemSelected.toString());
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

    private void setGoneLayout(ImageView imageViewCheck, LinearLayout linearLayoutCount) {
        imageViewCheck.setVisibility(View.GONE);
        linearLayoutCount.setVisibility(View.GONE);
    }

    private void setVisibleLayout(ImageView imageViewCheck, LinearLayout linearLayoutCount) {
        imageViewCheck.setVisibility(View.VISIBLE);
        linearLayoutCount.setVisibility(View.VISIBLE);
    }
}
