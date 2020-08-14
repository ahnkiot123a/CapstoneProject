package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditConvertRateAdapter extends RecyclerView.Adapter<EditConvertRateAdapter.ViewHolder>  {
    List<Unit> unitArrayList;
    Context context;

    public EditConvertRateAdapter(List<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;
    }

    public EditConvertRateAdapter() {
    }

    @NonNull
    @Override
    public EditConvertRateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_edit_convert_rate_recyclerview,parent,false);
        return new EditConvertRateAdapter.ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull EditConvertRateAdapter.ViewHolder holder, int position) {
        holder.etBigUnitName.setText("1 "+ unitArrayList.get(position).getUnitName() );
        holder.etSmallUnitName.setText(unitArrayList.get(unitArrayList.size()-1).getUnitName());
        holder.etConvertRate.setText(unitArrayList.get(position).getConvertRate()+"");
    }

    @Override
    public int getItemCount() {
        return unitArrayList.size() - 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etBigUnitName, etConvertRate,etSmallUnitName;

        public TextInputEditText getEtBigUnitName() {
            return etBigUnitName;
        }

        public TextInputEditText getEtConvertRate() {
            return etConvertRate;
        }

        public TextInputEditText getEtSmallUnitName() {
            return etSmallUnitName;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etBigUnitName = itemView.findViewById(R.id.etBigUnitName);
            etConvertRate = itemView.findViewById(R.id.etConvertRate);
            etSmallUnitName = itemView.findViewById(R.id.etSmallUnitName);


        }
    }
}
