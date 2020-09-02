package com.koit.capstonproject_version_1.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.view.DetailProductActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    List<Product> listProduct;
    //item layout
    int resourse;
    Activity context;

    public ItemAdapter(Activity context, List<Product> list, int resourse) {
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
        ShimmerFrameLayout shimmerFrameLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvProductName);
            itemQuantity = itemView.findViewById(R.id.tvProductQuantity);
            itemPrice = itemView.findViewById(R.id.tvProductPrice);
            imageView = itemView.findViewById(R.id.imgViewProductPicture);
            tvMinconvertRate = itemView.findViewById(R.id.tvMinconvertRate);
            tvBarcode = itemView.findViewById(R.id.tvBarcode);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            itemProduct = itemView.findViewById(R.id.itemProduct);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
                                .fitCenter()
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
