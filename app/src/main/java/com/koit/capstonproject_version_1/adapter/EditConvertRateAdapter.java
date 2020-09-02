package com.koit.capstonproject_version_1.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.koit.capstonproject_version_1.model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.List;

public class EditConvertRateAdapter extends RecyclerView.Adapter<EditConvertRateAdapter.ViewHolder>  {
    List<Unit> unitArrayList;
    Context context;

    public EditConvertRateAdapter(List<Unit> unitArrayList, Context context) {
        this.unitArrayList = unitArrayList;
        this.context = context;
    }

    public EditConvertRateAdapter() {
    }

    @NonNull
    @Override
    public EditConvertRateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.custom_edit_convert_rate_recyclerview,parent,false);
        return new EditConvertRateAdapter.ViewHolder(itemView);
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
    public void onBindViewHolder(@NonNull final EditConvertRateAdapter.ViewHolder holder, int position) {
        holder.etBigUnitName.setText("1 "+ unitArrayList.get(position).getUnitName() );
        holder.etSmallUnitName.setText(unitArrayList.get(unitArrayList.size()-1).getUnitName());
        holder.etConvertRate.setText(unitArrayList.get(position).getConvertRate()+"");
        holder.etConvertRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String convertRateString = s.toString();
                if (convertRateString.length() > 3) {
                    holder.etConvertRate.setText(convertRateString.substring(0, convertRateString.length() - 1));
                }
                if (convertRateString.startsWith("00")
                        || convertRateString.equals(""))
                    holder.etConvertRate.setText(0 + "");
                for (int i = 0; i <= 9; i++) {
                    if (convertRateString.equals("0" + i)) holder.etConvertRate.setText(i + "");
                }
                int convertRate = 0;
                try {
                    convertRate = Integer.parseInt(convertRateString);
                } catch (Exception e){
                    convertRate = 0;
                }
                if (convertRate == 0 || convertRateString.length() == 1) {
                    holder.etConvertRate.setSelection(holder.etConvertRate.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return unitArrayList.size() - 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText etBigUnitName, etConvertRate,etSmallUnitName;

        public TextInputEditText getEtBigUnitName() {
            return etBigUnitName;
        }

        public TextInputEditText getEtConvertRate() {
            return etConvertRate;
        }

        public TextInputEditText getEtSmallUnitName() {
            return etSmallUnitName;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etBigUnitName = itemView.findViewById(R.id.etBigUnitName);
            etConvertRate = itemView.findViewById(R.id.etConvertRate);
            etSmallUnitName = itemView.findViewById(R.id.etSmallUnitName);


        }
    }
}
