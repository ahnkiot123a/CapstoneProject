package com.koit.capstonproject_version_1.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.koit.capstonproject_version_1.Controller.SortController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class DraftOrderAdapter extends RecyclerView.Adapter<DraftOrderAdapter.ViewHolder> {
    private final ArrayList<Invoice> list;
    private TextView tvCount;
    private Activity context;
    private DraftOrderAdapter.OnItemClickListener mListener;

    public boolean showShimmer = true;
    private final int SHIMMER_ITEM_NUMBER = 1;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(DraftOrderAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public DraftOrderAdapter(ArrayList<Invoice> list, Activity context, TextView tvCount) {
        this.list = list;
        this.context = context;
        this.tvCount = tvCount;
    }


    @NonNull
    @Override
    public DraftOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft_order, parent, false);
        return new DraftOrderAdapter.ViewHolder(view, mListener);
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
    public void onBindViewHolder(@NonNull DraftOrderAdapter.ViewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            if (!list.isEmpty()) {
                SortController.getInstance().sortInvoiceListByDate(this.list);
                holder.invoiceItemContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
//                tvCount.setText(list.size() + " hoá đơn tạm");
                Invoice invoice = list.get(position);

                holder.tvOrderId.setBackground(null);
                holder.tvOrderId.setText(invoice.getInvoiceId());

                holder.tvOrderDate.setBackground(null);
                holder.tvOrderDate.setText(invoice.getInvoiceDate());

                holder.tvOrderTime.setBackground(null);
                holder.tvOrderTime.setText(invoice.getInvoiceTime());

                holder.tvTotalPrice.setBackground(null);
                holder.tvTotalPrice.setText(Money.getInstance().formatVN(invoice.getTotal()) + " đ");

                holder.ivPencil.setBackground(null);
                holder.ivPencil.setImageDrawable(context.getDrawable(R.drawable.icons8_pencil_tip));
            }
        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView tvOrderId, tvOrderDate, tvOrderTime, tvTotalPrice;
        ImageView ivPencil;
        RelativeLayout invoiceItemContainer;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivPencil = itemView.findViewById(R.id.ivPencil);
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
