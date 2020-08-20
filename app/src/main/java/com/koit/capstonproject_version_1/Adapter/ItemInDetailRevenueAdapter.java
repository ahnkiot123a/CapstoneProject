package com.koit.capstonproject_version_1.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.koit.capstonproject_version_1.Controller.OrderHistoryController;
import com.koit.capstonproject_version_1.Controller.SortController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class ItemInDetailRevenueAdapter extends RecyclerView.Adapter<ItemInDetailRevenueAdapter.ViewHolder> {

    private List<Invoice> list;
    private ArrayList<Invoice> listFiltered;
    private TextView tvCount;
    private Activity context;
    private OnItemClickListener mListener;

    public boolean showShimmer = true;

    private final int SHIMMER_ITEM_NUMBER = 1;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ItemInDetailRevenueAdapter(ArrayList<Invoice> list, Activity context, TextView tvCount) {
        this.list = list;
        this.listFiltered = list;
        this.context = context;
        this.tvCount = tvCount;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view, mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            if (!listFiltered.isEmpty()) {
                SortController.getInstance().sortInvoiceListByDate(this.listFiltered);
                if (listFiltered.size() != 1) {
                    holder.invoiceItemContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
                }

                tvCount.setText(listFiltered.size() + " đơn hàng");
                Invoice invoice = listFiltered.get(position);

                holder.tvOrderId.setBackground(null);
                holder.tvOrderId.setText(invoice.getInvoiceId());

                OrderHistoryController controller = new OrderHistoryController(context);

                holder.tvCustomer.setBackground(null);
                if (invoice.getDebtorId().isEmpty() || invoice.getDebtorId() == null) {
                    holder.tvCustomer.setText("Khách lẻ");
                } else {
                    controller.fillDebtorName(invoice.getDebtorId(), holder.tvCustomer);
                }
                holder.tvOrderDate.setBackground(null);
                holder.tvOrderDate.setText(invoice.getInvoiceDate());
                holder.tvOrderTime.setBackground(null);
                holder.tvOrderTime.setText(invoice.getInvoiceTime());

                holder.tvTotalPrice.setBackground(null);
                holder.tvTotalPrice.setText(Money.getInstance().formatVN(invoice.getTotal()));

                holder.tvOrderStatus.setBackground(null);
                if (invoice.getDebitAmount() != 0) {
                    holder.tvOrderStatus.setTextColor(Color.rgb(236, 135, 14));
                    holder.tvOrderStatus.setText("Vẫn còn nợ");
                } else {
                    holder.tvOrderStatus.setTextColor(Color.rgb(50, 205, 50));
                    holder.tvOrderStatus.setText("Đã thanh toán");
                }
                holder.imageView.setBackground(null);
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.icons8_money));
            }

        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : listFiltered != null ? listFiltered.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView tvOrderId, tvCustomer, tvOrderDate, tvOrderTime, tvTotalPrice, tvOrderStatus;
        ImageView imageView;
        RelativeLayout invoiceItemContainer;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            imageView = itemView.findViewById(R.id.imageView);
            invoiceItemContainer = itemView.findViewById(R.id.invoiceItemContainer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}