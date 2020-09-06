package com.koit.capstonproject_version_1.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.controller.OrderHistoryController;
import com.koit.capstonproject_version_1.controller.SortController;
import com.koit.capstonproject_version_1.helper.Helper;
import com.koit.capstonproject_version_1.helper.Money;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.view.InvoiceDetailActivity;
import com.koit.capstonproject_version_1.view.OrderHistoryActivity;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> implements Filterable {

    private final ArrayList<Invoice> list;
    private ArrayList<Invoice> listFiltered;
    private TextView tvCount;
    private Activity activity;
    public boolean showShimmer = true;

    private final int SHIMMER_ITEM_NUMBER = 1;

    public OrderHistoryAdapter(ArrayList<Invoice> list, Activity context, TextView tvCount) {
        this.list = list;
        this.listFiltered = list;
        this.activity = context;
        this.tvCount = tvCount;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            if (!listFiltered.isEmpty()) {
                SortController.getInstance().sortInvoiceListByDate(this.listFiltered);
                holder.invoiceItemContainer.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_transition_animation));
                tvCount.setText(listFiltered.size() + " đơn hàng");
                Invoice invoice = null;
                if (listFiltered.size() != 0) {
                    invoice = listFiltered.get(position);
                }
                if (invoice != null) {

                    holder.tvOrderId.setBackground(null);
                    holder.tvOrderId.setText(invoice.getInvoiceId());

                    OrderHistoryController controller = new OrderHistoryController(activity);

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
                        holder.tvOrderStatus.setText("Còn nợ: " + Money.getInstance().formatVN(invoice.getDebitAmount()) + " đ");
                    } else {
                        holder.tvOrderStatus.setTextColor(Color.rgb(50, 205, 50));
                        holder.tvOrderStatus.setText("Đã trả hết");
                    }
                    holder.imageView.setBackground(null);
                    holder.imageView.setImageDrawable(activity.getDrawable(R.drawable.icons8_money));
                    Invoice finalInvoice = invoice;
                    holder.invoiceItemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, InvoiceDetailActivity.class);
                            intent.putExtra(OrderHistoryActivity.INVOICE, finalInvoice);
                            activity.startActivity(intent);
                        }
                    });
                }

            }

        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : listFiltered != null ? listFiltered.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                Log.d("key", key);
                if (key.isEmpty()) {
                    listFiltered = list;
                } else {
                    ArrayList<Invoice> lstFiltered = new ArrayList<>();
                    for (Invoice iv : list) {
                        if (iv.getInvoiceId().trim().toLowerCase().contains(key.trim().toLowerCase())
                                || Helper.getInstance().deAccent(iv.getDebtorName().trim().toLowerCase())
                                .contains(Helper.getInstance().deAccent(key.trim().toLowerCase()))) {
                            lstFiltered.add(iv);
                        }
                    }
                    listFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<Invoice>) filterResults.values;
                if (listFiltered != null)
                    tvCount.setText(listFiltered.size() + " đơn hàng");
                notifyDataSetChanged();
            }
        };

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView tvOrderId, tvCustomer, tvOrderDate, tvOrderTime, tvTotalPrice, tvOrderStatus;
        ImageView imageView;
        RelativeLayout invoiceItemContainer;

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
            invoiceItemContainer = itemView.findViewById(R.id.invoiceItemContainer);

        }
    }
}