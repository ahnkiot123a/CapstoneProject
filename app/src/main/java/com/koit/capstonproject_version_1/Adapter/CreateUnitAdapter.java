package com.koit.capstonproject_version_1.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class CreateUnitAdapter extends RecyclerView.Adapter<CreateUnitAdapter.ViewHolder> {

    ArrayList<Integer> listNumberOrder;
    Context context;

    ArrayList<Unit> listUnitResult;


    public CreateUnitAdapter() {

    }

    public CreateUnitAdapter(Context context, ArrayList<Integer> listNumberOrder) {
        this.listNumberOrder = listNumberOrder;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_unit_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int num = listNumberOrder.get(position);
        holder.setTvNumberOrder(num);
    }

    @Override
    public int getItemCount() {
        return listNumberOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText etUnitName, etUnitPrice;
        TextView tvNumberOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etUnitName = itemView.findViewById(R.id.etUnitName);
            etUnitPrice = itemView.findViewById(R.id.etUnitPrice);
            tvNumberOrder = itemView.findViewById(R.id.tvNumberOrder);
        }

        public void setTvNumberOrder(int numberOrder){
            tvNumberOrder.setText(String.valueOf(numberOrder));
        }
    }
}
