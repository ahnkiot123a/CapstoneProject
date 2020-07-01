package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.R;

import java.util.List;

import androidx.annotation.NonNull;
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

        static View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public static void setDetails(String itemImage, String name, String quantity, String price) {
            TextView itemName = mView.findViewById(R.id.tvProductName);
            TextView itemQuantity = mView.findViewById(R.id.tvProductQuantity);
            TextView itemPrice = mView.findViewById(R.id.tvProductPrice);

            itemName.setText(name);
            itemQuantity.setText(quantity);
            itemPrice.setText(price);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = listProduct.get(position);
        MyViewHolder.setDetails(
                "list.get(position).getProductImageUrl()",
                product.getProductName(),
                product.getProductName(),
                product.getProductName()
        );
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }


}
