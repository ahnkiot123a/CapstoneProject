package com.koit.capstonproject_version_1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.model.Product;
import com.koit.capstonproject_version_1.model.Unit;
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
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tvProductTotal.setText("(" + getProductTotal() + ")");
        if(position > -1){
            Product product = list.get(position);
            holder.tvProductName.setText(product.getProductName());
            List<Unit> units = product.getUnits();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            holder.rvUnit.setLayoutManager(layoutManager);
            ItemUnitInProductAdapter unitInProductAdapter = new ItemUnitInProductAdapter(units);
            holder.rvUnit.setAdapter(unitInProductAdapter);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
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

        private TextView tvProductName;
        private RecyclerView rvUnit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            rvUnit = itemView.findViewById(R.id.rvUnit);

        }
    }
}
