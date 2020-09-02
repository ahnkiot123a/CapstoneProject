package com.koit.capstonproject_version_1.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.koit.capstonproject_version_1.controller.DetailRevenueController;
import com.koit.capstonproject_version_1.controller.TimeController;
import com.koit.capstonproject_version_1.model.Invoice;
import com.koit.capstonproject_version_1.helper.StatusBar;
import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class DetailReveneuActivity extends AppCompatActivity {
    private TextView tvFrom;
    private TextView tvTo;
    private Spinner spinnerChooseTypeTime;
    private ConstraintLayout revenueLayout;
    private ConstraintLayout timeLayout;
    private Date dateFrom;
    private Date dateTo;
    private boolean firstTime = false;
    private DetailRevenueController detailRevenueController;
    private RecyclerView recyclerVInvoice;
    private List<Invoice> invoiceList;
    private TextView totalRevenue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_detail_reveneu);
        initView();

        setSpinner();
        spinnerChooseTypeTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!firstTime) {
                    if (i == 0) {
                        TimeController.getInstance().setCurrentDate(tvFrom, tvTo);
                    } else {
                        TimeController.getInstance().setCurrentMonth(tvFrom, tvTo);
                    }
                }
                firstTime = false;
                detailRevenueController.getListInvoice(dateFrom, dateTo,
                        spinnerChooseTypeTime.getSelectedItemPosition(),totalRevenue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getIntentFromReveneuActivity();

        //set Date when text change
        tvFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String stringDate = tvFrom.getText().toString();
                //month
                if (stringDate.length() == 7) {
                    dateFrom = TimeController.getInstance().changeStringMonthToFirstDayOfMonth(stringDate);
                } else {
                    dateFrom = TimeController.getInstance().changeStringDayToDate(stringDate);
                }
                arrangeDate();
                detailRevenueController.getListInvoice(dateFrom, dateTo,
                        spinnerChooseTypeTime.getSelectedItemPosition(),totalRevenue);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        tvTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String stringDate = tvTo.getText().toString();
                //month
                if (stringDate.length() == 7) {
                    dateTo = TimeController.getInstance().changeStringMonthToFirstDayOfMonth(stringDate);
                } else {
                    dateTo = TimeController.getInstance().changeStringDayToDate(stringDate);
                }
                arrangeDate();
                detailRevenueController.getListInvoice(dateFrom, dateTo,
                        spinnerChooseTypeTime.getSelectedItemPosition(),totalRevenue);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        detailRevenueController = new DetailRevenueController(recyclerVInvoice, this, invoiceList);
        Log.d("ListAllInvoicesOnC", invoiceList.toString());

    }

    private void arrangeDate() {
        dateFrom = TimeController.getInstance().getDateAndMonthFromText(tvFrom.getText().toString());
        dateTo = TimeController.getInstance().getDateAndMonthFromText(tvTo.getText().toString());
        if (dateFrom.after(dateTo)) {
            Date temp = new Date();
            temp = dateFrom;
            dateFrom = dateTo;
            dateTo = temp;
        }
    }

    private void initView() {
        tvFrom = findViewById(R.id.tvFromDate);
        tvTo = findViewById(R.id.tvToDate);
        timeLayout = findViewById(R.id.timeLayout);
        revenueLayout = findViewById(R.id.revenueLayout);
        spinnerChooseTypeTime = findViewById(R.id.spinnerChooseTypeTime);
        recyclerVInvoice = findViewById(R.id.recyclerVInvoice);
        totalRevenue = findViewById(R.id.totalRevenue);
    }

    private void getIntentFromReveneuActivity() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        spinnerChooseTypeTime.setSelection(bundle.getInt("searchType"));
        firstTime = true;
        String stringDateFrom = bundle.getString("dateFrom");
        String stringDateTo = bundle.getString("dateTo");
        tvFrom.setText(stringDateFrom);
        tvTo.setText(stringDateTo);
        dateFrom = TimeController.getInstance().getDateAndMonthFromText(stringDateFrom, dateFrom);
        dateTo = TimeController.getInstance().getDateAndMonthFromText(stringDateTo, dateTo);
        invoiceList = (List<Invoice>) bundle.getSerializable("listInvoice");
        Log.d("ListAllInvoices", invoiceList.toString());
    }

    private void setSpinner() {
        String[] statusList = {"Theo ngày", "Theo tháng"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_near_arrow, statusList);
        spinnerChooseTypeTime.setAdapter(statusAdapter);
    }

    public void back(View view) {
        onBackPressed();
    }


    public void chooseTimeFrom(View view) {
        if (isDayType()) {
            TimeController.getInstance().chooseDayDialog(tvFrom, dateFrom, this, "01-01-2020");
        } else {
            TimeController.getInstance().chooseMonthDialog(tvFrom, dateFrom, this, "01-01-2020");
        }
    }

    public void chooseTimeTo(View view) {
        if (isDayType()) {
            TimeController.getInstance().chooseDayDialog(tvTo, dateTo, this, "01-01-2020");
        } else {
            TimeController.getInstance().chooseMonthDialog(tvTo, dateTo, this, "01-01-2020");
        }
    }

    private boolean isDayType() {
        if (spinnerChooseTypeTime.getSelectedItemPosition() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}