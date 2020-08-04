package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.DebitConfirmationActivity;
import com.koit.capstonproject_version_1.View.DetailProductActivity;

import java.util.List;

public class DebtorAdapter extends RecyclerView.Adapter<DebtorAdapter.ViewHolder> {
    List<Debtor> debtorList;
    Context context;

    public DebtorAdapter(List<Debtor> debtorList, Context context) {
        this.debtorList = debtorList;
        this.context = context;
    }

    public DebtorAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_debitor_layout,parent,false);
        return new DebtorAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Debtor debtor = debtorList.get(position);
        holder.tvDebitorName.setText(debtor.getFullName());
        holder.tvDebitorPhone.setText(debtor.getPhoneNumber());
        holder.tvDebitorId.setText(debtor.getDebtorId());
        holder.itemDebtor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDebtor = new Intent(context, DebitConfirmationActivity.class);
                intentDebtor.putExtra("debtor", debtor);
                intentDebtor.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentDebtor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return debtorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDebitorName, tvDebitorPhone, tvDebitorId;
        private ConstraintLayout itemDebtor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDebitorName = itemView.findViewById(R.id.tvDebitorName);
            tvDebitorPhone = itemView.findViewById(R.id.tvDebitorPhone);
            tvDebitorId = itemView.findViewById(R.id.tvDebitorId);
            itemDebtor = itemView.findViewById(R.id.itemDebtor);
        }
    }
}
