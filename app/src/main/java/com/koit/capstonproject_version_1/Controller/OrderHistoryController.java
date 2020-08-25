package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import com.koit.capstonproject_version_1.Adapter.DraftOrderAdapter;
import com.koit.capstonproject_version_1.Adapter.OrderHistoryAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.InvoiceDetailActivity;
import com.koit.capstonproject_version_1.View.OrderHistoryActivity;
import com.koit.capstonproject_version_1.dao.OrderHistoryDAO;

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
    private Date dateInput, start, end;
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
    public void invoiceList(final RecyclerView recyclerViewListProduct, final TextView textView, final TextView tvTime, final ConstraintLayout layoutNotFound, final SearchView searchView) {
        invoiceList = new ArrayList<>();
        final boolean hasInvoice = false;
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
                        if (status.equals("Tất cả đơn hàng") && time.equals("Hôm nay")) {
                            tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());
                            if (invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate())) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("7 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-7) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("30 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-30) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Tất cả đơn hàng") && time.equals("Tuỳ chỉnh")) {
                            if (dateInput != null) {
                                Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                if (date.equals(dateInput)) {
                                    invoiceList.add(invoice);
                                }
                            }
                        }

                        if (status.equals("Hoá đơn còn nợ") && time.equals("Hôm nay")) {
                            tvTime.setText("Hôm nay, " + TimeController.getInstance().getCurrentDate());
                            if (invoice.getDebitAmount() > 0 && invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate())) {
                                invoiceList.add(invoice);
                            }


                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("7 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-7) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);

                            }
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("30 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-30) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn còn nợ") && time.equals("Tuỳ chỉnh")) {
                            if (dateInput != null) {
                                Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                if (invoice.getDebitAmount() > 0 && date.equals(dateInput)) {
                                    invoiceList.add(invoice);
                                }
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
                                tvTime.setText("từ " + TimeController.getInstance().plusDate(-7) + " đến " + TimeController.getInstance().getCurrentDate());
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("30 ngày trước")) {
                            tvTime.setText("từ " + TimeController.getInstance().plusDate(-30) + " đến " + TimeController.getInstance().getCurrentDate());
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() == 0 && TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn trả hết") && time.equals("Tuỳ chỉnh")) {
                            if (dateInput != null) {
                                Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                                if (invoice.getDebitAmount() == 0 && date.equals(dateInput)) {
                                    invoiceList.add(invoice);
                                }
                            }
                        }
                        orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                        textView.setText(invoiceList.size() + " đơn hàng");
                        orderHistoryAdapter.notifyDataSetChanged();

                    }

                    if (invoiceList.isEmpty()) {
                        recyclerViewListProduct.setVisibility(View.GONE);
                        layoutNotFound.setVisibility(View.VISIBLE);
                    } else {
                        recyclerViewListProduct.setVisibility(View.VISIBLE);
                        layoutNotFound.setVisibility(View.GONE);
                    }
                }

            }

        };
        orderHistoryDAO.getInvoiceList(iInvoice, recyclerViewListProduct, layoutNotFound);
        orderHistoryAdapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                sendInvoiceToDetail(position);
            }
        });
    }

    private void sendInvoiceToDetail(int position) {
        if (!invoiceList.isEmpty()) {
            Invoice invoice = invoiceList.get(position);
            Intent intent = new Intent(activity, InvoiceDetailActivity.class);
            intent.putExtra(OrderHistoryActivity.INVOICE, invoice);
            activity.startActivity(intent);
        }
    }

    public void setTotalDraftOrder(final RelativeLayout cart_badge, final TextView totalOrderDraftQuantity) {
        draftOrderList = new ArrayList<>();
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice.isDrafted()) {
                    draftOrderList.add(invoice);
                    Log.d("draftedOrder", invoice.toString());
                    Log.d("draftOrderList", draftOrderList.size() + "");
                }
                if (draftOrderList.size() > 99) {
                    totalOrderDraftQuantity.setText("99+");
                    cart_badge.setVisibility(View.VISIBLE);
                } else if (draftOrderList.size() > 0 && draftOrderList.size() <= 99) {
                    totalOrderDraftQuantity.setText(draftOrderList.size() + "");
                    cart_badge.setVisibility(View.VISIBLE);
                } else cart_badge.setVisibility(View.GONE);

            }
        };
        orderHistoryDAO.getDraftOrderList(iInvoice);

    }


    public void draftOrderList(final RecyclerView rvDraftOrder, final TextView tvCount, final TextView tvTime, final ConstraintLayout layoutNotFound) {
        draftOrderList = new ArrayList<>();
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        rvDraftOrder.setLayoutManager(layoutManager);
        draftOrderAdapter = new DraftOrderAdapter(draftOrderList, activity, tvCount);
        rvDraftOrder.setAdapter(draftOrderAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null) {
                    if (invoice.isDrafted()) {
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

                        tvCount.setText(draftOrderList.size() + " hoá đơn tạm");
                        draftOrderAdapter.notifyDataSetChanged();
                    }
                    if (draftOrderList.isEmpty()) {
                        rvDraftOrder.setVisibility(View.GONE);
                        layoutNotFound.setVisibility(View.VISIBLE);
                    } else {
                        rvDraftOrder.setVisibility(View.VISIBLE);
                        layoutNotFound.setVisibility(View.GONE);
                    }
                }

            }
        };
        orderHistoryDAO.getInvoiceList(iInvoice, rvDraftOrder, layoutNotFound);
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

    //event when click time spinner or invoice status spinner
    public void invoiceSpinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner,
                                    final Spinner statusSpinner, final SearchView searchView, final TextView tvTime, final ConstraintLayout layoutNotFound) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = timeSpinner.getSelectedItem().toString();
                status = statusSpinner.getSelectedItem().toString();
                if (time.equals("Tuỳ chỉnh")) {
                    buildTimeDialog(recyclerView, textView, timeSpinner, searchView, tvTime, layoutNotFound);
                } else {
                    if (!OrderHistoryActivity.isFirstTimeRun) {
                        invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView);
                        orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());

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
                    invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView);
                    orderHistoryAdapter.getFilter().filter(searchView.getQuery().toString());
                }
                OrderHistoryActivity.isFirstTimeRun = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void draftSpinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner,
                                  final TextView tvTime, final ConstraintLayout layoutNotFound) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                draftOrderTime = timeSpinner.getSelectedItem().toString();
                draftOrderList(recyclerView, textView, tvTime, layoutNotFound);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //build timer dialog when search by custom time
    private void buildTimeDialog(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner,
                                 final SearchView searchView, final TextView tvTime, final ConstraintLayout layoutNotFound) {
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
                start = TimeController.getInstance().chooseDayInOrderHistory(alertDialog.getContext());
                tvDateStart.setText(TimeController.getInstance().convertDateToStr(start));
            }
        });

