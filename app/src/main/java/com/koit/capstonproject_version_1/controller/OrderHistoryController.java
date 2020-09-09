package com.koit.capstonproject_version_1.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.adapter.DraftOrderAdapter;
import com.koit.capstonproject_version_1.adapter.OrderHistoryAdapter;
import com.koit.capstonproject_version_1.controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.model.Debtor;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.model.dao.OrderHistoryDAO;
import com.koit.capstonproject_version_1.view.OrderHistoryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class OrderHistoryController {
    private Activity activity;
    private OrderHistoryDAO orderHistoryDAO;
    private OrderHistoryAdapter orderHistoryAdapter;
    private DraftOrderAdapter draftOrderAdapter;

    private ArrayList<Invoice> invoiceList;
    private ArrayList<Invoice> draftOrderList;
    public static Semaphore semaphore = new Semaphore(0);

    private DatePickerDialog.OnDateSetListener onDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener onDateSetListenerEnd;

    private String time = "Thời gian";
    private String status = "Tất cả đơn hàng";
    private Date start, end;
    private OrderSwipeController orderSwipeController;
    private String draftOrderTime = "Tất cả";


    public OrderHistoryController(Activity activity) {
        this.activity = activity;
        orderHistoryDAO = new OrderHistoryDAO();
    }

    //get debtor name and fill text view
    public void fillDebtorName(String id, final TextView textView) {
        IDebtor iDebtor = new IDebtor() {
            @Override
            public void getDebtor(Debtor debtor) {
                if (debtor != null) {
                    textView.setText(debtor.getFullName());
                }
            }
        };
        orderHistoryDAO.getDebtorById(id, iDebtor);
    }


    //get invoice list by time and status
    public void invoiceList(final RecyclerView recyclerViewListProduct, final TextView textView,
                            final TextView tvTime, final ConstraintLayout layoutNotFound,
                            final SearchView searchView, LinearLayout layoutOrderHistory) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(invoiceList, activity, textView);
        recyclerViewListProduct.setAdapter(orderHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null) {
                    if (!invoice.isDrafted()) {
                        orderHistoryAdapter.showShimmer = false;
                        if (status.equals("Tất cả đơn hàng") && time.equals("Thời gian")) {
                            tvTime.setText("");
                            invoiceList.add(invoice);
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("Hôm nay")) {
                            tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());
                            if (invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate())) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("7 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-6) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("30 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-29) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Tất cả đơn hàng") && time.equals("Tuỳ chỉnh")) {
                            if (start != null && end != null) {
                                Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                if (TimeController.getInstance().isInInterval(date, start, end)) {
                                    invoiceList.add(invoice);
                                }
                            }
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("Thời gian")) {
                            tvTime.setText("");
                            if (invoice.getDebitAmount() > 0) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn còn nợ") && time.equals("Hôm nay")) {
                            tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());
                            if (invoice.getDebitAmount() > 0 && invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate())) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("7 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-6) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);

                            }
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("30 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-29) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn còn nợ") && time.equals("Tuỳ chỉnh")) {
                            if (start != null && end != null) {
                                Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInInterval(date, start, end)) {
                                    invoiceList.add(invoice);
                                }
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("Thời gian")) {
                            tvTime.setText("");
                            if (invoice.getDebitAmount() == 0) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("Hôm nay")) {
                            tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());
                            if (invoice.getDebitAmount() == 0 && invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate())) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("7 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() == 0 && TimeController.getInstance().isInNumOfDays(7, date)) {
                                tvTime.setText("từ " + TimeController.getInstance().plusDate(-6) + " đến " + TimeController.getInstance().getCurrentDate());
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("30 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-29) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() == 0 && TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn trả hết") && time.equals("Tuỳ chỉnh")) {
                            if (start != null && end != null) {
                                Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                if (invoice.getDebitAmount() == 0 && TimeController.getInstance().isInInterval(date, start, end)) {
                                    invoiceList.add(invoice);
                                }
                            }
                        }
