package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.DayAxisValueFormatter;
import com.koit.capstonproject_version_1.helper.MonthAxisValueFormatter;
import com.koit.capstonproject_version_1.helper.MyMarkerView;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;
import com.koit.capstonproject_version_1.helper.CurrencyValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RevenueController {
    private BarChart chart;
    private Activity activity;
    private List<Invoice> listInvoice;
    private InvoiceHistoryDAO invoiceHistoryDAO;
    private LineChart lineChart;
    public RevenueController(Activity activity, BarChart chart, LineChart lineChart) {
        this.activity = activity;
        invoiceHistoryDAO = new InvoiceHistoryDAO();
        this.chart = chart;
        this.lineChart = lineChart;
    }

    public void getListInvoiceAfter(Date dateFrom, Date dateTo,
                                    final TextView tvTotal, int searchByType) {
        Log.d("checkDateFromAndTo", dateFrom.toString() + "\n" + dateTo.toString());
        setDataForBarAndLinechart(dateFrom, dateTo, searchByType);
        tvTotal.setText(Money.getInstance().formatVN(getTotalRevenue(listInvoice, dateFrom, dateTo, searchByType)));
    }

    public void getListInvoiceFirstTimme(final Date dateFrom, final Date dateTo, final LottieAnimationView animationView,
                                         final TextView tvTotal, final ShimmerFrameLayout shimmerFrameLayout,
                                         LinearLayout layoutNotFoundItem, LinearLayout layoutChart) {
        listInvoice = new ArrayList<>();
        final IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                //get all invoice
                if (invoice != null && !invoice.isDrafted()) {
                    long total = invoice.getTotal();
                    boolean isExist = false;
                    for (Invoice il : listInvoice
                    ) {
                        if (invoice.getInvoiceDate().equals(il.getInvoiceDate())) {
                            isExist = true;
                            total += il.getTotal();
                            il.setTotal(total);
                        }
                    }
                    if (!isExist) {
                        listInvoice.add(invoice);
                    }
                    //first time when intent to RevenueActivity
                    //set UI loading
                    animationView.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setShimmer(null);
                    setDataForBarAndLinechart(dateFrom, dateTo, 0);
                    //set textview total revenue
                    tvTotal.setText(Money.getInstance().formatVN(getTotalRevenue(listInvoice, dateFrom, dateTo, 0)));

                }
            }
        };
        invoiceHistoryDAO.getInvoiceList(iInvoice, layoutNotFoundItem, animationView,layoutChart);
    }

    public void setDataForBarAndLinechart(Date dateFrom, Date dateTo, int searchByType) {
        //search by day
        if (0 == searchByType) {
            int x = 0;
            long y = 0;
            List<BarEntry> elements = new ArrayList<>();
            List<Entry> entriesLineChart = new ArrayList<>();

            //days between 2 days
            for (int i = TimeController.getInstance().getDateInYear(dateFrom); i < TimeController.getInstance().getDateInYear(dateTo) + 1; i++) {
                String stringDateFrom = TimeController.getInstance().changeDateToString(dateFrom);
                // xAxis in chart
                x = i + 1;
                y = 0;
                String day = "";

                for (Invoice iv : listInvoice) {
                    if (stringDateFrom.equals(iv.getInvoiceDate()))
                        y += iv.getTotal();
                }
                //for Bar chart
                elements.add(new BarEntry(x, y, TimeController.getInstance().changeDateToString(dateFrom)));
                //for Line chart
                entriesLineChart.add(new Entry(x, y, TimeController.getInstance().changeDateToString(dateFrom)));

                //add 1 day to dateFrom
                Calendar c = Calendar.getInstance();
                c.setTime(dateFrom);
                c.add(Calendar.DATE, 1);
                dateFrom = c.getTime();
            }
            //set Data for bar chart
            BarDataSet bardataset = new BarDataSet(elements, "Doanh thu");
            BarData data = new BarData();
            data.addDataSet(bardataset);
            chart.setData(data);


            bardataset.setColors(activity.getResources().getColor(R.color.light_blue));
            //data in top of bar
            bardataset.setDrawValues(false);
            //search by day
            setUpBarChart(true);

            //set data for line chart
            LineDataSet lineDataSet = new LineDataSet(entriesLineChart, "Doanh thu");
            LineData dataLineChart = new LineData();
            dataLineChart.addDataSet(lineDataSet);
            lineDataSet.setLineWidth(4);
            lineDataSet.setCircleColor(activity.getResources().getColor(R.color.red));
            lineDataSet.setColors(activity.getResources().getColor(R.color.light_blue));
            lineDataSet.setDrawValues(false);
            //draw gardient color
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.light_blue_linear_color);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.setData(dataLineChart);

            setUpLineChart(true);
        } else
        //searh by Month
        {
            int x = 0;
            long y = 0;
            List<BarEntry> elements = new ArrayList<>();
            List<Entry> entriesLineChart = new ArrayList<>();

            //month between 2 months
            for (int i = TimeController.getInstance().getMonthFrom2020(dateFrom); i < TimeController.getInstance().getMonthFrom2020(dateTo) + 1; i++) {
                // xAxis in chart
                x = i;
                y = 0;

                for (Invoice iv : listInvoice) {
                    if (dateFrom.equals(TimeController.getInstance().changeStringDayToFirstDayOfMonth(iv.getInvoiceDate())))
                        y += iv.getTotal();
                    Log.d("checkDateInInvoice", TimeController.getInstance().changeStringDayToFirstDayOfMonth(iv.getInvoiceDate()) + "");
                }
                //add entry to barchart
                elements.add(new BarEntry(x, y, TimeController.getInstance().changeDateToMonthString(dateFrom)));
                //add entry to line chart
                entriesLineChart.add(new Entry(x, y, TimeController.getInstance().changeDateToMonthString(dateFrom)));

                //add 1 month to dateFrom
                Calendar c = Calendar.getInstance();
                c.setTime(dateFrom);
                c.add(Calendar.MONTH, 1);
                dateFrom = c.getTime();
                Log.d("checkDateFromRC", dateFrom.toString());
            }
            Log.d("checkElemetns", elements.toString());
            BarDataSet bardataset = new BarDataSet(elements, "Doanh thu");
            BarData data = new BarData();
            data.addDataSet(bardataset);
            chart.setData(data);

            bardataset.setColors(activity.getResources().getColor(R.color.light_blue));
            //data in top of bar
            bardataset.setDrawValues(false);
            setUpBarChart(false);

            //set data for line chart
            LineDataSet lineDataSet = new LineDataSet(entriesLineChart, "Doanh thu");
            LineData dataLineChart = new LineData();
            dataLineChart.addDataSet(lineDataSet);
            lineDataSet.setLineWidth(4);
            lineDataSet.setCircleColor(activity.getResources().getColor(R.color.red));
            lineDataSet.setColors(activity.getResources().getColor(R.color.light_blue));
            lineDataSet.setDrawValues(false);
            //draw gardient color
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.light_blue_linear_color);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.setData(dataLineChart);
            //search by month
            setUpLineChart(false);
        }

    }

    //set up UI for Bar Graph
    public void setUpBarChart(boolean isSearchByDay) {
        chart.invalidate();
        chart.getDescription().setText("");

        chart.animateY(1000);
        chart.setVisibleXRangeMaximum(1000); // allow 50 values to be displayed at once on the x-axis, not more
        chart.moveViewToX(10); // set the left edge of the chart to x-index 10
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        MyMarkerView mv = new MyMarkerView(activity, R.layout.custom_marker_view);
        mv.setChartView(chart);
        chart.setMarker(mv);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        ValueFormatter xAxisFormatter;
        if (isSearchByDay)
            xAxisFormatter = new DayAxisValueFormatter(chart);
        else {
            //search by month
            xAxisFormatter = new MonthAxisValueFormatter(chart);
        }
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(12);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setAxisLineWidth(2);
        xAxis.setValueFormatter(xAxisFormatter);
//        xAxis.setValueFormatter(new MyXAxisValueFormatter()) ;

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(14);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(new CurrencyValueFormatter());
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

    }

    //setup Line chart
    public void setUpLineChart(boolean isSearchByDay) {
        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(activity, R.layout.custom_marker_view);
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
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.enableGridDashedLine(5f, 10f, 2f);
            ValueFormatter xAxisFormatter;
            if (isSearchByDay)
                xAxisFormatter = new DayAxisValueFormatter(chart);
            else {
                //search by month
                xAxisFormatter = new MonthAxisValueFormatter(chart);
            }
            xAxis.setValueFormatter(xAxisFormatter);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = lineChart.getAxisLeft();
            // disable dual axis (only use LEFT axis)
            lineChart.getAxisRight().setEnabled(false);
            yAxis.setTextSize(14);
            // horizontal grid lines
            yAxis.setGranularity(1f);
            yAxis.enableGridDashedLine(5f, 10f, 2f);
            yAxis.setValueFormatter(new CurrencyValueFormatter());
            yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

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

    //get Total Revenue betweeen 2 dates
    private long getTotalRevenue(List<Invoice> listInvoice, Date dateFrom, Date dateTo, int searchByType) {
        Log.d("ChekDateFrom", dateFrom.toString());
        long total = 0;
        for (Invoice invoice : listInvoice
        ) {
            Date dateInInvoice;
            //search by day
            if (searchByType == 0) {
                dateInInvoice = TimeController.getInstance().changeStringDayToDate(invoice.getInvoiceDate());
            } else {
                dateInInvoice = TimeController.getInstance().changeStringDayToFirstDayOfMonth(invoice.getInvoiceDate());
            }
            if ((dateFrom.before(dateInInvoice) || (dateFrom.equals(dateInInvoice))) &&
                    (dateTo.after(dateInInvoice) || dateTo.equals(dateInInvoice))) {
                total += invoice.getTotal();
            }
        }
        return total;

    }

    public List<Invoice> getListInvoice() {
        return listInvoice;
    }
}
