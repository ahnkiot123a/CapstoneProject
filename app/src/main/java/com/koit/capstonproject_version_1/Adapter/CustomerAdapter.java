package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Model.Customer;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    List<Customer> customerList;
    Context context;

    public CustomerAdapter(List<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    public CustomerAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_customer_layout,parent,false);
        return new CustomerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName, tvCustomerPhone;

        public TextView getTvCustomerName() {
            return tvCustomerName;
        }

        public void setTvCustomerName(TextView tvCustomerName) {
            this.tvCustomerName = tvCustomerName;
        }

        public TextView getTvCustomerPhone() {
            return tvCustomerPhone;
        }

        public void setTvCustomerPhone(TextView tvCustomerPhone) {
            this.tvCustomerPhone = tvCustomerPhone;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
        }
    }
}
