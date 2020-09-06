package com.koit.capstonproject_version_1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class AddQuantityAdapter extends RecyclerView.Adapter<AddQuantityAdapter.ViewHolder> {

    List<Unit> unitArrayList = new ArrayList<>();
    Context context;

    public AddQuantityAdapter(List<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;

    }

    public AddQuantityAdapter() {
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
    public AddQuantityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_add_quantity, parent, false);
        return new AddQuantityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddQuantityAdapter.ViewHolder holder, final int position) {
        holder.tvUnitName.setText(unitArrayList.get(position).getUnitName());
        holder.etProductQuantity.setText("0");
        //unitArrayList.get(position).setUnitQuantity(0);
        holder.ibAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(String.valueOf(holder.etProductQuantity.getText()));
                } catch (Exception e) {
                    quantity = 0;
                }
                holder.etProductQuantity.setText(quantity + 1 + "");
            }
        });
        holder.ibMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(String.valueOf(holder.etProductQuantity.getText()));
                } catch (Exception e) {
                    quantity = 0;
                }
                if (quantity > 0) holder.etProductQuantity.setText(quantity - 1 + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return unitArrayList == null ? 0 : unitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText etProductQuantity;
        private TextView tvUnitName;
        private ImageButton ibMinusQuantity, ibAddQuantity;

        public EditText getEtProductQuantity() {
            return etProductQuantity;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            etProductQuantity = itemView.findViewById(R.id.etProductQuantity);
            ibMinusQuantity = itemView.findViewById(R.id.ibMinusQuantity);
            ibAddQuantity = itemView.findViewById(R.id.ibAddQuantity);

        }
    }
}
