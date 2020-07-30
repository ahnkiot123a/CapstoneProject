package com.koit.capstonproject_version_1.Model.UIModel;

import java.text.NumberFormat;
import java.util.Locale;

public class Money {

    private static Money mInstance;

    public Money() {
    }

    public static Money getInstance() {
        if (mInstance == null) {
            mInstance = new Money();
        }
        return mInstance;
    }

    public String formatVN(long money){
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        return vn.format(money);
    }


}
