package com.koit.capstonproject_version_1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class ItemUnitInProductAdapter extends RecyclerView.Adapter<ItemUnitInProductAdapter.ViewHolder>{

    List<Unit> list;

    public ItemUnitInProductAdapter(List<Unit> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unit_in_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position > -1){
            Unit unit = list.get(position);
            long quantity = unit.getUnitQuantity();
            long price = unit.getUnitPrice();
            holder.tvProductQuantity.setText(quantity + "");
            holder.tvUnitName.setText(unit.getUnitName());
            holder.tvUnitPrice.setText(Money.getInstance().formatVN(price));
            holder.tvTotalPrice.setText(Money.getInstance().formatVN(quantity*price));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductQuantity, tvUnitName, tvUnitPrice, tvTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);

        }
    }
}