//                        orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                        textView.setText(invoiceList.size() + " đơn hàng");
                        orderHistoryAdapter.notifyDataSetChanged();
                    }
                    if (invoiceList.isEmpty()) {
                        layoutOrderHistory.setVisibility(View.GONE);
                        layoutNotFound.setVisibility(View.VISIBLE);
                    } else {
                        layoutOrderHistory.setVisibility(View.VISIBLE);
                        layoutNotFound.setVisibility(View.GONE);
                    }
                }

            }

        };
        orderHistoryDAO.getInvoiceList(iInvoice, recyclerViewListProduct, layoutNotFound, layoutOrderHistory);
    }


    public void setTotalDraftOrder(final RelativeLayout cart_badge, final TextView totalOrderDraftQuantity) {
        draftOrderList = new ArrayList<>();
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                Date invoiceDate = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                Date threeDate = TimeController.getInstance().convertStrToDate(TimeController.getInstance().plusDate(-3));
                if (invoice.isDrafted() && invoiceDate.after(threeDate)) {
                    draftOrderList.add(invoice);
                    Log.d("draftedOrder", invoice.toString());
                    Log.d("draftOrderList", draftOrderList.size() + "");
                }
                if (draftOrderList.size() > 99) {
                    totalOrderDraftQuantity.setText("99+");
                    cart_badge.setVisibility(View.VISIBLE);
                } else if (draftOrderList.size() >= 0 && draftOrderList.size() <= 99) {
                    totalOrderDraftQuantity.setText(draftOrderList.size() + "");
                    cart_badge.setVisibility(View.VISIBLE);
                }

            }
        };
        orderHistoryDAO.getDraftOrderList(iInvoice);

    }


    public void draftOrderList(final RecyclerView rvDraftOrder, final TextView tvCount, final TextView tvTime,
                               final ConstraintLayout layoutNotFound, LinearLayout layoutDraftOrder) {
        draftOrderList = new ArrayList<>();
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        rvDraftOrder.setLayoutManager(layoutManager);
        draftOrderAdapter = new DraftOrderAdapter(draftOrderList, activity, tvCount);
        rvDraftOrder.setAdapter(draftOrderAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null) {
                    Date invoiceDate = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                    Date threeDate = TimeController.getInstance().convertStrToDate(TimeController.getInstance().plusDate(-3));
                    if (invoice.isDrafted() && invoiceDate.after(threeDate)) {
                        draftOrderAdapter.showShimmer = false;
                        if (draftOrderTime.equals("Tất cả")) {
                            tvTime.setText("");
                            draftOrderList.add(invoice);
                        }
                        if (draftOrderTime.equals("Hôm nay")) {
                            tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());
                            if (invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate())) {
                                draftOrderList.add(invoice);
                            }

                        }
                        if (draftOrderTime.equals("Hôm qua")) {
                            tvTime.setText("Hôm qua, " + TimeController.getInstance().plusDate(-1));
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            Date yesterday = TimeController.getInstance().convertStrToDate(TimeController.getInstance().plusDate(-1));
                            if (date.equals(yesterday)) {
                                draftOrderList.add(invoice);
                            }
                        }

                        if (draftOrderTime.equals("Hôm kia")) {
                            tvTime.setText("Hôm kia, " + TimeController.getInstance().plusDate(-2));
                            Date orderDate = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            Date date = TimeController.getInstance().convertStrToDate(TimeController.getInstance().plusDate(-2));
                            if (orderDate.equals(date)) {
                                draftOrderList.add(invoice);
                            }
                        }
//                        tvCount.setText(draftOrderList.size() + " hoá đơn tạm");
                        draftOrderAdapter.notifyDataSetChanged();
                    }

                }
                tvCount.setText(draftOrderList.size() + " hoá đơn tạm");
                if (draftOrderList.isEmpty()) {
                    layoutDraftOrder.setVisibility(View.GONE);
                    layoutNotFound.setVisibility(View.VISIBLE);
                } else {
                    layoutDraftOrder.setVisibility(View.VISIBLE);
                    layoutNotFound.setVisibility(View.GONE);
                }

            }
        };
        orderHistoryDAO.getInvoiceList(iInvoice, rvDraftOrder, layoutNotFound, layoutDraftOrder);
        draftOrderAdapter.setOnItemClickListener(new DraftOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!draftOrderList.isEmpty()) {
                    Invoice invoice = draftOrderList.get(position);
                    InvoiceDetailController invoiceDetailController = new InvoiceDetailController(activity);
                    invoiceDetailController.sendDraftOrder(invoice.getInvoiceId());
                }
            }
        });
    }

    public void deleteDraftOrderBefore3Days(){
        orderHistoryDAO.deleteDraftOrderBefore3Days();
    }

    //event when click time spinner or invoice status spinner
    public void invoiceSpinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner,
                                    final Spinner statusSpinner, final SearchView searchView, final TextView tvTime,
                                    final ConstraintLayout layoutNotFound, SwipeRefreshLayout refreshLayout,
                                    LinearLayout layoutOrderHistory, SwipeRefreshLayout refreshLayoutNotFound) {

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = timeSpinner.getSelectedItem().toString();
                status = statusSpinner.getSelectedItem().toString();
                if (time.equals("Tuỳ chỉnh")) {
                    buildTimeDialog(recyclerView, textView, timeSpinner, searchView, tvTime, layoutNotFound, layoutOrderHistory);
                } else {
                    if (!OrderHistoryActivity.isFirstTimeRun) {
                        invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                        searchView.setQuery("", true);
                        searchView.clearFocus();
//                        orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                                searchView.setQuery("", true);
                                searchView.clearFocus();
//                                orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                                refreshLayout.setRefreshing(false);
                            }
                        });
                        refreshLayoutNotFound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                                searchView.setQuery("", true);
                                searchView.clearFocus();
