package com.koit.capstonproject_version_1.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.SortController;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.DebtPaymentDetailActivity;

import java.util.List;

public class DebtPaymentAdapter extends RecyclerView.Adapter<DebtPaymentAdapter.ViewHolder> {

    private List<DebtPayment> list;
    private Activity activity;

    public DebtPaymentAdapter(List<DebtPayment> list, Activity activity) {
        this.list = list;
        this.activity = activity;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt_payment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SortController.getInstance().sortDebtPaymentListByDate((list));
        final DebtPayment dp = list.get(position);
        holder.tvPayAmount.setText(Money.getInstance().formatVN(dp.getPayAmount()));
        holder.tvPayDate.setText(dp.getPayDate());
        holder.tvPayTime.setText(dp.getPayTime());
        holder.layoutDebtPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DebtPaymentDetailActivity.class);
                intent.putExtra(DebtPaymentDetailActivity.DEBT_PAYMENT_DETAIL, dp);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

         private TextView tvPayAmount, tvPayDate, tvPayTime;
         private ConstraintLayout layoutDebtPayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPayAmount = itemView.findViewById(R.id.tvPayAmount);
            tvPayDate = itemView.findViewById(R.id.tvPayDate);
            tvPayTime = itemView.findViewById(R.id.tvPayTime);
            layoutDebtPayment = itemView.findViewById(R.id.layoutDebtPayment);
        }
    }
}
