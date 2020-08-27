package com.koit.capstonproject_version_1.Controller;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.koit.capstonproject_version_1.Controller.Interface.IDebtPaymentDetail;
import com.koit.capstonproject_version_1.Model.DebtPayment;
import com.koit.capstonproject_version_1.Model.DebtorPayment;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class DebtorPaymentController {
    private Activity activity;
    private DebtorPayment debtorPayment;
    public List<DebtorPayment> debtorPaymentList;

    public DebtorPaymentController() {
    }

    public DebtorPaymentController(Activity activity) {
        this.activity = activity;
        debtorPayment = new DebtorPayment();
        debtorPaymentList = new ArrayList<>();
    }

    public void getListDebtPayments(final TextView tvPaid, final TextView tvRemaining,
                                    final TextView tvTotalDebt, final PieChart chart) {
        IDebtPaymentDetail iDebtPaymentDetail = new IDebtPaymentDetail() {
            @Override
            public void getDebtPaymentDetail(DebtorPayment debtPaymentDetail) {
                if (debtPaymentDetail != null){
                    debtorPaymentList.add(debtPaymentDetail);
                    tvPaid.setText(Money.getInstance().formatVN(getTotalPayAmountByAllDebtors()) + " đ");
                    long paidAmount = Money.getInstance().reFormatVND(tvPaid.getText().toString());
                    long remainingAmount = Money.getInstance().reFormatVND(tvRemaining.getText().toString());
                    long totalDebt = paidAmount + remainingAmount;
                    tvTotalDebt.setText(Money.getInstance().formatVN(totalDebt) + " đ");
                    float percentPaid = (float) paidAmount / totalDebt *100;
                    setUpPieChart(percentPaid, chart);
//                    Log.d("PayAmountAllDebtors",Money.getInstance().formatVN(getTotalPayAmountByAllDebtors()));
                }
            }
        };
        debtorPayment.getListDebtPayments(iDebtPaymentDetail);
    }

    public long getTotalPayAmountByAllDebtors() {
        long total = 0;
        for (DebtorPayment debtorPayment : debtorPaymentList) {
            for (DebtPayment debtPayment : debtorPayment.getDebtPaymentList())
                total += debtPayment.getPayAmount();
        }
        return total;
    }
    private void setUpPieChart(float percentPaid, PieChart chart) {
        //Color
        try {
            final int[] MY_COLORS = {activity.getResources().getColor(R.color.yellow_chrome), activity.getResources().getColor(R.color.green_chrome)};
            List<Integer> colors = new ArrayList<>();
            for (int c : MY_COLORS) colors.add(c);
            //pupulating list of PieEntires
            List<PieEntry> pieEntires = new ArrayList<>();
            pieEntires.add(new PieEntry(100 - percentPaid, 100 - percentPaid));
            pieEntires.add(new PieEntry(percentPaid, percentPaid));

            PieDataSet dataSet = new PieDataSet(pieEntires, "");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData data = new PieData(dataSet);
            //set color in array MY_COLORS
            dataSet.setColors(colors);
            dataSet.setValueTextColor(Color.WHITE);
            data.setValueTextSize(15f);
            chart.setData(data);
            //Get the chart
            chart.setUsePercentValues(true);
            chart.getDescription().setEnabled(false);
            chart.setExtraOffsets(5, 10, 5, 5);
            chart.setCenterTextSize(18);
            chart.setDragDecelerationFrictionCoef(0.95f);

            chart.setCenterText("Đã trả "+ String.format("%.1f",percentPaid) +"%");

            chart.setDrawHoleEnabled(true);
            chart.setHoleColor(Color.WHITE);

            chart.setTransparentCircleColor(Color.WHITE);
            chart.setTransparentCircleAlpha(110);

            chart.setHoleRadius(58f);
            chart.setTransparentCircleRadius(61f);

            chart.setDrawCenterText(true);

            chart.setRotationAngle(0);
            // enable rotation of the chart by touch
            chart.setRotationEnabled(true);
            chart.setHighlightPerTapEnabled(true);

            // chart.setUnit(" €");
            // chart.setDrawUnitsInChart(true);

            chart.animateY(1400, Easing.EaseInOutQuad);
            // chart.spin(2000, 0, 360);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            chart.setEntryLabelColor(Color.BLACK);
            chart.setEntryLabelTextSize(20);
        }catch (Exception e){

        }

    }
}
