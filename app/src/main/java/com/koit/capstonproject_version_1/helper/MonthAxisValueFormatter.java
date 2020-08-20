package com.koit.capstonproject_version_1.helper;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MonthAxisValueFormatter extends ValueFormatter {

    private final String[] mMonths = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    };

    private final BarLineChartBase<?> chart;

    public MonthAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

        int month = (int) value;
        String year = String.valueOf(determineYear(month)).substring(2, 4);

        String monthName = mMonths[month % mMonths.length];
        String yearName = "/" + year;

        if (chart.getVisibleXRange() > 30 * 6) {
            return monthName;
        } else {
            return monthName + yearName;
        }
    }


    // minimum year is 2020, if month is 13, this month is 2021
    private int determineYear(int months) {
        return months / 12 + 2020;
    }
}