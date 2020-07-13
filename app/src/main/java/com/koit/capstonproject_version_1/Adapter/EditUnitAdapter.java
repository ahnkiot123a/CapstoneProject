package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditUnitAdapter extends RecyclerView.Adapter<EditUnitAdapter.ViewHolder>  {
    List<Unit> unitArrayList;
    Context context;

    public EditUnitAdapter(List<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;
    }

    public EditUnitAdapter() {
    }

    @NonNull
    @Override
    public EditUnitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_edit_unit_recyclerview,parent,false);
        return new EditUnitAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EditUnitAdapter.ViewHolder holder, int position) {
        holder.etUnitName.setText(unitArrayList.get(position).getUnitName());
        holder.etUnitPrice.setText(unitArrayList.get(position).getUnitPrice()+"");
    }

    @Override
    public int getItemCount() {
        return unitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etUnitName, etUnitPrice;


        public TextInputEditText getEtUnitName() {
            return etUnitName;
        }

        public TextInputEditText getEtUnitPrice() {
            return etUnitPrice;
        }


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etUnitName =  itemView.findViewById(R.id.etUnitName);
            etUnitPrice =  itemView.findViewById(R.id.etUnitPrice);


        }    }
}
