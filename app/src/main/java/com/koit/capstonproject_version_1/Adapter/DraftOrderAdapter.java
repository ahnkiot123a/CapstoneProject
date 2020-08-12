package com.koit.capstonproject_version_1.Adapter;

import android.app.Activity;
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
import com.koit.capstonproject_version_1.Controller.InvoiceHistoryController;
import com.koit.capstonproject_version_1.Controller.SortController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class DraftOrderAdapter extends RecyclerView.Adapter<DraftOrderAdapter.ViewHolder> implements Filterable {
    private final ArrayList<Invoice> list;
    private ArrayList<Invoice> listFiltered;
    private TextView tvCount;
    private Activity context;
    public boolean showShimmer = true;

    private final int SHIMMER_ITEM_NUMBER = 1;

    public DraftOrderAdapter(ArrayList<Invoice> list, Activity context, TextView tvCount) {
        this.list = list;
        this.listFiltered = list;
        this.context = context;
        this.tvCount = tvCount;
    }


    @NonNull
    @Override
    public DraftOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft_order, parent, false);
        return new DraftOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DraftOrderAdapter.ViewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            if (!list.isEmpty()) {
                SortController.getInstance().sortInvoiceListByDate(this.list);
                if (list.size() != 1) {
                    holder.invoiceItemContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
                }
                tvCount.setText(listFiltered.size() + " đơn hàng tạm");
                Invoice invoice = listFiltered.get(position);

                holder.tvOrderId.setBackground(null);
                holder.tvOrderId.setText(invoice.getInvoiceId());

                InvoiceHistoryController controller = new InvoiceHistoryController(context);

                holder.tvCustomer.setBackground(null);
                if (invoice.getDebtorId().isEmpty()) {
                    holder.tvCustomer.setText("Khách lẻ");
                } else {
                    controller.fillDebtorName(invoice.getDebtorId(), holder.tvCustomer);
                }
                holder.tvOrderDate.setBackground(null);
                holder.tvOrderDate.setText(invoice.getInvoiceDate());

                holder.tvOrderTime.setBackground(null);
                holder.tvOrderTime.setText(invoice.getInvoiceTime());

                holder.tvTotalPrice.setBackground(null);
                holder.tvTotalPrice.setText(Money.getInstance().formatVN(invoice.getTotal()) + " đ");

                holder.ivPencil.setBackground(null);
                holder.ivPencil.setImageDrawable(context.getDrawable(R.drawable.icons8_pencil_tip));
            } else {
                tvCount.setText("0 đơn hàng tạm");
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
                if (key.isEmpty()) {
                    listFiltered = list;
                } else {
                    ArrayList<Invoice> lstFiltered = new ArrayList<>();
                    for (Invoice iv : list) {
                        if (iv.getInvoiceId().toLowerCase().contains(key.toLowerCase())
                                || iv.getDebtorId().toLowerCase().contains(key.toLowerCase())) {
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
                tvCount.setText(listFiltered.size() + " đơn hàng tạm");
                notifyDataSetChanged();
            }
        };

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView tvOrderId, tvCustomer, tvOrderDate, tvOrderTime, tvTotalPrice;
        ImageView ivPencil;
        RelativeLayout invoiceItemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivPencil = itemView.findViewById(R.id.ivPencil);
            invoiceItemContainer = itemView.findViewById(R.id.invoiceItemContainer);

        }
    }
}
