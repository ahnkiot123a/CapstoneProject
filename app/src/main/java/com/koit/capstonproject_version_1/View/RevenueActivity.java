package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RevenueActivity extends AppCompatActivity {
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
        getDetailRevenueActivity();
        RevenueController revenueController = new RevenueController(this, chart);

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
                String stringDate = tvFrom.getText().toString();
                dateFrom = TimeController.getInstance().getDateAndMonthFromText(stringDate);
                RevenueController revenueController = new RevenueController(RevenueActivity.this, chart);
                revenueController.getListInvoice(dateFrom, dateTo, animationView, tvTotal, shimmerFrameLayout);
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
                dateTo = TimeController.getInstance().getDateAndMonthFromText(stringDate);
                RevenueController revenueController = new RevenueController(RevenueActivity.this, chart);
                revenueController.getListInvoice(dateFrom, dateTo, animationView, tvTotal, shimmerFrameLayout);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //set timeFrom and timTo for the first time
        dateFrom = TimeController.getInstance().getDateAndMonthFromText(tvFrom.getText().toString());
        dateTo = TimeController.getInstance().getDateAndMonthFromText(tvTo.getText().toString());
        if (dateFrom.after(dateTo)) {
            Date temp = new Date();
            temp = dateFrom;
            dateFrom = dateTo;
            dateTo = temp;
        }
        revenueController.getListInvoice(dateFrom, dateTo, animationView, tvTotal, shimmerFrameLayout);
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
    }

    //https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/BarChartActivity.java
    private void setUpBarChart() {
//        InvoiceHistoryController invoiceHistoryController = new InvoiceHistoryController(this, chart);
//        invoiceHistoryController.setDataForBarchart();
        chart.invalidate();
        chart.getDescription().setText("");

        chart.animateY(1000);

        chart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
        chart.moveViewToX(10); // set the left edge of the chart to x-index 10
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(chart);
        chart.setMarker(mv);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(14);

    }

    //set Line chart
    private void setUpLineChart() {
//        setDataForLinechart(lineChart);
        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        // Set the marker to the chart
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        // draw points over time
        lineChart.animateX(500);
        lineChart.getDescription().setText("");

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(true);
            xAxis.setTextSize(14);
            xAxis.enableGridDashedLine(5f, 10f, 2f);
        }
        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = lineChart.getAxisLeft();
            // disable dual axis (only use LEFT axis)
            lineChart.getAxisRight().setEnabled(false);
            yAxis.setTextSize(14);
            // horizontal grid lines
            yAxis.enableGridDashedLine(5f, 10f, 2f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(15f, 15f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);
//            llXAxis.setTypeface(tfRegular);
//            ll2.setTypeface(tfRegular);
            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);
        }

    }

    private void setDataForLinechart() {
        List<Entry> NoOfEmp = new ArrayList<>();
        NoOfEmp.add(new Entry(1, 4));
        NoOfEmp.add(new Entry(2, 3));
        NoOfEmp.add(new Entry(3, 6));
        NoOfEmp.add(new Entry(4, 7));
        NoOfEmp.add(new Entry(5, 4));
        NoOfEmp.add(new Entry(6, 6));
        NoOfEmp.add(new Entry(7, 20));
        NoOfEmp.add(new Entry(8, 3));

        LineDataSet lineDataSet = new LineDataSet(NoOfEmp, "Doanh thu");
        LineData data = new LineData();
        data.addDataSet(lineDataSet);
        lineDataSet.setLineWidth(4);
        lineDataSet.setCircleColor(getResources().getColor(R.color.red));
        lineDataSet.setColors(getResources().getColor(R.color.light_blue));
        lineDataSet.setDrawValues(false);
        //draw gardient color
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.light_blue_linear_color);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(drawable);


        lineChart.setData(data);
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
            dateFrom = TimeController.getInstance().getDateAndMonthFromText(stringDateFrom);
            dateTo = TimeController.getInstance().getDateAndMonthFromText(stringDateTo);
            spinnerChooseTime.setSelection(bundle.getInt("searchType"));
        }
    }

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

