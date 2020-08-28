package com.koit.capstonproject_version_1;

import com.koit.capstonproject_version_1.Controller.TimeController;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;


public class TimeControllerUnitTest {
    TimeController timeController = new TimeController();


    @Test
    public void convertStrToDate_isCorrect() {
        Date firstDay =  timeController.convertStrToDate("01-08-2020");
        Assert.assertEquals(firstDay, timeController.changeStringDayToFirstDayOfMonth("28-08-2020"));
    }

    @Test
    public void dayDiff_isCorrect() {
//        Date startDate =
//        assertEquals(7, timeController.dayDiff());
    }


}