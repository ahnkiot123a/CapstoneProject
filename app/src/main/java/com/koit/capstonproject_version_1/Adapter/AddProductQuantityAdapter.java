package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class AddProductQuantityAdapter extends RecyclerView.Adapter<AddProductQuantityAdapter.ViewHolder> {
    List<Unit> unitArrayList;
    Context context;
    private Unit unit = new Unit();

    public AddProductQuantityAdapter(List<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;

    }

    public AddProductQuantityAdapter() {
    }
    public List<Unit> getListUnitAdd() {

        return unitArrayList;
    }

    @NonNull
    @Override
    public AddProductQuantityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_add_quantity_recyclerview,parent,false);
        return new AddProductQuantityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddProductQuantityAdapter.ViewHolder holder, final int position) {
        holder.etUnitName.setText(unitArrayList.get(position).getUnitName());
        holder.etProductQuantity.setText("0");
        //unitArrayList.get(position).setUnitQuantity(0);
        holder.ibAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(String.valueOf(holder.etProductQuantity.getText()));
                holder.etProductQuantity.setText(quantity+1+"");
            }
        });
        holder.ibMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(String.valueOf(holder.etProductQuantity.getText()));
               if(quantity > 0) holder.etProductQuantity.setText(quantity-1+"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return unitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etUnitName, etProductQuantity;
        ImageButton ibMinusQuantity, ibAddQuantity;

        public TextInputEditText getEtUnitName() {
            return etUnitName;
        }

        public TextInputEditText getEtProductQuantity() {
            return etProductQuantity;
        }


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etUnitName =  itemView.findViewById(R.id.etUnitName);
            etProductQuantity =  itemView.findViewById(R.id.etProductQuantity);
            ibMinusQuantity =  itemView.findViewById(R.id.ibMinusQuantity);
            ibAddQuantity =  itemView.findViewById(R.id.ibAddQuantity);

        }
    }
}
