package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Controller.SortController;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.DebitOfDebtorActivity;
import com.koit.capstonproject_version_1.helper.Helper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListDebtorAdapter extends RecyclerView.Adapter<ListDebtorAdapter.ViewHolder> implements Filterable  {
    List<Debtor> listFiltered;
    Context context;
    List<Debtor> debtorList;
    LinearLayout layout_not_found_item;
    TextView tvRemaining;

    public ListDebtorAdapter(List<Debtor> debtorList, Context context, LinearLayout layout_not_found_item ) {
        this.debtorList = debtorList;
        this.listFiltered = debtorList;
        this.context = context;
        this.layout_not_found_item = layout_not_found_item;
    }

    public ListDebtorAdapter() {
    }

    @NonNull
    @Override
    public ListDebtorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_in_list_debtor_layout, parent, false);
        return new ListDebtorAdapter.ViewHolder(itemView);
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
    public void onBindViewHolder(@NonNull ListDebtorAdapter.ViewHolder holder, int position) {
        if (!listFiltered.isEmpty()) {
            SortController.getInstance().sortDebtorListByDebitAmount(listFiltered);
            final Debtor debtor = listFiltered.get(position);
            holder.tvDebitorName.setText(debtor.getFullName());
            holder.tvDebitorPhone.setText(debtor.getPhoneNumber());
            holder.tvDebtTotalAmount.setText(Money.getInstance().formatVN(debtor.getRemainingDebit()) + " đ");
//        holder.tvFirstDebtorName.setText(debtor.getFullName().charAt(0)+"");
            Helper.getInstance().setImage(holder.imgAvatar,holder.tvFirstDebtorName,debtor.getFullName().charAt(0));
            holder.itemDebtor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DebitOfDebtorActivity.class);
                    intent.putExtra(DebitOfDebtorActivity.ITEM_DEBTOR, debtor);
                    context.startActivity(intent);
                }
            });
        }



    }
    public long getDebitTotal(){
        long debitTotal = 0;
        for (Debtor d : debtorList){
            debitTotal += d.getRemainingDebit();
        }
        return  debitTotal;
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
                if (listFiltered != null){
                    if (listFiltered.size() == 0)  layout_not_found_item.setVisibility(View.VISIBLE);
                    else layout_not_found_item.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDebitorName, tvDebitorPhone, tvDebtTotalAmount,tvFirstDebtorName;
        private ConstraintLayout itemDebtor;
        private CircleImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDebitorName = itemView.findViewById(R.id.tvDebtorName);
            tvDebitorPhone = itemView.findViewById(R.id.tvDebtorPhone);
            tvDebtTotalAmount = itemView.findViewById(R.id.tvDebtTotalAmount);
            itemDebtor = itemView.findViewById(R.id.itemDebtor);
            tvFirstDebtorName = itemView.findViewById(R.id.tvFirstName);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
