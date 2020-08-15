package com.koit.capstonproject_version_1.Controller;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.View.RevenueActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeController {

    private static TimeController mInstance;
    static Locale locale = new Locale("vi", "VN");

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static Date changeStringToDate(String stringDate) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
        } catch (Exception e) {

        }
        return date;
    }

    public Date changeStringToMonth(String stringMonth) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("MM/yyyy").parse(stringMonth);
        } catch (Exception e) {

        }
        return date;
    }

    public String changeDateToMonthString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
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
            date = TimeController.getInstance().changeStringToMonth(stringDate);
        } else {
            date = TimeController.getInstance().changeStringToDate(stringDate);
        }
        return date;
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date now = new Date();
                        if (date.after(now)) {
                            textView.setText(dateFormat.format(now));
                        } else {
                            textView.setText(dateFormat.format(date));
                        }
                    }
                }).display();
    }


    public void chooseMonthDialog(final TextView textView, Date defaultDate, Context context) {
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
                        Date now = new Date();
                        if (date.after(now)) {
                            textView.setText(dateFormat.format(now));
                        } else
                            textView.setText(dateFormat.format(date));
                    }
                }).display();
    }

}
