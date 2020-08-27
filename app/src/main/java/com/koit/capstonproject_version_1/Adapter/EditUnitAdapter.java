package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.MoneyEditText;

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
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull EditUnitAdapter.ViewHolder holder, int position) {
        holder.etUnitName.setText(unitArrayList.get(position).getUnitName());
        holder.etUnitPrice.setText(Money.getInstance().formatVN(unitArrayList.get(position).getUnitPrice())+"");
    }

    @Override
    public int getItemCount() {
        return unitArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etUnitName;
        MoneyEditText etUnitPrice;


        public TextInputEditText getEtUnitName() {
            return etUnitName;
        }

        public MoneyEditText getEtUnitPrice() {
            return etUnitPrice;
        }


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etUnitName =  itemView.findViewById(R.id.etUnitName);
            etUnitPrice =  itemView.findViewById(R.id.etUnitPrice);


        }    }
}
