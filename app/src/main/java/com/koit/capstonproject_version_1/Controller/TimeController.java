package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeController {

    private static TimeController mInstance;
    static Locale locale = new Locale("vi", "VN");
    private Date dateChose = null;


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
        Date startStart = new Date(start.getYear(), start.getMonth(), start.getDate());
        Log.d("startDate", startStart.toString());
        Date endStart = new Date(end.getYear(), end.getMonth(), end.getDate());
        return (endStart.getTime() - startStart.getTime()) / (1000 * 60 * 60 * 24);
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
        if (diffDays < 0 || diffDays > numOfDays - 1) return false;
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

    public String changeDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public Date changeStringDayToDate(String stringDate) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date changeStringMonthToFirstDayOfMonth(String stringMonth) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("MM-yyyy").parse(stringMonth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public String changeDateToMonthString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        return dateFormat.format(date);
    }

    public void setCurrentMonth(TextView tvFrom, TextView tvTo) {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_YEAR, 1);
        Date firstDayOfYear = c.getTime();
        Date thisMonth = new Date();
        tvFrom.setText(TimeController.getInstance().changeDateToMonthString(firstDayOfYear));
        tvTo.setText(TimeController.getInstance().changeDateToMonthString(thisMonth));
    }

    public void setCurrentDate(TextView tvFrom, TextView tvTo) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDay = cal.getTime();
        Date today = new Date();
        tvFrom.setText(TimeController.getInstance().changeDateToString(firstDay));
        tvTo.setText(TimeController.getInstance().changeDateToString(today));
    }

    public Date getDateAndMonthFromText(String stringDate, Date date) {
        if (stringDate.length() == 7) {
            date = TimeController.getInstance().changeStringMonthToFirstDayOfMonth(stringDate);
        } else {
            date = TimeController.getInstance().changeStringDayToDate(stringDate);
        }
        return date;
    }

    //return the first day of month
    public Date changeStringDayToFirstDayOfMonth(String day) {
        Date date = new Date();
        Date firstDay = new Date();
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            firstDay = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstDay;
    }

    public Date getDateAndMonthFromText(String stringDate) {
        Date date = new Date();
        if (stringDate.length() == 7) {
            date = TimeController.getInstance().changeStringMonthToFirstDayOfMonth(stringDate);
        } else {
            date = TimeController.getInstance().changeStringDayToDate(stringDate);
        }
        return date;
    }

    public int getDateInYear(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        //first day of year
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date start = cal.getTime();

//        //last day of year
//        cal.set(Calendar.YEAR, year);
//        cal.set(Calendar.MONTH, 11);
//        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
//        Date end = cal.getTime();

        long diff = date.getTime() - start.getTime();
        int dateInYear = (int) (diff / (24 * 60 * 60 * 1000)) + 1;
        return dateInYear;
    }

    //return month from 2020, if 01/2021, month must be 13
    public int getMonthFrom2020(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return (year - 2020) * 12 + month;
    }

    public void chooseDayDialog(final TextView textView, Date defaultDate, Context context) {
        // create a new locale
        new SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .customLocale(locale)
                .displayMonthNumbers(true)
                .displayYears(true)
                .defaultDate(defaultDate)
                .displayDaysOfMonth(true)
                .titleTextColor(Color.parseColor("#1fb34a"))
                .mainColor(Color.parseColor("#1fb34a"))
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .title("Chọn ngày")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date now = new Date();
                        if (date.after(now)) {
                            textView.setText(dateFormat.format(now));
                        } else {
                            textView.setText(dateFormat.format(date));
                        }
                    }
                }).display();
    }

    public void chooseDayDialog(final TextView textView, Date defaultDate, Context context, String sMinimumDate) {

        final Date minimumDate = changeStringDayToDate(sMinimumDate);
        // create a new locale
        new SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .customLocale(locale)
                .displayMonthNumbers(true)
                .displayYears(true)
                .defaultDate(defaultDate)
                .displayDaysOfMonth(true)
                .titleTextColor(Color.parseColor("#1fb34a"))
                .mainColor(Color.parseColor("#1fb34a"))
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .title("Chọn ngày")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        //if choseDate after now, set to Now
                        Date now = new Date();
                        if (date.after(now)) {
                            textView.setText(dateFormat.format(now));
                        } else {
                            //if choseDate before minumumDate, set to minimumDate
                            if (date.before(minimumDate)) {
                                textView.setText(dateFormat.format(minimumDate));
                            } else
                                textView.setText(dateFormat.format(date));
                        }

                    }
                }).display();
    }

    public void chooseMonthDialog(final TextView textView, Date defaultDate, Context context, String sMinimumDate) {
        final Date minimumDate = changeStringDayToDate(sMinimumDate);
        new SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .defaultDate(defaultDate)
                .customLocale(locale)
                .displayMonthNumbers(true)
                .displayYears(true)
                .titleTextColor(Color.parseColor("#1fb34a"))
                .mainColor(Color.parseColor("#1fb34a"))
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .title("Chọn tháng")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        Date now = new Date();
                        if (date.after(now)) {
                            textView.setText(changeDateToMonthString(now));
                        } else {
                            if (date.before(minimumDate)) {
                                textView.setText(changeDateToMonthString(minimumDate));
                            } else
                                textView.setText(changeDateToMonthString(date));
                        }

                    }
                }).display();
    }

    public Date chooseDayInOrderHistory(Context context) {

        // create a new locale
        Date defaultDate = TimeController.getInstance().convertStrToDate(getCurrentDate());
        new SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayMonth(true)
                .customLocale(locale)
                .displayMonthNumbers(true)
                .displayYears(true)
                .defaultDate(defaultDate)
                .displayDaysOfMonth(true)
                .titleTextColor(Color.parseColor("#1fb34a"))
                .mainColor(Color.parseColor("#1fb34a"))
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                    }
                })
                .title("Chọn ngày")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date now = new Date();
                        if (date.after(now)) {
                            dateChose = now;
                        } else {
                            dateChose = date;
                        }
                    }
                }).display();
        return dateChose;
    }

    public String setDate(int year, int month, int day) {
        month += 1;
        String date = day + "-" + month + "-" + year;
        if (month < 10) {
            date = day + "-0" + month + "-" + year;
        }
        if (day < 10) {
            date = "0" + day + "-" + month + "-" + year;
        }
        if (month < 10 && day < 10) {
            date = "0" + day + "-0" + month + "-" + year;
        }
        return date;
    }

}
