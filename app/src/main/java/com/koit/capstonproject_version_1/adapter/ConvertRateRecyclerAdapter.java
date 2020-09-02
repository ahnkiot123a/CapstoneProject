package com.koit.capstonproject_version_1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.model.Unit;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_layout_recyclerview_convert_rate,parent,false);
        return new ConvertRateRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int last = unitArrayList.size()-1;
        holder.tvSmallestUnitName.setText(unitArrayList.get(position).getConvertRate() + "  " + unitArrayList.get(last).getUnitName());
        holder.tvBigUnitName.setText("1 " + unitArrayList.get(position).getUnitName() );
    }

    @Override
    public int getItemCount() {
       return unitArrayList.size() - 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBigUnitName, tvSmallestUnitName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBigUnitName = itemView.findViewById(R.id.tvBigUnitName);
            tvSmallestUnitName = itemView.findViewById(R.id.tvSmallestUnitName);
        }

    }
}