//                                orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                                refreshLayoutNotFound.setRefreshing(false);
                            }
                        });
                    }
                    OrderHistoryActivity.isFirstTimeRun = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = timeSpinner.getSelectedItem().toString();
                status = statusSpinner.getSelectedItem().toString();
                if (!OrderHistoryActivity.isFirstTimeRun) {
                    invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                    searchView.setQuery("", true);
                    searchView.clearFocus();
                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                            searchView.setQuery("", true);
                            searchView.clearFocus();
//                            orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                            refreshLayout.setRefreshing(false);
                        }
                    });
                    refreshLayoutNotFound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                            searchView.setQuery("", true);
                            searchView.clearFocus();
//                            orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                            refreshLayoutNotFound.setRefreshing(false);
                        }
                    });

                }
                OrderHistoryActivity.isFirstTimeRun = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void draftSpinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner,
                                  final TextView tvTime, final ConstraintLayout layoutNotFound,
                                  SwipeRefreshLayout refreshLayout, LinearLayout layoutDraftOrder,
                                  SwipeRefreshLayout refreshLayoutNotFound) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                draftOrderTime = timeSpinner.getSelectedItem().toString();
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        draftOrderList(recyclerView, textView, tvTime, layoutNotFound, layoutDraftOrder);
                        refreshLayout.setRefreshing(false);
                    }
                });
                refreshLayoutNotFound.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        draftOrderList(recyclerView, textView, tvTime, layoutNotFound, layoutDraftOrder);
                        refreshLayoutNotFound.setRefreshing(false);
                    }
                });
                draftOrderList(recyclerView, textView, tvTime, layoutNotFound, layoutDraftOrder);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //build timer dialog when search by custom time
    private void buildTimeDialog(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner,
                                 final SearchView searchView, final TextView tvTime,
                                 final ConstraintLayout layoutNotFound, LinearLayout layoutOrderHistory) {
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
                    invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView, layoutOrderHistory);
                    searchView.setQuery("", true);
                    searchView.clearFocus();
//                    orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());

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

    public void etSearchEvent(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderHistoryAdapter.getFilter().filter(newText.trim());
                return true;
            }
        });
    }

    public void setupRecyclerView(RecyclerView recyclerView, final TextView count) {
        orderSwipeController = new OrderSwipeController(new OrderSwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //remove item in 2 list
                if (!draftOrderList.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Bạn có muốn xóa đơn tạm này không? ")
                            .setCancelable(true)
                            .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //remove item on right click
                                    OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
                                    Invoice invoice = draftOrderList.get(position);
                                    orderHistoryDAO.deleteDraftOrder(invoice.getInvoiceId());
                                    draftOrderList.remove(position);
                                    draftOrderAdapter.notifyItemRemoved(position);
                                    count.setText(draftOrderList.size() + " hóa đơn tạm");
                                    Toast.makeText(activity, "Bạn đã xoá thành công đơn tạm", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                        }
                    });
                    alert.show();
                }
            }

        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(orderSwipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                orderSwipeController.onDraw(c);
            }
        });

    }
}