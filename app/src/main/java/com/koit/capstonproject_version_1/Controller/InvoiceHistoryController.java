package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.DraftOrderAdapter;
import com.koit.capstonproject_version_1.Adapter.InvoiceHistoryAdapter;
import com.koit.capstonproject_version_1.Adapter.ItemInOrderAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtor;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Debtor;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.Model.Unit;
import com.koit.capstonproject_version_1.View.DraftOrderActivity;
import com.koit.capstonproject_version_1.View.InvoiceDetailActivity;
import com.koit.capstonproject_version_1.View.InvoiceHistoryActivity;
import com.koit.capstonproject_version_1.View.ListProductActivity;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InvoiceHistoryController {
    private Activity activity;
    private InvoiceHistoryDAO invoiceHistoryDAO;
    private InvoiceHistoryAdapter invoiceHistoryAdapter;
    private DraftOrderAdapter draftOrderAdapter;

    private ArrayList<Invoice> invoiceList;
    private ArrayList<Invoice> draftOrderList;

    private DatePickerDialog.OnDateSetListener onDateSetListenerOrderHistory;

    private String time = "Thời gian";
    private String status = "Tất cả đơn hàng";
    private Date dateInput;
    private OrderSwipeController orderSwipeController;
    private String draftOrderTime = "Tất cả";

    public InvoiceHistoryController(Activity activity) {
        this.activity = activity;
        invoiceHistoryDAO = new InvoiceHistoryDAO();
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
        invoiceHistoryDAO.getDebtorById(id, iDebtor);
    }


    //get invoice list by time and status
    public void invoiceList(RecyclerView recyclerViewListProduct, final TextView textView, final TextView tvTime) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        invoiceHistoryAdapter = new InvoiceHistoryAdapter(invoiceList, activity, textView);
        recyclerViewListProduct.setAdapter(invoiceHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null && !invoice.isDrafted()) {
                    invoiceHistoryAdapter.showShimmer = false;
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

                    if (status.equals("Tất cả đơn hàng") && time.equals("Chọn ngày")) {
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

                    if (status.equals("Hoá đơn còn nợ") && time.equals("Chọn ngày")) {
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

                    if (status.equals("Hoá đơn trả hết") && time.equals("Chọn ngày")) {
                        if (dateInput != null) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() == 0 && date.equals(dateInput)) {
                                invoiceList.add(invoice);
                            }
                        }
                    }

                }
                textView.setText(invoiceList.size() + " đơn hàng");
                invoiceHistoryAdapter.notifyDataSetChanged();
            }
        };
        invoiceHistoryDAO.getInvoiceList(iInvoice);
        invoiceHistoryAdapter.setOnItemClickListener(new InvoiceHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                sendInvoiceToDetail(position);
            }
        });
    }

    private void sendInvoiceToDetail(int position) {
        Invoice invoice = invoiceList.get(position);
        Intent intent = new Intent(activity, InvoiceDetailActivity.class);
        intent.putExtra(InvoiceHistoryActivity.INVOICE, invoice);
        activity.startActivity(intent);
    }

    public void draftOrderList(RecyclerView rvDraftOrder, final TextView tvCount, final TextView tvTime) {
        draftOrderList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        rvDraftOrder.setLayoutManager(layoutManager);
        draftOrderAdapter = new DraftOrderAdapter(draftOrderList, activity, tvCount);
        rvDraftOrder.setAdapter(draftOrderAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                if (invoice != null && invoice.isDrafted()) {
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
            }
        };
        invoiceHistoryDAO.getInvoiceList(iInvoice);
    }

    //event when click time spinner or invoice status spinner
    public void invoiceSpinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner, final Spinner statusSpinner, final SearchView searchView, final TextView tvTime) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = timeSpinner.getSelectedItem().toString();
                status = statusSpinner.getSelectedItem().toString();
                if (time.equals("Chọn ngày")) {
                    showDatePicker(onDateSetListenerOrderHistory);
                    onDateSetListenerOrderHistory = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String sDate = setDate(year, month, dayOfMonth);
                            tvTime.setText(sDate);
                            dateInput = TimeController.getInstance().convertStrToDate(sDate);
                            invoiceList(recyclerView, textView, tvTime);
                        }
                    };
                } else {
                    if (!InvoiceHistoryActivity.isFirstTimeRun) {
                        invoiceList(recyclerView, textView, tvTime);
                        etSearchEvent(searchView);
                    }
                    InvoiceHistoryActivity.isFirstTimeRun = false;
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
                if (!InvoiceHistoryActivity.isFirstTimeRun) {
                    invoiceList(recyclerView, textView, tvTime);
                    etSearchEvent(searchView);
                }
                InvoiceHistoryActivity.isFirstTimeRun = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void draftSpinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner, final TextView tvTime) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                draftOrderTime = timeSpinner.getSelectedItem().toString();
                if (!DraftOrderActivity.isFirstTimeRun) {
                    draftOrderList(recyclerView, textView, tvTime);
                }
                DraftOrderActivity.isFirstTimeRun = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

//    //build timer dialog when search by custom time
//    private void buildTimeDialog(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner, final SearchView searchView, final TextView tvTime) {
//        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
//        View view = activity.getLayoutInflater().inflate(R.layout.dialog_time_input, null);
//        final TextView tvDateStart, tvDateEnd;
//        Button btnCancel, btnConfirm;
//
//        tvDateStart = view.findViewById(R.id.tvDateStart);
//        tvDateEnd = view.findViewById(R.id.tvDateEnd);
//        btnCancel = view.findViewById(R.id.btnCancel);
//        btnConfirm = view.findViewById(R.id.btnConfirm);
//
//        tvDateStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePicker(onDateSetListenerStart);
//
//            }
//        });
//
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
//
//        tvDateEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePicker(onDateSetListenerEnd);
//            }
//        });
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
//
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                start = TimeController.getInstance().convertStrToDate(tvDateStart.getText().toString());
//                end = TimeController.getInstance().convertStrToDate(tvDateEnd.getText().toString());
//                if (start.equals(end)) {
//                    tvTime.setText(TimeController.getInstance().convertDateToStr(start));
//                } else {
//                    tvTime.setText("từ " + TimeController.getInstance().convertDateToStr(start) + " đến " + TimeController.getInstance().convertDateToStr(end));
//                }
//                alertDialog.cancel();
//                invoiceList(recyclerView, textView, tvTime);
//                etSearchEvent(searchView);
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.cancel();
//                timeSpinner.setSelection(0);
//            }
//        });
//
//        alertDialog.setView(view);
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//
//    }

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

    public void chooseDate(TextView tvTime) {

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
                invoiceHistoryAdapter.getFilter().filter(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                invoiceHistoryAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    public void setInvoiceDetail() {

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
                                InvoiceHistoryDAO invoiceHistoryDAO = new InvoiceHistoryDAO();
                                Invoice invoice = draftOrderList.get(position);
                                invoiceHistoryDAO.deleteDraftOrder(invoice.getInvoiceId());
                                draftOrderList.remove(position);
                                draftOrderAdapter.notifyItemRemoved(position);
                                count.setText(draftOrderList.size()+" hóa đơn tạm");
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