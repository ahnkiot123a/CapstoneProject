package com.koit.capstonproject_version_1.Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeController {

    private static TimeController mInstance;

    public TimeController() {
    }

    public static TimeController getInstance() {
        if (mInstance == null) {
            mInstance = new TimeController();
        }
        return mInstance;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(calendar.getTime());
    }
}
