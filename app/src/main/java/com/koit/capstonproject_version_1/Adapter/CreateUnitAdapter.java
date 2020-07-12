package com.koit.capstonproject_version_1.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.media.tv.TvView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

public class CreateUnitAdapter extends RecyclerView.Adapter<CreateUnitAdapter.ViewHolder> {

    private OnItemClickLister mLister;

    private ArrayList<Integer> listNumberOrder;
    private Context context;

    private ArrayList<Unit> listUnitResult;
    private Unit unit = new Unit();

    public ArrayList<Unit> getListUnitResult() {
        return listUnitResult;
    }

    public CreateUnitAdapter() {

    }

    public CreateUnitAdapter(Context context, ArrayList<Integer> listNumberOrder) {
        this.listNumberOrder = listNumberOrder;
        this.context = context;
        listUnitResult = new ArrayList<>();
    }

    public interface OnItemClickLister{
        void onDeleteClick(int position);
    }

    public void setOnItemClickLister(OnItemClickLister lister){
        mLister = lister;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_unit_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mLister);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String unitName = holder.etUnitName.getText().toString().trim();
//        String unitPrice = holder.etUnitPrice.getText().toString().trim();
//        if (!unitName.isEmpty() && !unitPrice.isEmpty()){
//
//            unit.setUnitName(unitName);
//            unit.setUnitPrice(Long.parseLong(unitName));
//            listUnitResult.add(unit);
//        }
    }

    @Override
    public int getItemCount() {
        return listNumberOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText etUnitName, etUnitPrice;
        TextView tvNumberOrder;
        ImageButton ibtnDeleteUnit;

        public EditText getEtUnitName() {
            return etUnitName;
        }

        public void setEtUnitName(EditText etUnitName) {
            this.etUnitName = etUnitName;
        }

        public EditText getEtUnitPrice() {
            return etUnitPrice;
        }

        public void setEtUnitPrice(EditText etUnitPrice) {
            this.etUnitPrice = etUnitPrice;
        }

        public ViewHolder(@NonNull final View itemView, final OnItemClickLister lister) {
            super(itemView);
            etUnitName = itemView.findViewById(R.id.etUnitName);
            etUnitPrice = itemView.findViewById(R.id.etUnitPrice);
            tvNumberOrder = itemView.findViewById(R.id.tvNumberOrder);
            ibtnDeleteUnit = itemView.findViewById(R.id.ibtnDeleteUnit);

            ibtnDeleteUnit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lister != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION && listNumberOrder.size() > 1){
                            lister.onDeleteClick(position);
                            
                        }
                    }
                }
            });

            etUnitName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i("unitname", s.toString());
                    if(s.length() > 0){
                        unit.setUnitName(s.toString());
                    }
                }
            });

            etUnitPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i("unitname", s.toString());

                    if(s.length() > 0){
                        unit.setUnitPrice(Long.parseLong(s.toString()));
                    }
                }
            });
        }

        public void setTvNumberOrder(int numberOrder){
            tvNumberOrder.setText(String.valueOf(numberOrder));
        }
    }
}
