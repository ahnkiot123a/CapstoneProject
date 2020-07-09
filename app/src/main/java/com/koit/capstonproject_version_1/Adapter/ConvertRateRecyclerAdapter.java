package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class ConvertRateRecyclerAdapter extends RecyclerView.Adapter<ConvertRateRecyclerAdapter.ViewHolder> {
    ArrayList<Unit> unitArrayList;
    Context context;

    public ConvertRateRecyclerAdapter() {
    }

    public ConvertRateRecyclerAdapter(ArrayList<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_layout_recyclerview_convert_rate,parent,false);
        return new ConvertRateRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSmallestUnitName.setText(unitArrayList.get(position+1).getConvertRate() + "  " + unitArrayList.get(0).getUnitName());
        holder.tvBigUnitName.setText("1 " + unitArrayList.get(position + 1).getUnitName() );
    }

    @Override
    public int getItemCount() {
       return unitArrayList.size() - 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBigUnitName, tvSmallestUnitName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBigUnitName = (TextView) itemView.findViewById(R.id.tvBigUnitName);
            tvSmallestUnitName = (TextView) itemView.findViewById(R.id.tvSmallestUnitName);
        }

    }
}
