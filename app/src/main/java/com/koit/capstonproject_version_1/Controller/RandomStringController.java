package com.koit.capstonproject_version_1.Controller;

import com.koit.capstonproject_version_1.dao.CategoryDAO;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomStringController {

    private static RandomStringController mInstance;

    public RandomStringController() {
    }

    public static RandomStringController getInstance() {
        if (mInstance == null) {
            mInstance = new RandomStringController();
        }
        return mInstance;
    }

    public String randomString(){
        String userID = UserDAO.getInstance().getUserID();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String result = "PRODUCT" + "_" + timeStamp + "_" + userID;

        return result;
    }
    public String randomCustomerId(){
        String userID = UserDAO.getInstance().getUserID();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String result = "KH" + "_" + timeStamp + "_" + userID;

        return result;
    }

}
