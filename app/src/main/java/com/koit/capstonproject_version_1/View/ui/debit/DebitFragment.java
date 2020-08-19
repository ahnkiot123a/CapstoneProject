package com.koit.capstonproject_version_1.View.ui.debit;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.koit.capstonproject_version_1.Controller.DebtPaymentDetailController;
import com.koit.capstonproject_version_1.Controller.DebtorController;
import com.koit.capstonproject_version_1.Model.UIModel.Money;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.List;

public class DebitFragment extends Fragment {

    private DebitViewModel debitViewModel;
    private RecyclerView recyclerViewDebitors;
    private SearchView svDebtor;
    private PieChart chart;
    private TextView tvTotalDebt, tvPaid, tvRemaining;
    private DebtorController debtorController;
    private DebtPaymentDetailController debtPaymentDetailController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        debitViewModel =
                ViewModelProviders.of(this).get(DebitViewModel.class);
        View root = inflater.inflate(R.layout.fragment_debit, container, false);
        chart = root.findViewById(R.id.pieChart);
        svDebtor = root.findViewById(R.id.svDebtor);
        tvRemaining = root.findViewById(R.id.tvRemaining);
        tvTotalDebt = root.findViewById(R.id.tvTotalDebt);
        tvPaid = root.findViewById(R.id.tvPaid);
        debitViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        setUpPieChart();
        recyclerViewDebitors = root.findViewById(R.id.recyclerViewDebitors);
        debtorController = new DebtorController(this.getContext());
        debtorController.getListDebtor(recyclerViewDebitors, tvRemaining);
        debtorController.etSearchEventListDebtor(svDebtor);

        debtPaymentDetailController = new DebtPaymentDetailController(this.getActivity());
        debtPaymentDetailController.getListDebtPayments(tvPaid);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long paidAmount = Money.getInstance().reFormatVN(tvPaid.getText().toString());
                long remainingAmount = Money.getInstance().reFormatVN(tvRemaining.getText().toString());
                long totalDebt = paidAmount + remainingAmount;
                tvTotalDebt.setText(Money.getInstance().formatVN(totalDebt));
                float percentPaid = (float) paidAmount / totalDebt *100;
//                setUpPieChart(percentPaid);
                Log.d("percent", percentPaid +"");
            }
        },1000);
        return root;
    }

    //https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/PieChartActivity.java
    private void setUpPieChart() {
        //Color
        final int[] MY_COLORS = {getResources().getColor(R.color.yellow_chrome), getResources().getColor(R.color.green_chrome)};
        List<Integer> colors = new ArrayList<>();
        for (int c : MY_COLORS) colors.add(c);
        //pupulating list of PieEntires
        List<PieEntry> pieEntires = new ArrayList<>();
        pieEntires.add(new PieEntry(60, 60));
        pieEntires.add(new PieEntry(40, 40));

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

        chart.setCenterText("Đã trả 40%");

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
    }
}