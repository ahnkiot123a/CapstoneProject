package com.koit.capstonproject_version_1.View;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class RevenueActivvity extends AppCompatActivity {
    BarChart barChart;
    Spinner spinnerChooseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_revenue_activvity);
        barChart = findViewById(R.id.barChart);
        spinnerChooseTime = findViewById(R.id.spinnerChooseTime);
        setSpinner();

        ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
        NoOfEmp.add(new BarEntry(0, 3));
        NoOfEmp.add(new BarEntry(1, 4));
        NoOfEmp.add(new BarEntry(3, 6));
        NoOfEmp.add(new BarEntry(4, 10));


        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Doanh thu");
        barChart.animateY(1000);
        BarData data = new BarData();
        data.addDataSet(bardataset);
        bardataset.setColors(getResources().getColor(R.color.light_blue));
        barChart.setData(data);
        barChart.invalidate();
        barChart.getDescription().setText("");
    }

    private void setSpinner() {
        String[] statusList = {"Tất cả các ngày", "Hôm qua", "Hôm kia"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_near_arrow, statusList);
        spinnerChooseTime.setAdapter(statusAdapter);
    }
}