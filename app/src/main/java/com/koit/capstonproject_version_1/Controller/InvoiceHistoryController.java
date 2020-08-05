package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koit.capstonproject_version_1.Adapter.InvoiceHistoryAdapter;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.InvoiceHistoryActivity;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InvoiceHistoryController {
    private Activity activity;
    private InvoiceHistoryDAO invoiceHistoryDAO;
    private InvoiceHistoryAdapter invoiceHistoryAdapter;
    private ArrayList<Invoice> invoiceList;
    private DatePickerDialog.OnDateSetListener onDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener onDateSetListenerEnd;

    private String time = "Thời gian";
    private String status = "Tất cả đơn hàng";

    public InvoiceHistoryController(Activity activity) {
        this.activity = activity;
        invoiceHistoryDAO = new InvoiceHistoryDAO();
    }

    public void invoiceList(RecyclerView recyclerViewListProduct, final TextView textView) {
        invoiceList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerViewListProduct.setLayoutManager(layoutManager);
        invoiceHistoryAdapter = new InvoiceHistoryAdapter(invoiceList, activity, textView);
        recyclerViewListProduct.setAdapter(invoiceHistoryAdapter);
        IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                Log.d("invoice", invoice.toString());
                if (invoice != null && !invoice.isDrafted()) {
                    invoiceHistoryAdapter.showShimmer = false;
                    if (time.equals("Thời gian") && status.equals("Tất cả đơn hàng")) {
                        invoiceList.add(invoice);
                    } else {
                        if (status.equals("Tất cả đơn hàng") && time.equals("Hôm nay")) {
                            if (invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate()))
                                invoiceList.add(invoice);
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("7 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Tất cả đơn hàng") && time.equals("30 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Tất cả đơn hàng") && time.equals("Tuỳ chỉnh")) {

                        }

                        if (status.equals("Hoá đơn còn nợ") && time.equals("Thời gian")) {
                            if (invoice.getDebitAmount() > 0) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("Hôm nay")) {
                            if (invoice.getDebitAmount() > 0 && invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate()))
                                invoiceList.add(invoice);
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("7 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn còn nợ") && time.equals("30 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() > 0 && TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn còn nợ") && time.equals("Tuỳ chỉnh")) {

                        }

                        if (status.equals("Hoá đơn trả hết") && time.equals("Thời gian")) {
                            if (invoice.getDebitAmount() == 0) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("Hôm nay")) {
                            if (invoice.getDebitAmount() == 0 && invoice.getInvoiceDate().equals(TimeController.getInstance().getCurrentDate()))
                                invoiceList.add(invoice);
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("7 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() == 0 && TimeController.getInstance().isInNumOfDays(7, date)) {
                                invoiceList.add(invoice);
                            }
                        }
                        if (status.equals("Hoá đơn trả hết") && time.equals("30 ngày trước")) {
                            Date date = TimeController.getInstance().convertStrToDate(invoice.getInvoiceDate());
                            if (invoice.getDebitAmount() == 0 && TimeController.getInstance().isInNumOfDays(30, date)) {
                                invoiceList.add(invoice);
                            }
                        }

                        if (status.equals("Hoá đơn trả hết") && time.equals("Tuỳ chỉnh")) {

                        }

                    }
                    invoiceHistoryAdapter.notifyDataSetChanged();
                }

            }

        };
        invoiceHistoryDAO.getInvoiceList(iInvoice);
    }

    public void spinnerEvent(final RecyclerView recyclerView, final TextView textView, final Spinner timeSpinner, final Spinner statusSpinner, final EditText editText) {
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = timeSpinner.getSelectedItem().toString();
                status = statusSpinner.getSelectedItem().toString();
                if (time.equals("Tuỳ chỉnh")) {
                    buildTimeDialog();

                } else {
                    if (!InvoiceHistoryActivity.isFirstTimeRun) {
                        invoiceList(recyclerView, textView);
                        invoiceHistoryAdapter.getFilter().filter(editText.getText().toString().trim());
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
                    invoiceList(recyclerView, textView);
                    invoiceHistoryAdapter.getFilter().filter(editText.getText().toString().trim());
                }
                InvoiceHistoryActivity.isFirstTimeRun = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void buildTimeDialog() {
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
                month +=1;
                String date = day + "-" + month + "-" + year;
                tvDateStart.setText(date);
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
                month +=1;
                String date = day + "-" + month + "-" + year;
                tvDateEnd.setText(date);
            }
        };

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();

    }

    private void showDatePicker(DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(activity, android.R.style.Theme_DeviceDefault_Dialog_MinWidth
                , onDateSetListener, year, month, day);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void etSearchEvent(EditText etSearch) {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                invoiceHistoryAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
