package com.koit.capstonproject_version_1.View;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.koit.capstonproject_version_1.Model.UIModel.StatusBar;
import com.koit.capstonproject_version_1.R;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;


public class RevenueActivvity extends AppCompatActivity {
    BarChart barChart;
    Spinner spinnerChooseTime;
    TextView tvFrom, tvTo;
    static int yy, mm, dd;
    ConstraintLayout timeLayout;
    ConstraintLayout revenueLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.setStatusBar(this);
        setContentView(R.layout.activity_revenue_activvity);
        barChart = findViewById(R.id.barChart);
        tvFrom = findViewById(R.id.tvFromDate);
        TextView tvTo = findViewById(R.id.tvToDate);
        spinnerChooseTime = findViewById(R.id.spinnerChooseTypeTime);
        revenueLayout = findViewById(R.id.revenueLayout);
        timeLayout = findViewById(R.id.timeLayout);
        setSpinner();

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

    private void setSpinner() {
        String[] statusList = {"Theo ngày", "Theo tháng"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_near_arrow, statusList);
        spinnerChooseTime.setAdapter(statusAdapter);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void transferToDetail(View view) {
        Intent intent = new Intent(RevenueActivvity.this, DetailReveneuActivity.class);
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
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void chooseTimeTo(View view) {
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            yy = calendar.get(Calendar.YEAR);
            mm = calendar.get(Calendar.MONTH);
            dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            populateSetDate(yy, mm + 1, dd);
        }
    }

    public void populateSetDate(int year, int month, int day) {
        tvTo.setText(day + "/" + month + "/" + year);
    }
}

