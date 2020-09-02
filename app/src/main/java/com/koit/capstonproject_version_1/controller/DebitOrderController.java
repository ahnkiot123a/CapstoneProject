package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koit.capstonproject_version_1.adapter.OrderHistoryAdapter;
import com.koit.capstonproject_version_1.controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.model.dao.OrderHistoryDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DebitOrderController {

    private Activity activity;
    private OrderHistoryDAO orderHistoryDAO;
    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<Invoice> invoiceList;
    private boolean hasInvoice = false;
    private String orderTime = "Tất cả";

    private DatePickerDialog.OnDateSetListener onDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener onDateSetListenerEnd;
    private Date start, end;


    public DebitOrderController(Activity activity) {
        this.activity = activity;
        orderHistoryDAO = new OrderHistoryDAO();
    }

    public void debitOrderList(final Debtor debtor, final RecyclerView recyclerViewListProduct, final TextView tvOrderTotal,
                               final ConstraintLayout layoutNotFound, final TextView tvTime, LinearLayout layoutDebitOrder) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(invoiceList, activity, tvOrderTotal);
        recyclerViewListProduct.setAdapter(orderHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null) {
                    if (!invoice.isDrafted()) {
                        orderHistoryAdapter.showShimmer = false;
                        if (invoice.getDebtorId().equals(debtor.getDebtorId()) && invoice.getDebitAmount() > 0) {
                            if (orderTime.equals("Tất cả")) {
                                tvTime.setText("");
                                invoiceList.add(invoice);
                                hasInvoice = true;
                            }
                            if (orderTime.equals("Tuỳ chỉnh")) {
                                if (start != null && end != null) {
                                    Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                    if (TimeController.getInstance().isInInterval(date, start, end)) {
                                        invoiceList.add(invoice);
                                    }
                                }
                            }

                        }
                    }
                    tvOrderTotal.setText(invoiceList.size() + " đơn hàng");
                    orderHistoryAdapter.notifyDataSetChanged();
                }

            }

        };
//        if(!hasInvoice){
//            recyclerViewListProduct.setVisibility(View.GONE);
//            layoutNotFound.setVisibility(View.VISIBLE);
//        }else{
//            recyclerViewListProduct.setVisibility(View.VISIBLE);
//            layoutNotFound.setVisibility(View.GONE);
//        }

        orderHistoryDAO.getInvoiceList(iInvoice, recyclerViewListProduct, layoutNotFound, layoutDebitOrder);
    }

    public void orderSpinnerEvent(final RecyclerView rvOrderDebtor, final TextView tvInvoiceCount, final Spinner timeSpinner,
                                  final TextView tvTime, final ConstraintLayout layout_not_found_item,
                                  final Debtor debtor, LinearLayout layoutDebitOrder, SwipeRefreshLayout refreshLayout,
                                  SwipeRefreshLayout refreshLayoutNotFound) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderTime = timeSpinner.getSelectedItem().toString();
                if (orderTime.equals("Tuỳ chỉnh")) {
                    buildTimeDialog(rvOrderDebtor, tvInvoiceCount, timeSpinner, tvTime, layout_not_found_item, debtor, layoutDebitOrder);
                } else {
//                    if (!OrderHistoryActivity.isFirstTimeRun) {
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            debitOrderList(debtor, rvOrderDebtor, tvInvoiceCount, layout_not_found_item, tvTime, layoutDebitOrder);
                            refreshLayout.setRefreshing(false);
                        }
                    });
                    refreshLayoutNotFound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            debitOrderList(debtor, rvOrderDebtor, tvInvoiceCount, layout_not_found_item, tvTime, layoutDebitOrder);
                            refreshLayoutNotFound.setRefreshing(false);
                        }
                    });
                    debitOrderList(debtor, rvOrderDebtor, tvInvoiceCount, layout_not_found_item, tvTime, layoutDebitOrder);

//                    }
//                    OrderHistoryActivity.isFirstTimeRun = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void buildTimeDialog(final RecyclerView rvOrderDebtor, final TextView tvInvoiceCount, final Spinner timeSpinner,
                                 final TextView tvTime, final ConstraintLayout layout_not_found_item,
                                 final Debtor debtor, LinearLayout layoutDebitOrder) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_time_input, null);
        final TextView tvDateStart, tvDateEnd;
        Button btnCancel, btnConfirm;

        tvDateStart = view.findViewById(R.id.tvDateStart);
        tvDateEnd = view.findViewById(R.id.tvDateEnd);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(onDateSetListenerStart);
            }
        });

        onDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dateTwo = tvDateEnd.getText().toString();
                if (!dateTwo.isEmpty()) {
                    Date one = TimeController.getInstance().convertStrToDate(TimeController.getInstance().setDate(year, month, day));
                    Date two = TimeController.getInstance().convertStrToDate(dateTwo);
                    if (one.after(two)) {
                        tvDateStart.setText(dateTwo);
                        tvDateEnd.setText(TimeController.getInstance().setDate(year, month, day));
                    } else {
                        tvDateStart.setText(TimeController.getInstance().setDate(year, month, day));
                    }
                } else {
                    tvDateStart.setText(TimeController.getInstance().setDate(year, month, day));
                }
            }
        };

        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(onDateSetListenerEnd);
            }
        });


        onDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dateOne = tvDateStart.getText().toString();
                if (!dateOne.isEmpty()) {
                    Date one = TimeController.getInstance().convertStrToDate(dateOne);
                    Date two = TimeController.getInstance().convertStrToDate(TimeController.getInstance().setDate(year, month, day));
                    if (one.after(two)) {
                        tvDateStart.setText(TimeController.getInstance().setDate(year, month, day));
                        tvDateEnd.setText(dateOne);
                    } else {
                        tvDateEnd.setText(TimeController.getInstance().setDate(year, month, day));
                    }
                } else {
                    tvDateEnd.setText(TimeController.getInstance().setDate(year, month, day));
                }
            }
        };

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvDateStart.getText().toString().isEmpty() && !tvDateEnd.getText().toString().isEmpty()) {
                    start = TimeController.getInstance().convertStrToDate(tvDateStart.getText().toString());
                    end = TimeController.getInstance().convertStrToDate(tvDateEnd.getText().toString());
                    if (start.equals(end)) {
                        tvTime.setText(TimeController.getInstance().convertDateToStr(start));
                    } else {
                        tvTime.setText("từ " + TimeController.getInstance().convertDateToStr(start) + " đến " + TimeController.getInstance().convertDateToStr(end));
                    }
                    alertDialog.cancel();
                    debitOrderList(debtor, rvOrderDebtor, tvInvoiceCount, layout_not_found_item, tvTime, layoutDebitOrder);
                } else {

                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                timeSpinner.setSelection(0);
            }
        });
        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Material_Dialog
                , onDateSetListener, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

}
