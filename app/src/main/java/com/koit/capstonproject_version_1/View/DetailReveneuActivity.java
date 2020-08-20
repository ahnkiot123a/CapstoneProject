package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Date;
import java.util.List;

public class DetailReveneuActivity extends AppCompatActivity {
    TextView tvFrom;
    TextView tvTo;
    Spinner spinnerChooseTypeTime;
    ConstraintLayout revenueLayout;
    ConstraintLayout timeLayout;
    Date dateFrom;
    Date dateTo;
    boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_detail_reveneu);
        tvFrom = findViewById(R.id.tvFromDate);
        tvTo = findViewById(R.id.tvToDate);
        timeLayout = findViewById(R.id.timeLayout);
        revenueLayout = findViewById(R.id.revenueLayout);

        spinnerChooseTypeTime = findViewById(R.id.spinnerChooseTypeTime);
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
                    dateFrom = TimeController.getInstance().changeStringToMonth(stringDate);
                } else {
                    dateFrom = TimeController.getInstance().changeStringDayToDate(stringDate);
                }
                Log.d("checkDateFrom", dateFrom.toString());
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
                    dateTo = TimeController.getInstance().changeStringToMonth(stringDate);
                } else {
                    dateTo = TimeController.getInstance().changeStringDayToDate(stringDate);
                }
                Log.d("checkDateTo", dateTo.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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
        List<Invoice> invoiceList = (List<Invoice>) bundle.getSerializable("listInvoice");
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
            TimeController.getInstance().chooseDayDialog(tvFrom, dateFrom, this,"01-01-2020");
        } else {
            TimeController.getInstance().chooseMonthDialog(tvFrom, dateFrom, this,"01-01-2020");
        }
    }

    public void chooseTimeTo(View view) {
        if (isDayType()) {
            TimeController.getInstance().chooseDayDialog(tvTo, dateTo, this,"01-01-2020");
        } else {
            TimeController.getInstance().chooseMonthDialog(tvTo, dateTo, this,"01-01-2020");
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