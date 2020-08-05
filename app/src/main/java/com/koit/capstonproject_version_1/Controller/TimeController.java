package com.koit.capstonproject_version_1.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeController {

    private static TimeController mInstance;

    public TimeController() {
    }

    public static TimeController getInstance(){
        if(mInstance != null){
            mInstance = new TimeController();
        }
        return mInstance;
    }

    public String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
