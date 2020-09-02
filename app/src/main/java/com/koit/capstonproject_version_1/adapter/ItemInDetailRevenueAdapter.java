package com.koit.capstonproject_version_1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.R;

import java.util.List;
import java.util.Map;

public class ItemInDetailRevenueAdapter extends RecyclerView.Adapter<ItemInDetailRevenueAdapter.ViewHolder> {

    private List<Map<String, Long>> list;

    public ItemInDetailRevenueAdapter(List<Map<String, Long>> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ItemInDetailRevenueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_detail_revenue, parent, false);
        ItemInDetailRevenueAdapter.ViewHolder viewHolder = new ItemInDetailRevenueAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Long> map  = list.get(position);
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            holder.tvDate.setText(entry.getKey());
            holder.tvTotalInTime.setText(Money.getInstance().formatVN(entry.getValue()));
        }
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    //View Holder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvTotalInTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalInTime = itemView.findViewById(R.id.tvTotalInTime);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}