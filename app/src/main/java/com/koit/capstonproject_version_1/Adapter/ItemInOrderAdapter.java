package com.koit.capstonproject_version_1.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ItemInOrderAdapter extends RecyclerView.Adapter<ItemInOrderAdapter.MyViewHolder> {

    private List<Product> listSelectedProductInWareHouse;
    private int resourse;
    private Context context;
    private TextView tvTotalQuantity;
    private TextView tvTotalPrice;
    private List<Product> listSelectedProductInOrder;

    public ItemInOrderAdapter(Context context, int resourse, List<Product> listSelectedProduct,
                              TextView tvTotalQuantity, TextView tvTotalPrice, List<Product> listSelectedProductInOrder) {
        this.resourse = resourse;
        this.context = context;
        this.listSelectedProductInWareHouse = listSelectedProduct;
        this.tvTotalQuantity = tvTotalQuantity;
        this.tvTotalPrice = tvTotalPrice;
        this.listSelectedProductInOrder = listSelectedProductInOrder;
    }

    //View Holder class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        Spinner spinnerUnit;
        ConstraintLayout itemProduct;
        ImageButton imageButtonDecrease;
        ImageButton imageButtonIncrease;
        EditText editTextQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvProductName);
            itemPrice = itemView.findViewById(R.id.tvProductPrice);
            spinnerUnit = itemView.findViewById(R.id.spinnerUnit);
            itemProduct = itemView.findViewById(R.id.itemProduct);
            imageButtonDecrease = itemView.findViewById(R.id.imageButtonDecrease);
            imageButtonIncrease = itemView.findViewById(R.id.imageButtonIncrease);
            editTextQuantity = itemView.findViewById(R.id.editTextQuantity);
        }
    }

    @NonNull
    @Override
    public ItemInOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_in_order, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Product product = listSelectedProductInWareHouse.get(position);
        final Product productInOrder = listSelectedProductInOrder.get(position);
        //set Value for Holder
        holder.itemName.setText(product.getProductName());
        List<Unit> unitListIncrease = new ArrayList<>();
        unitListIncrease = product.getUnits();
        sortUnitIncreaseByPrice(unitListIncrease);
        setSpinnerUnit((ArrayList<Unit>) unitListIncrease, holder.spinnerUnit, holder.itemPrice, position, holder.editTextQuantity, productInOrder);

        //
        holder.editTextQuantity.setText(productInOrder.getUnits().get(0).getUnitQuantity() + "");
        //set total price
        long totalPrice = getTotalPrice(listSelectedProductInOrder);
        tvTotalPrice.setText(Money.getInstance().formatVN(totalPrice));
        //set Total quantity
        int totalQuantity = getTotalQuantity(listSelectedProductInOrder);
        tvTotalQuantity.setText(totalQuantity + "");

//        Log.d("ItemPriceTotal", tvTotalPrice.getText().toString());

        holder.imageButtonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(String.valueOf(holder.editTextQuantity.getText()));
                } catch (Exception e) {
                    quantity = 1;
                    holder.editTextQuantity.setText("1");
                }
                if (quantity > 1) holder.editTextQuantity.setText(quantity - 1 + "");
            }
        });
        holder.imageButtonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(String.valueOf(holder.editTextQuantity.getText()));
                } catch (Exception e) {
                    quantity = 1;
                    holder.editTextQuantity.setText("1");
                }
                holder.editTextQuantity.setText(quantity + 1 + "");
            }
        });
        holder.editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //update change to list
                //check number
                {
                    Unit unitInOrder = productInOrder.getUnits().get(0);
                    try {
                        Long.parseLong(holder.editTextQuantity.getText().toString());
                    } catch (Exception e) {
                        holder.editTextQuantity.setText("1");
                    }
                    unitInOrder.setUnitQuantity(Long.parseLong(holder.editTextQuantity.getText().toString()));
                    List<Unit> unitList = new ArrayList<>();
                    unitList.add(unitInOrder);
                    //update unit's quantity
                    productInOrder.setUnits(unitList);
                }
                //get total quantity
                int totalQuantity = getTotalQuantity(listSelectedProductInOrder);
                tvTotalQuantity.setText(totalQuantity + "");
                // update total price
                tvTotalPrice.setText(Money.getInstance().formatVN(getTotalPrice(listSelectedProductInOrder)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listSelectedProductInOrder.size();
    }

    public static int getTotalQuantity(List<Product> listSelectedProductInOrder) {
        int totalQuantity = 0;
        for (Product p : listSelectedProductInOrder
        ) {
            //each product in selected product contain only 1 unit
            Unit unitInOrder = p.getUnits().get(0);
            totalQuantity += unitInOrder.getUnitQuantity();
        }
        return totalQuantity;
    }


    public void setSpinnerUnit(final ArrayList<Unit> listUnit, Spinner spinnerUnit, final TextView tvUnitPrice,
                               final int positionProduct, final EditText editTextQuantity, Product product) {

        ArrayList<String> listUnitname = new ArrayList<>();

        for (int i = 0; i < listUnit.size(); i++) {
            listUnitname.add(listUnit.get(i).getUnitName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listUnitname);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(product.getUnits().get(0).getUnitName());
        spinnerUnit.setSelection(spinnerPosition);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvUnitPrice.setText(Money.getInstance().formatVN(listUnit.get(position).getUnitPrice()));
                //set Unit to listInOrder(name, price, quantity)
                List<Unit> unitList = new ArrayList<>();
                Unit unitInOrder = new Unit();
                unitInOrder.setUnitId(listUnit.get(position).getUnitId());
                unitInOrder.setUnitName(listUnit.get(position).getUnitName());
                unitInOrder.setUnitPrice(listUnit.get(position).getUnitPrice());

                //get quantity and update to UI
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(String.valueOf(editTextQuantity.getText()));
                } catch (Exception e) {
                    quantity = 1;
                    editTextQuantity.setText("1");
                }
                unitInOrder.setUnitQuantity(quantity);
                unitList.add(unitInOrder);
                listSelectedProductInOrder.get(positionProduct).setUnits(unitList);
                //set total price
                long totalPrice = getTotalPrice(listSelectedProductInOrder);
                tvTotalPrice.setText(Money.getInstance().formatVN(totalPrice));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void sortUnitIncreaseByPrice(List<Unit> unitList) {
        Collections.sort(unitList, new Comparator<Unit>() {
            @Override
            public int compare(Unit o1, Unit o2) {
                return (int) (o1.getUnitPrice() - o2.getUnitPrice());
            }
        });
    }

    //return total price of selected product in list order
    public static long getTotalPrice(List<Product> listSelectedProductInOrder) {
        long totalPrice = 0;
        for (Product p : listSelectedProductInOrder
        ) {
            //each product in selected product contain only 1 unit
            Unit unitInOrder = p.getUnits().get(0);
            totalPrice += unitInOrder.getUnitPrice() * unitInOrder.getUnitQuantity();
        }
        return totalPrice;
    }

}
