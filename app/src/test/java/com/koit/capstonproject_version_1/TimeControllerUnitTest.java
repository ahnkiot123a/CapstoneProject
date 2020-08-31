package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.TimeController;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;


public class TimeControllerUnitTest {
    TimeController timeController;

    @Before
    public void beforeTest(){
        timeController = new TimeController();
    }
    @Test
    public void convertStrToDate_isCorrect() {
        Date firstDay =  timeController.convertStrToDate("01-08-2020");
        assertEquals(firstDay, timeController.changeStringDayToFirstDayOfMonth("28-08-2020"));
    }

    @Test
    public void dayDiff_isCorrect() {
        Date startDate =  timeController.convertStrToDate("01-08-2020");
        Date endDate = timeController.convertStrToDate("01-08-2020");
        //test same day
        assertEquals(0,timeController.dayDiff(startDate,endDate));

        //before 1 day
        startDate =  timeController.convertStrToDate("31-12-2019");
        endDate = timeController.convertStrToDate("01-01-2020");
        assertEquals(1, timeController.dayDiff(startDate,endDate));

        //before 31 days
        startDate =  timeController.convertStrToDate("25-12-2019");
        endDate = timeController.convertStrToDate("01-01-2020");
        assertEquals(7, timeController.dayDiff(startDate,endDate));

        //startDate > end date
        startDate =  timeController.convertStrToDate("21-08-2020");
        endDate = timeController.convertStrToDate("15-08-2020");
        assertEquals(-6, timeController.dayDiff(startDate,endDate));
    }

    @Test
    public void getDateInYear_isCorrect() {
        Date date =  timeController.convertStrToDate("01-1-2020");
        //start day of year
        assertEquals(1,timeController.getDateInYear(date));
        //before 1 day
        date = timeController.convertStrToDate("31-12-2020");
        assertEquals(366, timeController.getDateInYear(date));

        //other day in year
        date = timeController.convertStrToDate("03-03-2020");
        assertEquals(63, timeController.getDateInYear(date));
    }
    @Test
    public void getMonthFrom2020_isCorrect(){
        Date date =  timeController.convertStrToDate("01-1-2020");
        //start month of year
        assertEquals(0,timeController.getMonthFrom2020(date));

        //other month in year
        date = timeController.convertStrToDate("31-08-2020");
        assertEquals(7, timeController.getMonthFrom2020(date));

        //month of other ywar
        date = timeController.convertStrToDate("03-03-2021");
        assertEquals(14, timeController.getMonthFrom2020(date));
    }
}