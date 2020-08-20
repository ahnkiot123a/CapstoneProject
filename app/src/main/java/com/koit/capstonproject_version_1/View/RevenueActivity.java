package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.koit.capstonproject_version_1.Controller.RevenueController;
import com.koit.capstonproject_version_1.Controller.TimeController;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.MyMarkerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RevenueActivity extends AppCompatActivity {
    LinearLayout layoutChart;
    LinearLayout linearLayoutEmptyInvoice;
    BarChart chart;
    LineChart lineChart;
    Spinner spinnerChooseTime;
    TextView tvFrom, tvTo;
    ConstraintLayout timeLayout;
    ConstraintLayout revenueLayout;
    Date dateFrom;
    Date dateTo;
    boolean isFirstTime = true;
    Spinner spinnerGraph;
    LottieAnimationView animationView;
    TextView tvTotal;
    ShimmerFrameLayout shimmerFrameLayout;
    RevenueController revenueController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_revenue_activvity);
        initView();
        setSpinner();
        //set up UI loading
        shimmerFrameLayout.startShimmer();
        animationView.setVisibility(View.VISIBLE);

        TimeController.getInstance().setCurrentDate(tvFrom, tvTo);
        revenueController = new RevenueController(this, chart, lineChart);

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
        spinnerGraph.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    chart.setVisibility(View.VISIBLE);
//                    setUpBarChart();
                    lineChart.setVisibility(View.GONE);
                } else {
                    lineChart.setVisibility(View.VISIBLE);
//                    setUpLineChart();
                    chart.setVisibility(View.GONE);
                }
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
                arrangeDate();
                revenueController.getListInvoiceAfter(dateFrom, dateTo, tvTotal,
                        spinnerChooseTime.getSelectedItemPosition());
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
                arrangeDate();
                revenueController.getListInvoiceAfter(dateFrom, dateTo, tvTotal,
                        spinnerChooseTime.getSelectedItemPosition());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //set timeFrom and timTo for the first time
        arrangeDate();
        revenueController.getListInvoiceFirstTimme(dateFrom, dateTo, animationView, tvTotal,
                shimmerFrameLayout, linearLayoutEmptyInvoice, layoutChart);
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
        chart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        tvFrom = findViewById(R.id.tvFromDate);
        tvTo = findViewById(R.id.tvToDate);
        spinnerChooseTime = findViewById(R.id.spinnerChooseTypeTime);
        revenueLayout = findViewById(R.id.revenueLayout);
        timeLayout = findViewById(R.id.timeLayout);
        spinnerGraph = findViewById(R.id.spinnerChooseTypeGraph);
        animationView = findViewById(R.id.animationView);
        tvTotal = findViewById(R.id.tvTotal);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
        linearLayoutEmptyInvoice = findViewById(R.id.linearLayoutEmptyInvoice);
        layoutChart = findViewById(R.id.layoutChart);
    }


//    private void getDetailRevenueActivity() {
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("BUNDLE");
//        if (bundle != null) {
//            isFirstTime = true;
//            String stringDateFrom = bundle.getString("dateFrom");
//            String stringDateTo = bundle.getString("dateTo");
//            tvFrom.setText(stringDateFrom);
//            tvTo.setText(stringDateTo);
//            dateFrom = TimeController.getInstance().getDateAndMonthFromText(stringDateFrom);
//            dateTo = TimeController.getInstance().getDateAndMonthFromText(stringDateTo);
//            spinnerChooseTime.setSelection(bundle.getInt("searchType"));
//        }
//    }

    private void setSpinner() {
        String[] statusList = {"Theo ngày", "Theo tháng"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_near_arrow, statusList);
        spinnerChooseTime.setAdapter(statusAdapter);

        String[] graphType = {"Biểu đồ cột", "Biểu đồ đường"};
        ArrayAdapter<String> graphsAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_near_arrow, graphType);
        spinnerGraph.setAdapter(graphsAdapter);
    }

    public void transferToDetail(View view) {
        Intent intent = new Intent(RevenueActivity.this, DetailReveneuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dateFrom", tvFrom.getText().toString());
        bundle.putString("dateTo", tvTo.getText().toString());
        bundle.putInt("searchType", spinnerChooseTime.getSelectedItemPosition());
        bundle.putSerializable("listInvoice", (Serializable) revenueController.getListInvoice());
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

