package com.koit.capstonproject_version_1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class DebtPaymentAdapter extends RecyclerView.Adapter<DebtPaymentAdapter.ViewHolder> {

    private List<DebtPayment> list;

    public DebtPaymentAdapter(List<DebtPayment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt_payment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DebtPayment dp = list.get(position);
        holder.tvPayAmount.setText(Money.getInstance().formatVN(dp.getPayAmount()));
        holder.tvPayDate.setText(dp.getPayDate());
        holder.tvPayTime.setText(dp.getPayTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvPayAmount, tvPayDate, tvPayTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPayAmount = itemView.findViewById(R.id.tvPayAmount);
            tvPayDate = itemView.findViewById(R.id.tvPayDate);
            tvPayTime = itemView.findViewById(R.id.tvPayTime);
        }
    }
}
