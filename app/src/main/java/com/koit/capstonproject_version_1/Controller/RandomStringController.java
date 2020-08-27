package com.koit.capstonproject_version_1.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

    public String randomString() {
//        String userID = UserDAO.getInstance().getUserID();
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String result = "PRODUCT" + "_" + timeStamp + "_" + userID;

        return UUID.randomUUID().toString();
    }

    public static String randomID() {
        return UUID.randomUUID().toString();
    }

    public String randomDebtorId() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String result = "KH" + "" + timeStamp + "";

        return result;
    }

    public String randomInvoiceId() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int random_int = (int)(Math.random() * (999 - 100 + 1) + 100);

        String result = "HD" + "" + timeStamp + ""+random_int;
        return result;
    }
    public String randomDebtPaymentId() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int random_int = (int)(Math.random() * (999 - 100 + 1) + 100);

        String result = "TN" + "" + timeStamp + ""+random_int;

        return result;
    }

}
