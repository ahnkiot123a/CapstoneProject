package com.koit.capstonproject_version_1.Controller;

import java.text.ParseException;
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

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(calendar.getTime());
    }

    //Calculate number of days between 2 date
    public long dayDiff(Date start, Date end) {
        return (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
    }

    //check whether a date is between start date and end date
    public boolean isInInterval(Date checkDate, Date start, Date end) {
        if (dayDiff(checkDate, start) * dayDiff(checkDate, end) <= 0) return true;
        return false;
    }

    //Check whether a date is in given number of days before
    public boolean isInNumOfDays(int numOfDays, Date checkDate) {
        Calendar cal = Calendar.getInstance();
        Date current = cal.getTime();
        long diffDays = dayDiff(checkDate, current);
        if (diffDays < 0 || diffDays > numOfDays) return false;
        return true;
    }

    //Convert String to Date
    public Date convertStrToDate(String str) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //Convert date to stringd
    public String convertDateToStr(Date date) {
        String str = null;
        try {
            str = new SimpleDateFormat("dd-MM-yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String plusDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        Date da = cal.getTime();
        return convertDateToStr(da);
    }
}
