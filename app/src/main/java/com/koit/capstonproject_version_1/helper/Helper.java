package com.koit.capstonproject_version_1.helper;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class Helper  {
    public static Helper mInstance;

    public Helper() {
    }

    public static Helper getInstance() {
        if (mInstance==null) mInstance = new Helper();
        return mInstance;
    }

    }
