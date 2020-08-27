package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.InvoiceDetail;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.DebitConfirmationActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectDebtorAdapter extends RecyclerView.Adapter<SelectDebtorAdapter.ViewHolder> implements Filterable {
    List<Debtor> debtorList;
    List<Debtor> listFiltered;
    Context context;
    Invoice invoice;
    InvoiceDetail invoiceDetail;
    List<Product> listSelectedProductWarehouse;

    public SelectDebtorAdapter(List<Debtor> debtorList, Context context, Invoice invoice, InvoiceDetail invoiceDetail, List<Product> listSelectedProductWarehouse) {
        this.debtorList = debtorList;
        this.context = context;
        this.listFiltered = debtorList;
        this.invoice = invoice;
        this.invoiceDetail = invoiceDetail;
        this.listSelectedProductWarehouse = listSelectedProductWarehouse;
    }

    public SelectDebtorAdapter(List<Debtor> debtorList, Context context) {
        this.debtorList = debtorList;
        this.listFiltered = debtorList;
        this.context = context;
    }

    public SelectDebtorAdapter() {
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_in_list_debtor_layout, parent, false);
        return new SelectDebtorAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Debtor debtor = listFiltered.get(position);
        holder.tvDebitorName.setText(debtor.getFullName());
        holder.tvDebitorPhone.setText(debtor.getPhoneNumber());
        holder.tvDebtTotalAmount.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
        holder.tvFirstDebtorName.setText(debtor.getFullName().charAt(0)+"");
        holder.itemDebtor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDebtor = new Intent(context, DebitConfirmationActivity.class);
                intentDebtor.putExtra("debtor", debtor);
                intentDebtor.putExtra("invoice", invoice);
                intentDebtor.putExtra("invoiceDetail", invoiceDetail);
                Bundle args2 = new Bundle();
                args2.putSerializable("listSelectedProductWarehouse", (Serializable) listSelectedProductWarehouse);
                intentDebtor.putExtra("BUNDLE", args2);

                //  invoice = SelectDebtorActivity.getInstance().getInvoice();
                //Log.d("InvoiceAdapter", invoice.toString());
                intentDebtor.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intentDebtor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFiltered != null ? listFiltered.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                if (key.isEmpty()) {
                    listFiltered = debtorList;
                } else {
                    List<Debtor> lstFiltered = new ArrayList<>();
                    for (Debtor iv : debtorList) {
                        if (
                                 iv.getFullName().toLowerCase().contains(key.toLowerCase())
                                || iv.getPhoneNumber().toLowerCase().contains(key.toLowerCase())
                        ) {
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
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (ArrayList<Debtor>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDebitorName, tvDebitorPhone, tvDebtTotalAmount,tvFirstDebtorName;
        private ConstraintLayout itemDebtor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDebitorName = itemView.findViewById(R.id.tvDebtorName);
            tvDebitorPhone = itemView.findViewById(R.id.tvDebtorPhone);
            tvDebtTotalAmount = itemView.findViewById(R.id.tvDebtTotalAmount);
            itemDebtor = itemView.findViewById(R.id.itemDebtor);
            tvFirstDebtorName = itemView.findViewById(R.id.tvFirstName);
        }
    }

}
