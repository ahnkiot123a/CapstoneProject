package com.koit.capstonproject_version_1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class DebtPaymentDetailAdapter extends RecyclerView.Adapter<DebtPaymentDetailAdapter.ViewHolder> {
    private List<Invoice> list;

    public DebtPaymentDetailAdapter(List<Invoice> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt_payment_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Invoice invoice = list.get(position);
        holder.tvInvoiceId.setText(invoice.getInvoiceId());
        holder.tvOrderDate.setText(invoice.getInvoiceDate());
        holder.tvOrderTime.setText(invoice.getInvoiceTime());
        holder.tvDebtPayMoney.setText(Money.getInstance().formatVN(invoice.getPayMoney()) + " đ");
        long remainingDebt = invoice.getDebitAmount();
        if (remainingDebt > 0){
            holder.tvRemainingDebt.setText(Money.getInstance().formatVN(remainingDebt) + " đ");
            holder.tvRemainingDebtStatus.setVisibility(View.GONE);
        }else if(remainingDebt == 0 ){
            holder.tvRemainingDebtTitle.setVisibility(View.GONE);
            holder.tvRemainingDebt.setVisibility(View.GONE);
            holder.tvRemainingDebtStatus.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvInvoiceId, tvOrderDate, tvOrderTime, tvDebtPayMoney, tvRemainingDebt, tvRemainingDebtStatus, tvRemainingDebtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInvoiceId = itemView.findViewById(R.id.tvInvoiceId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvDebtPayMoney = itemView.findViewById(R.id.tvDebtPayMoney);
            tvRemainingDebt = itemView.findViewById(R.id.tvRemainingDebt);
            tvRemainingDebtTitle = itemView.findViewById(R.id.tvRemainingDebtTitle);
            tvRemainingDebtStatus = itemView.findViewById(R.id.tvRemainingDebtStatus);

        }
    }
}
