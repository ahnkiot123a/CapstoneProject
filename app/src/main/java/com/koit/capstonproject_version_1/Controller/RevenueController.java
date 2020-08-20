package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.koit.capstonproject_version_1.Controller.Interface.IInvoice;
import com.koit.capstonproject_version_1.Model.Invoice;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.helper.DayAxisValueFormatter;
import com.koit.capstonproject_version_1.helper.MyMarkerView;
import com.koit.capstonproject_version_1.dao.InvoiceHistoryDAO;
import com.koit.capstonproject_version_1.helper.MyValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RevenueController {
    private BarChart chart;
    Activity activity;
    List<Invoice> listInvoice;
    InvoiceHistoryDAO invoiceHistoryDAO;

    public RevenueController(Activity activity, BarChart chart) {
        this.activity = activity;
        invoiceHistoryDAO = new InvoiceHistoryDAO();
        this.chart = chart;
    }

    public void getListInvoice(final Date dateFrom, final Date dateTo, final LottieAnimationView animationView, final TextView tvTotal, final ShimmerFrameLayout shimmerFrameLayout) {
        listInvoice = new ArrayList<>();

        final IInvoice iInvoice = new IInvoice() {
            @Override
            public void getInvoice(Invoice invoice) {
                //if dateFrom> dateTo swap
                if (invoice != null && !invoice.isDrafted()) {
                    Date dateInInvoice = TimeController.getInstance().changeStringToDate(invoice.getInvoiceDate());
                    //date in invoice between dateFrom and dateTo
                    if ((dateFrom.before(dateInInvoice) || (dateFrom.equals(dateInInvoice))) &&
                            (dateTo.after(dateInInvoice) || dateTo.equals(dateInInvoice))) {
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
                        } else {
                        }
                        setDataForBarchart(dateFrom, dateTo);
                        //set textview total revenue
                        tvTotal.setText(Money.getInstance().formatVN(getTotalRevenue(listInvoice)));
                    }
                    //set UI loading
                    animationView.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setShimmer(null);
                }
            }
        };

        invoiceHistoryDAO.getInvoiceList(iInvoice);
    }

    public void setDataForBarchart(Date dateFrom, Date dateTo) {
        int x = 0;
        long y = 0;

        //get Diff from dateFrom to dateTo
        long diff = dateTo.getTime() - dateFrom.getTime();
        int totalDay = (int) (diff / (24 * 60 * 60 * 1000));
        Log.d("totalDay", totalDay + "");

        List<BarEntry> elements = new ArrayList<>();

        for (int i = 1; i < totalDay + 2; i++) {
            String stringDateFrom = TimeController.getInstance().changeDateToString(dateFrom);
            // xAxis in chart
            x = i + 1;
            y = 0;
            String day = "";
            for (Invoice iv : listInvoice) {
//                arrayDate = iv.getInvoiceDate().split("[-]");
//            x = Math.round(Integer.parseInt(arrayDate[0]));
                if (stringDateFrom.equals(iv.getInvoiceDate()))
                    y += iv.getTotal();
                day = iv.getInvoiceDate();
            }
            elements.add(new BarEntry(x, y, TimeController.getInstance().changeDateToString(dateFrom)));
            //add 1 day to dateFrom
            Calendar c = Calendar.getInstance();
            c.setTime(dateFrom);
            c.add(Calendar.DATE, 1);
            dateFrom = c.getTime();
        }

        BarDataSet bardataset = new BarDataSet(elements, "Doanh thu");
        BarData data = new BarData();
        data.addDataSet(bardataset);
        chart.setData(data);

        bardataset.setColors(activity.getResources().getColor(R.color.light_blue));
        //data in top of bar
        bardataset.setDrawValues(false);
        setUpBarChart();

    }
    //set up UI for Bar Graph
    private void setUpBarChart() {
        chart.invalidate();
        chart.getDescription().setText("");

        chart.animateY(1000);

        chart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
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

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);
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
        leftAxis.setValueFormatter(new MyValueFormatter());
//        leftAxis.setLabelCount(8, false);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

    }

    private long getTotalRevenue(List<Invoice> listInvoice) {
        long total = 0;
        for (Invoice invoice : listInvoice
        ) {
            total += invoice.getTotal();
        }
        return total;
    }

}
