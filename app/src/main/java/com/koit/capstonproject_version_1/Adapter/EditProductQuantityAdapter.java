package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditProductQuantityAdapter extends RecyclerView.Adapter<EditProductQuantityAdapter.ViewHolder> {
    List<Unit> unitArrayList;
    Context context;

    public EditProductQuantityAdapter(List<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;

    }

    public EditProductQuantityAdapter() {
    }

    @NonNull
    @Override
    public EditProductQuantityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_add_quantity_rv, parent, false);
        return new EditProductQuantityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EditProductQuantityAdapter.ViewHolder holder, final int position) {
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
        return unitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText etProductQuantity;
        TextView tvUnitName;
        ImageButton ibMinusQuantity, ibAddQuantity;
        Spinner spinnerChooseType;

        public TextView getTvUnitName() {
            return tvUnitName;
        }

        public EditText getEtProductQuantity() {
            return etProductQuantity;
        }

        public Spinner getSpinnerChooseType() {
            return spinnerChooseType;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            etProductQuantity = itemView.findViewById(R.id.etProductQuantity);
            ibMinusQuantity = itemView.findViewById(R.id.ibMinusQuantity);
            ibAddQuantity = itemView.findViewById(R.id.ibAddQuantity);
            spinnerChooseType = itemView.findViewById(R.id.spinnerChooseType);

            String [] type = {"Thêm", "Bớt"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, type);
            spinnerChooseType.setAdapter(adapter);

        }
    }
}
