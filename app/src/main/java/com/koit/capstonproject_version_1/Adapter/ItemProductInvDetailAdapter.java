package com.koit.capstonproject_version_1.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class ItemProductInvDetailAdapter extends RecyclerView.Adapter<ItemProductInvDetailAdapter.ViewHolder> {

    private List<Product> list;
    private Activity activity;
    private TextView tvProductTotal;

    public ItemProductInvDetailAdapter(List<Product> list, Activity activity, TextView tvProductTotal) {
        this.list = list;
        this.activity = activity;
        this.tvProductTotal = tvProductTotal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_invoice_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tvProductTotal.setText("Tổng số lượng hàng (" + getProductTotal() + ")");
        if(position < list.size()){
            Product product = list.get(position);
            for(Unit unit : product.getUnits()){
                holder.tvProductName.setText(product.getProductName());
                holder.tvProductQuantity.setText(String.format("%d", unit.getUnitQuantity()));
                holder.tvUnitName.setText(unit.getUnitName());
                holder.tvUnitPrice.setText(Money.getInstance().formatVN(unit.getUnitPrice()));
                holder.tvTotalPrice.setText(Money.getInstance().formatVN(unit.getUnitQuantity()*unit.getUnitPrice()));
            }
        }


    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    public int getSize(){
        int size = 0;
        if(!list.isEmpty()){
            for(Product p : list){
                size += p.getUnits().size();
            }
        }
        return size;
    }

    public int getProductTotal(){
        int total = 0;
        if(!list.isEmpty()){
            for(Product p : list){
                for (Unit u : p.getUnits()){
                    total += u.getUnitQuantity();
                }
            }
        }
        return total;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductName, tvProductQuantity, tvUnitName, tvUnitPrice, tvTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);

        }
    }
}
