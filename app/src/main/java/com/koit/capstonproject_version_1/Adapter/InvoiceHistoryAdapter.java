package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class InvoiceHistoryAdapter extends RecyclerView.Adapter<InvoiceHistoryAdapter.ViewHolder> {

    private ArrayList<Invoice> list;
    private Context context;
    public boolean showShimmer = true;

    private final int SHIMMER_ITEM_NUMBER = 1;

    public InvoiceHistoryAdapter(ArrayList<Invoice> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            if (!list.isEmpty()) {
                Invoice invoice = list.get(position);

                holder.tvOrderId.setBackground(null);
                holder.tvOrderId.setText(invoice.getInvoiceId());

                holder.tvCustomer.setBackground(null);
                holder.tvCustomer.setText(invoice.getCustomerId());

                holder.tvOrderDate.setBackground(null);
                holder.tvOrderDate.setText(invoice.getInvoiceDate());

                holder.tvOrderTime.setBackground(null);
                holder.tvOrderTime.setText(invoice.getInvoiceTime());

                holder.tvTotalPrice.setBackground(null);
                holder.tvTotalPrice.setText(String.valueOf(invoice.getTotal()));

                holder.tvOrderStatus.setBackground(null);
                if (invoice.getDebitAmount() == 0) {
                    holder.tvOrderStatus.setText("Đã thanh toán");
                } else {
                    holder.tvOrderStatus.setTextColor(Color.rgb(236,135,14));
                    holder.tvOrderStatus.setText("Vẫn còn nợ");

                }
                holder.imageView.setBackground(null);
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.icons8_money));
            }

        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView tvOrderId, tvCustomer, tvOrderDate, tvOrderTime, tvTotalPrice, tvOrderStatus;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            imageView = itemView.findViewById(R.id.imageView);


        }
    }
}
