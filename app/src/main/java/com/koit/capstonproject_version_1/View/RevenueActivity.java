package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class RevenueActivity extends AppCompatActivity {
    BarChart barChart;
    Spinner spinnerChooseTime;
    TextView tvFrom, tvTo;
    ConstraintLayout timeLayout;
    ConstraintLayout revenueLayout;
    Date dateFrom;
    Date dateTo;
    boolean isFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_revenue_activvity);
        barChart = findViewById(R.id.barChart);
        tvFrom = findViewById(R.id.tvFromDate);
        tvTo = findViewById(R.id.tvToDate);
        spinnerChooseTime = findViewById(R.id.spinnerChooseTypeTime);
        revenueLayout = findViewById(R.id.revenueLayout);
        timeLayout = findViewById(R.id.timeLayout);
        setSpinner();
        TimeController.getInstance().setCurrentDate(tvFrom, tvTo);
        getDetailRevenueActivity();
        spinnerChooseTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTime) {
                    if (i == 0) {
                        TimeController.getInstance().setCurrentDate(tvFrom, tvTo);
                    } else {
                        TimeController.getInstance().setCurrentMonth(tvFrom, tvTo);
                    }
                }
                isFirstTime = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //set Date when text change
        tvFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String stringDate = tvFrom.getText().toString();
                dateFrom = TimeController.getInstance().getDateAndMonthFromText(stringDate, dateFrom);
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
                dateTo = TimeController.getInstance().getDateAndMonthFromText(stringDate, dateTo);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        setUpBarChart();

    }
    //https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/BarChartActivity.java
    private void setUpBarChart() {
        ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
        NoOfEmp.add(new BarEntry(0, 3));
        NoOfEmp.add(new BarEntry(1, 4));
        NoOfEmp.add(new BarEntry(3, 6));
        NoOfEmp.add(new BarEntry(4, 10));
        NoOfEmp.add(new BarEntry(2, 3));
        NoOfEmp.add(new BarEntry(5, 4));
        NoOfEmp.add(new BarEntry(6, 6));
        NoOfEmp.add(new BarEntry(7, 2));

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu");
        barChart.animateY(1000);
        BarData data = new BarData();
        data.addDataSet(bardataset);
        bardataset.setColors(getResources().getColor(R.color.light_blue));
        barChart.setData(data);
        barChart.invalidate();
        barChart.getDescription().setText("");
    }

    private void getDetailRevenueActivity() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            isFirstTime = true;
            String stringDateFrom = bundle.getString("dateFrom");
            String stringDateTo = bundle.getString("dateTo");
            tvFrom.setText(stringDateFrom);
            tvTo.setText(stringDateTo);
            dateFrom = TimeController.getInstance().getDateAndMonthFromText(stringDateFrom, dateFrom);
            dateTo = TimeController.getInstance().getDateAndMonthFromText(stringDateTo, dateTo);
            spinnerChooseTime.setSelection(bundle.getInt("searchType"));
        }
    }

    private void setSpinner() {
        String[] statusList = {"Theo ngày", "Theo tháng"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_near_arrow, statusList);
        spinnerChooseTime.setAdapter(statusAdapter);
    }

    public void transferToDetail(View view) {
        Intent intent = new Intent(RevenueActivity.this, DetailReveneuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dateFrom", tvFrom.getText().toString());
        bundle.putString("dateTo", tvTo.getText().toString());
        bundle.putInt("searchType", spinnerChooseTime.getSelectedItemPosition());
        intent.putExtra("BUNDLE", bundle);
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(this.getTimeLayout(), "timeLayout");
        pairs[1] = new Pair<View, String>(this.getRevenueLayout(), "revenueLayout");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        startActivity(intent, options.toBundle());
    }

    private ConstraintLayout getRevenueLayout() {
        return revenueLayout;
    }

    private ConstraintLayout getTimeLayout() {
        return timeLayout;
    }

    public void chooseTimeFrom(View view) {
        if (isDayType()) {
            TimeController.getInstance().chooseDayDialog(tvFrom, dateFrom, this);
        } else {
            TimeController.getInstance().chooseMonthDialog(tvFrom, dateFrom, this);
        }
    }

    public void chooseTimeTo(View view) {
        if (isDayType()) {
            TimeController.getInstance().chooseDayDialog(tvTo, dateTo, this);
        } else {
            TimeController.getInstance().chooseMonthDialog(tvTo, dateTo, this);
        }
    }

    private boolean isDayType() {
        if (spinnerChooseTime.getSelectedItemPosition() == 0) {
            return true;
        }
        return false;
    }


    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RevenueActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