//        onDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                String dateTwo = tvDateEnd.getText().toString();
//                if (!dateTwo.isEmpty()) {
//                    Date one = TimeController.getInstance().convertStrToDate(setDate(year, month, day));
//                    Date two = TimeController.getInstance().convertStrToDate(dateTwo);
//                    if (one.after(two)) {
//                        tvDateStart.setText(dateTwo);
//                        tvDateEnd.setText(setDate(year, month, day));
//                    } else {
//                        tvDateStart.setText(setDate(year, month, day));
//                    }
//                } else {
//                    tvDateStart.setText(setDate(year, month, day));
//                }
//            }
//        };

        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end = TimeController.getInstance().chooseDayInOrderHistory(alertDialog.getContext());
                if (start.before(end)) {
                    tvDateStart.setText(TimeController.getInstance().convertDateToStr(end));
                    tvDateEnd.setText(TimeController.getInstance().convertDateToStr(start));
                } else {
                    tvDateEnd.setText(TimeController.getInstance().convertDateToStr(end));
                }
            }
        });

//
//        onDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                String dateOne = tvDateStart.getText().toString();
//                if (!dateOne.isEmpty()) {
//                    Date one = TimeController.getInstance().convertStrToDate(dateOne);
//                    Date two = TimeController.getInstance().convertStrToDate(setDate(year, month, day));
//                    if (one.after(two)) {
//                        tvDateStart.setText(setDate(year, month, day));
//                        tvDateEnd.setText(dateOne);
//                    } else {
//                        tvDateEnd.setText(setDate(year, month, day));
//                    }
//                } else {
//                    tvDateEnd.setText(setDate(year, month, day));
//                }
//            }
//        };

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = TimeController.getInstance().convertStrToDate(tvDateStart.getText().toString());
                end = TimeController.getInstance().convertStrToDate(tvDateEnd.getText().toString());
                if (start.equals(end)) {
                    tvTime.setText(TimeController.getInstance().convertDateToStr(start));
                } else {
                    tvTime.setText("từ " + TimeController.getInstance().convertDateToStr(start) + " đến " + TimeController.getInstance().convertDateToStr(end));
                }
                alertDialog.cancel();
                invoiceList(recyclerView, textView, tvTime, layoutNotFound, searchView);
                etSearchEvent(searchView);
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

    private String setDate(int year, int month, int day) {
        month += 1;
        String date = day + "-" + month + "-" + year;
        if (month < 10) {
            date = day + "-0" + month + "-" + year;
        }
        if (day < 10) {
            date = "0" + day + "-" + month + "-" + year;
        }
        if (month < 10 && day < 10) {
            date = "0" + day + "-0" + month + "-" + year;
        }
        return date;
    }


    private void showDatePicker(DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , onDateSetListener, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    public void etSearchEvent(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                invoiceHistoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderHistoryAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public void setupRecyclerView(RecyclerView recyclerView, final TextView count) {
        orderSwipeController = new OrderSwipeController(new OrderSwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                //remove item in 2 list
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